package sonac.github.io.instagraph.repository

import cats.effect.IO
import fs2.Stream
import sonac.github.io.instagraph.model.{User, UserAlreadyExistsError, UserNotFoundError}

trait InstaRepository[F[_]] {
  def getUsers: F[Seq[User]]
  def getUser(id: Int): F[Option[User]]
  def createUser(user: User): F[Either[UserAlreadyExistsError.type, User]]
  def deleteUser(id: Int): F[Either[UserNotFoundError.type, Unit]]
  def updateUser(user: User): F[Either[UserNotFoundError.type, User]]
}