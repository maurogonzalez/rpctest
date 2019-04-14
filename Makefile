DOCKER_ROOT_DIR = /src
PROTO_DIR = modules/rpctest/app/target/scala-2.12/resource_managed/main/proto
GOLIB_DIR = gotest
UI_DIR = modules/rpctest/ui
TSLIB_DIR = $(UI_DIR)/ui/src/proto
PROTOCOL_NAME = protocol
SERVICE_NAME = Greeter
LOCAL_HOST_NAME := host.docker.internal#$(shell hostname)

$(GOLIB_DIR)/protocol.pb.go: $(PROTO_DIR)/protocol.proto
	protoc -I $(PROTO_DIR) protocol.proto --go_out=plugins=grpc:$(GOLIB_DIR)

$(PROTO_DIR)/protocol.proto:
	sbt "rpctest / idlGen"

run-server:
	sbt "rpctest / run"

run-client: $(GOLIB_DIR)/protocol.pb.go
	go run gotest/cmd/*.go

run-proxy:
	sed 's/LOCAL_HOST_NAME/$(LOCAL_HOST_NAME)/' envoy.yaml > envoy.local.yaml
	docker run -ti --rm \
		--mount type=bind,source="$(PWD)",target=/src \
		-p 8080:8080 -p 9901:9901 \
		envoyproxy/envoy:v1.10.0 envoy \
			--config-path /src/envoy.local.yaml \
			--log-level trace

run-ui: $(UI_DIR)/node_modules $(TSLIB_DIR)/$(PROTOCOL_NAME)_pb.js
	cd modules/rpctest/ui && npm start

$(UI_DIR)/node_modules:
	cd modules/rpctest/ui && npm install

$(TSLIB_DIR)/$(PROTOCOL_NAME)_pb.js:
	rm -rf $(TSLIB_DIR)
	mkdir -p $(TSLIB_DIR)
	docker run --rm -it \
		--mount type=bind,source="$(PWD)",target=$(DOCKER_ROOT_DIR) \
		grpcweb/common protoc \
				-I=$(DOCKER_ROOT_DIR)/$(PROTO_DIR) $(PROTOCOL_NAME).proto \
		--js_out=import_style=commonjs:$(DOCKER_ROOT_DIR)/$(TSLIB_DIR) \
			--grpc-web_out=import_style=typescript,mode=grpcwebtext:$(DOCKER_ROOT_DIR)/$(TSLIB_DIR)

	# Make it run with TypeScript and react-scripts.
	# Use GNU sed (instead of possibly BSD sed) to add newlines.
	docker run --rm -it \
		--mount type=bind,source="$(PWD)",target=$(DOCKER_ROOT_DIR) \
		debian:stretch sed -i \
			-e '1s;^;/* eslint-disable */\n;' \
			-e "s/\(var jspb =\)/\/\/ @ts-ignore\n\1/" \
			$(DOCKER_ROOT_DIR)/$(TSLIB_DIR)/$(PROTOCOL_NAME)_pb.js

clean:
	rm -rf $(GOLIB_DIR)/protocol.pb.go $(PROTO_DIR)/protocol.proto $(TSLIB_DIR)
