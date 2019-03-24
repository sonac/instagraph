package sonac.github.io.instagraph.services

import cats.Applicative
import cats.effect._
import cats.implicits._
import io.circe.{Encoder, Decoder, Json, HCursor}
import io.circe.generic.semiauto._
import io.circe.parser.parse
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._

trait Instadata[F[_]]{
  def user(username: String): F[Instadata.User]
  def getFollowers(username: String): F[Seq[Instadata.User]]
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
    implicit def userEntityEncoder[F[_]: Applicative]: EntityEncoder[F, User] =
      jsonEncoderOf[F, User]
    implicit def usersEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Seq[User]] =
      jsonEncoderOf[F, Seq[User]]
  }

  def jsonParse(json: Json, username: String): Seq[User] = {
    val cursor: HCursor = json.hcursor
    if (cursor.keys.get.toList.contains(username)) {
      val users: List[scala.util.Either[io.circe.DecodingFailure, User]] = cursor.downField(username)
        .keys.get.toList.map{u => 
          cursor.downField(username).get[Int](u).map(f => User(u, f))
        }
      users.map {
        case Right(value) => value
        case other => throw new Exception(s"Unexpected Left: $other")
      }
    } else Nil
  }
  
  final case class UserError(e: Throwable) extends RuntimeException

  def impl[F[_]: Applicative]: Instadata[F] = new Instadata[F]{
    def user(username: String): F[Instadata.User] =
      User(username).pure[F]

    def getFollowers(username: String): F[Seq[Instadata.User]] = {
      val jsonFile = scala.io.Source.fromFile("/home/sonac/git/instagraph/src/main/resources/data.json").getLines.mkString
      val json = parse(jsonFile).getOrElse(Json.Null)
      jsonParse(json, username).pure[F]
    }
  }

}