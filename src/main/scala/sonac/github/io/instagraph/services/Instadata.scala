package sonac.github.io.instagraph.services

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.{Encoder, Decoder, Json, HCursor}
import io.circe.generic.semiauto._
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._

trait Instadata[F[_]]{
	def user(username: String): F[Instadata.User]
}

object Instadata {
	def apply[F[_]](implicit ev: Instadata[F]): Instadata[F] = ev

	case class User(username: String, followersCount: Int = 0)
	object User {
    implicit val userEncoder: Encoder[User] = new Encoder[User] {
      final def apply(u: User): Json = Json.obj(
        ("message", Json.fromString(u.username + " " + u.followersCount)),
      )
    }
    implicit def userEntityDecoder[F[_]: Applicative]: EntityEncoder[F, User] =
      jsonEncoderOf[F, User]
  }
  
  final case class UserError(e: Throwable) extends RuntimeException

  def impl[F[_]: Applicative]: Instadata[F] = new Instadata[F]{
    def user(username: String): F[Instadata.User] =
      User(username).pure[F]
  }

}