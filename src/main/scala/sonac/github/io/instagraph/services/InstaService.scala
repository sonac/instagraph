package sonac.github.io.instagraph.services

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.client.Client
import sonac.github.io.instagraph.model.{User, UserError}
import sonac.github.io.instagraph.repository.InstaRepository

class InstaService(repository: InstaRepository[IO], client: Client[IO]) extends Http4sDsl[IO] {
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
        res <- repository.createUser(user)
        resp <- userResult(res)
      } yield resp
    case GET -> Root / "api" / "parse" =>
      Ok(
        new Instaparse(client).parseFollowers("andreysumko")
      )
  }.orNotFound

  private def userResult(res: Either[UserError, User]) = {
    res match {
      case Left(err) => BadRequest(err.error)
      case Right(user) => Ok(user.asJson)
    }
  }

}
