package rpctest {
  import cats.effect.Async
  import cats.syntax.applicative._

  import rpctest.protocol._

  class ServiceHandler[F[_]: Async] extends Greeter[F] {
    override def SayHello(request: HelloRequest): F[HelloResponse] =
      HelloResponse(message = "Good bye!").pure[F]
  }
}
