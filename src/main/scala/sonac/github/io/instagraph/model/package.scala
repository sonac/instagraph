package sonac.github.io.instagraph

import cats.Applicative
import io.circe.{Encoder, HCursor, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

package object model {
  case class User(username: String, followers: Int, photoLink: String)
  case class UserFollowers(userId: Int, followerId: Int)
  case object UserNotFoundError
}
