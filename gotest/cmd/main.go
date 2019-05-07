package main

import (
	"context"
	"fmt"

	"google.golang.org/grpc"

	pb ".."
)

func main() {
	conn, err := grpc.Dial("127.0.0.1:8000", grpc.WithInsecure())
	if err != nil {
		panic(err)
	}
	defer conn.Close()

	client := pb.NewGreeterClient(conn)
	reply, err := client.SayHello(context.Background(),
		&pb.HelloRequest{Name: "bye"})
	if err != nil {
		panic(err)
	}

	fmt.Println(reply)
}
