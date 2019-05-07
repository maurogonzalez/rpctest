ThisBuild / scalaVersion := "2.12.7"

lazy val rpctest = (project in file("modules/rpctest/app"))
  .settings(
    name := "RPCTest",
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-channel" % "0.18.0",
      "io.higherkindness" %% "mu-rpc-server" % "0.18.0",
      "co.fs2" %% "fs2-core" % "1.0.4",
      "io.chrisdavenport" %% "log4cats-slf4j"   % "0.3.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ),
    idlType := "proto",
    srcGenSerializationType := "ProtoWithSchema",
    sourceGenerators in Compile += (Compile / srcGen).taskValue,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1"
      cross CrossVersion.patch)
  )
