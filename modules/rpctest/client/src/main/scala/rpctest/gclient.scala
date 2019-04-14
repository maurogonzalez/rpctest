package rpctest

import cats.effect.IO
import higherkindness.mu.rpc._

import protocol._

object gclient {
  trait Implicits extends CommonRuntime {
    val channelFor = ChannelForAddress("localhost", 8000)

    implicit val serviceClient = Greeter.client[IO](channelFor)
  }

  object implicits extends Implicits
}
