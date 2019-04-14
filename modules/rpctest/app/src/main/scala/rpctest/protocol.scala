package rpctest {
  import higherkindness.mu.rpc.protocol._

  object protocol {
    @message
    case class HelloRequest(name: String)

    @message
    case class HelloResponse(message: String)

    @service(Protobuf)
    trait Greeter[F[_]] {
      def SayHello(request: HelloRequest): F[HelloResponse]
    }
  }
}
