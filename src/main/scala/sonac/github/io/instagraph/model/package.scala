package sonac.github.io.instagraph

import cats.Applicative
import io.circe.{Encoder, HCursor, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

package object model {
  case class User(username: String, followers: Int, photoLink: String)
  case class UserFollowers(userId: Int, followerId: Int)
  sealed trait UserError {
    def error: String
  }
  case object UserNotFoundError extends UserError {
    def error = "User not found"
  }
  case object UserAlreadyExistsError extends UserError {
    def error = "User already exists"
  }
}
