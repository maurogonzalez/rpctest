package rpctest {
  import cats.effect.IO
  import higherkindness.mu.rpc.server._
  import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
  import rpctest.pkg.protocol._

  class ServerImplicits[F[_]] {
    object implicits {
      val EC = scala.concurrent.ExecutionContext.Implicits.global

      implicit val cs: cats.effect.ContextShift[cats.effect.IO] = cats.effect.IO.contextShift(EC)
      implicit val logger = Slf4jLogger.fromName[IO]("hello-server-app").unsafeRunSync()

      implicit val greeterServiceHandler: GreeterServiceHandler[IO] =
        new GreeterServiceHandler[IO]
    }
  }

  object ServerApp {
    val implicits = new ServerImplicits[IO]
    import implicits.implicits._

    def main(args: Array[String]): Unit = {
      val runServer = for {
        grpcConfig <- Greeter.bindService[IO]
        server <- GrpcServer.default[IO](8000, List(AddService(grpcConfig)))
        runServer <- GrpcServer.server[IO](server)
      } yield runServer

      runServer.unsafeRunSync()
    }
  }
}
