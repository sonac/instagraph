package sonac.github.io.instagraph

import cats.Applicative
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.syntax._
import org.http4s.dsl.io._
import org.http4s.server.blaze._
import fs2.Stream
import config.Config
import sonac.github.io.instagraph.db.Database
import sonac.github.io.instagraph.repository.UserRepository
import sonac.github.io.instagraph.services.InstaService

object InstagraphServer extends IOApp {

  def stream: Stream[IO, ExitCode] = {


    for {
      config <- Stream.eval(Config.load())
      transactor <- Stream.eval(Database.transactor(config.database))
      _ <- Stream.eval(Database.initialize(transactor))
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(new InstaService(new UserRepository(transactor)).service)
        .serve
    } yield exitCode


  }

  def run(args: List[String]): IO[ExitCode] = {
    stream.compile.drain.as(ExitCode.Success)
  }
}