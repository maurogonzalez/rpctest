package rpctest {
  import scala.concurrent.ExecutionContext
  import cats.effect.IO

  trait CommonRuntime {
    val EC = scala.concurrent.ExecutionContext.Implicits.global

    implicit val cs: cats.effect.ContextShift[cats.effect.IO] =
      cats.effect.IO.contextShift(EC)
  }
}
