package rpctest {
  import cats.effect.IO
  import higherkindness.mu.rpc.server._
  import protocol._

  object App {
    import gserver.implicits._

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
