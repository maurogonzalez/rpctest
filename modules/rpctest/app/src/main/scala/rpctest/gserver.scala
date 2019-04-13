package rpctest {
  import cats.effect.IO
  import higherkindness.mu.rpc.server._

  object gserver {
    trait Implicits extends CommonRuntime {
      implicit val greeterServiceHandler: ServiceHandler[IO] =
        new ServiceHandler[IO]
    }

    object implicits extends Implicits
  }
}
