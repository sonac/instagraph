import sbt._

object Dependencies {

  lazy val Http4sVersion = "0.19.0-M4"
  lazy val CirceVersion = "0.10.0"
  lazy val Specs2Version = "4.2.0"
  lazy val LogbackVersion = "1.2.3"
  lazy val PureConfigVersion = "0.10.2"
  
  lazy val blazeServer: ModuleID = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  lazy val blazeClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % Http4sVersion
  lazy val http4sCirce: ModuleID = "org.http4s" %% "http4s-circe" % Http4sVersion
  lazy val http4sDsl: ModuleID = "org.http4s" %% "http4s-dsl" % Http4sVersion
  lazy val circeGeneric: ModuleID = "io.circe" %% "circe-generic" % CirceVersion
  lazy val circeParser: ModuleID = "io.circe" %% "circe-parser" % CirceVersion
  lazy val specsCore: ModuleID = "org.specs2" %% "specs2-core" % Specs2Version % "test"
  lazy val logback: ModuleID = "ch.qos.logback" % "logback-classic" % LogbackVersion
  lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
  
  lazy val dependencies: Seq[ModuleID] = Seq(
    blazeServer,
    blazeClient,
    http4sCirce,
    http4sDsl,
    circeGeneric,
    circeParser,
    specsCore,
    logback,
    pureConfig
  )
  
}
