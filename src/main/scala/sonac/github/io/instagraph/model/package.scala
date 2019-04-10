package sonac.github.io.instagraph

package object model {
  case class User(id: Int, followers: Int, photoLink: String)
  case class UserFollowers(userId: Int, followerId: Int)
  case object UserNotFoundError
}
