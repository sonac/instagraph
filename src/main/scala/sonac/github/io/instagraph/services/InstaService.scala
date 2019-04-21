package sonac.github.io.instagraph.services

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import sonac.github.io.instagraph.model.User
import sonac.github.io.instagraph.repository.InstaRepository

class InstaService(repository: InstaRepository[IO]) extends Http4sDsl[IO] {
  import org.http4s.implicits._

  implicit val decoder: EntityDecoder[IO, User] = jsonOf[IO, User]

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "api" / "users" =>
      Ok(repository.getUsers.map(_.asJson.noSpaces))
    case GET -> Root / "api" / "user" / userId =>
      Ok(repository.getUser(userId.toInt).map(_.asJson.noSpaces))
    case req @ POST -> Root / "api" / "add-user" =>
      for {
        user <- req.as[User]
        resp <- Ok(repository.createUser(user).map(_.asJson.noSpaces))
      } yield resp
  }.orNotFound

}
