package rpctest {
  import cats.effect.Async
  import cats.syntax.functor._
  import io.chrisdavenport.log4cats.Logger
  import rpctest.pkg.protocol._

  class GreeterServiceHandler[F[_]:Async](implicit L: Logger[F]) extends Greeter[F] {
    override def SayHello(req: HelloRequest): F[HelloResponse] =
      L.info(s"GreeterServiceHandler - Request: $req").as(HelloResponse(req.name))
  }
}
