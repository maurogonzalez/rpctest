package rpctest

import cats.effect.IO

import protocol._

object ClientApp {
  import gclient.implicits._

  def main(args: Array[String]): Unit = {
    serviceClient.use(_.sayHello(HelloRequest("foo"))).flatMap { result =>
      IO(println(s"Result = $result"))
    }.unsafeRunSync()
  }
}
