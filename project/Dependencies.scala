import sbt._

object Dependencies {

  lazy val Http4sVersion = "0.19.0-M4"
  lazy val CirceVersion = "0.10.0"
  lazy val Specs2Version = "4.2.0"
  lazy val LogbackVersion = "1.2.3"
  lazy val PureConfigVersion = "0.10.2"
  lazy val DoobieVersion = "0.5.4"
  lazy val FlywayVersion = "5.2.4"
  
  lazy val blazeServer: ModuleID = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  lazy val blazeClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % Http4sVersion
  lazy val http4sCirce: ModuleID = "org.http4s" %% "http4s-circe" % Http4sVersion
  lazy val http4sDsl: ModuleID = "org.http4s" %% "http4s-dsl" % Http4sVersion
  lazy val circeGeneric: ModuleID = "io.circe" %% "circe-generic" % CirceVersion
  lazy val circeParser: ModuleID = "io.circe" %% "circe-parser" % CirceVersion
  lazy val specsCore: ModuleID = "org.specs2" %% "specs2-core" % Specs2Version % "test"
  lazy val logback: ModuleID = "ch.qos.logback" % "logback-classic" % LogbackVersion
  lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
  lazy val doobieCore: ModuleID = "org.tpolecat" %% "doobie-core" % DoobieVersion
  lazy val doobieHikari: ModuleID = "org.tpolecat" %% "doobie-hikari" % DoobieVersion
  lazy val doobiePostgres: ModuleID = "org.tpolecat" %% "doobie-postgres" % DoobieVersion
  lazy val flywayDB: ModuleID = "org.flywaydb" % "flyway-core" % FlywayVersion
  
  lazy val dependencies: Seq[ModuleID] = Seq(
    blazeServer,
    blazeClient,
    http4sCirce,
    http4sDsl,
    circeGeneric,
    circeParser,
    specsCore,
    logback,
    pureConfig,
    doobieCore,
    doobieHikari,
    doobiePostgres,
    flywayDB
  )
  
}
