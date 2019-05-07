package rpctest

import cats.effect._
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import rpctest.pkg.protocol.Test

class HelloProgram[F[_]: ConcurrentEffect: ContextShift: Timer] {

  def greeterServiceClient(host: String, port: Int)(
    implicit L: Logger[F]): Stream[F, GreeterServiceClient[F]] =
    GreeterServiceClient.createClient(host, port, sslEnabled = false)

  def runProgram(args: List[String]): Stream[F, ExitCode] = {
    for {
      logger   <- Stream.eval(Slf4jLogger.fromName[F]("hello-client-app"))
      exitCode <- clientProgram(logger)
    } yield exitCode
  }

  def clientProgram(implicit L: Logger[F]): Stream[F, ExitCode] = {
    for {
      client <- greeterServiceClient("localhost", 8000)
      _      <- Stream.eval(client.sayHello(Test.HELLO))
    } yield ExitCode.Success
  }
}

object ClientApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    new HelloProgram[IO]
      .runProgram(args)
      .compile
      .toList
      .map(_.headOption.getOrElse(ExitCode.Error))
}