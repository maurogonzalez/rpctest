PROTO_DIR = modules/rpctest/app/target/scala-2.12/resource_managed/main/proto
GOLIB_DIR = gotest

$(GOLIB_DIR)/protocol.pb.go: $(PROTO_DIR)/protocol.proto
	protoc -I $(PROTO_DIR) protocol.proto --go_out=plugins=grpc:$(GOLIB_DIR)

$(PROTO_DIR)/protocol.proto:
	sbt "rpctest / idlGen"

run-server:
	sbt "rpctest / run"

run-client: $(GOLIB_DIR)/protocol.pb.go
	go run gotest/cmd/*.go
