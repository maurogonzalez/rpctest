package rpctest

import java.net.InetAddress

import cats.effect.{ ConcurrentEffect, ContextShift, Effect, Resource, Timer }
import cats.implicits._
import higherkindness.mu.rpc.ChannelForAddress
import higherkindness.mu.rpc.channel.{ ManagedChannelInterpreter, UsePlaintext }
import io.chrisdavenport.log4cats.Logger
import io.grpc.{ CallOptions, ManagedChannel }
import rpctest.pkg.protocol._

trait GreeterServiceClient[F[_]] {

  def sayHello(name: Test): F[HelloResponse]

}

object GreeterServiceClient {

  def apply[F[_]](client: Greeter[F])(
    implicit F: Effect[F],
    L: Logger[F],
    T: Timer[F]): GreeterServiceClient[F] =
    new GreeterServiceClient[F] {

      override def sayHello(name: Test): F[HelloResponse] =
        for {
          result <- client.SayHello(HelloRequest(name))
          _      <- L.info(s"GreeterServiceClient - Request: $name - Result: $result")
        } yield result
    }

  def createClient[F[_]: ContextShift: Logger: Timer](
                                                       hostname: String,
                                                       port: Int,
                                                       sslEnabled: Boolean = true)(
                                                       implicit F: ConcurrentEffect[F]): fs2.Stream[F, GreeterServiceClient[F]] = {

    val channel: F[ManagedChannel] =
      F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
        val channelFor    = ChannelForAddress(ip, port)
        val channelConfig = if (!sslEnabled) List(UsePlaintext()) else Nil
        new ManagedChannelInterpreter[F](channelFor, channelConfig).build
      }

    def clientFromChannel: Resource[F, Greeter[F]] =
      Greeter.clientFromChannel(channel, CallOptions.DEFAULT)

    fs2.Stream.resource(clientFromChannel).map(GreeterServiceClient(_))
  }
}