package sonac.github.io.instagraph

import scala.concurrent.ExecutionContext.global

import cats.effect.{ConcurrentEffect, Effect, ExitCode, IO, IOApp, Timer, ContextShift}
import cats.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import fs2.Stream

import sonac.github.io.instagraph.services._
import sonac.github.io.instagraph.config.ServerConfig

import org.http4s.server.middleware.Logger

object InstagraphServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      client <- BlazeClientBuilder[F](global).stream
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)
      instaAlg = Instadata.impl[F]

      // Combine Service Routes into an HttpApp
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        InstagraphRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        InstagraphRoutes.jokeRoutes[F](jokeAlg) <+>
        InstagraphRoutes.instaRoutes[F](instaAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger(true, true)(httpApp)


      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}