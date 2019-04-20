package sonac.github.io.instagraph.services

import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, HttpService, MediaType, Uri}
import org.http4s.headers.{Location, `Content-Type`}
import io.circe.generic.auto._
import io.circe.syntax._
import fs2.Stream
import sonac.github.io.instagraph.repository.InstaRepository

class InstaService(repository: InstaRepository[IO]) extends Http4sDsl[IO] {
  import org.http4s.implicits._

  val service = HttpRoutes.of[IO] {
    case GET -> Root / "api" =>
      Ok(repository.getUsers.map(_.asJson.noSpaces))
  }.orNotFound

}
