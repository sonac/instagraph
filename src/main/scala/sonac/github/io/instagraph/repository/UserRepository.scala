package sonac.github.io.instagraph.repository

import cats.effect.{IO, Sync}
import fs2.Stream
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import doobie.postgres.sqlstate
import sonac.github.io.instagraph.model.{User, UserAlreadyExistsError, UserNotFoundError}

class UserRepository[F[_]: Sync](transactor: Transactor[F]) extends InstaRepository[F] {

  implicit val han: LogHandler = LogHandler.jdkLogHandler

  def getUsers: F[Seq[User]] = {
    sql"select username, followers, photo_link from rave.users"
      .query[User]
      .to[Seq]
      .transact(transactor)
  }

  def getUser(id: Int): F[Option[User]] = {
    sql"select username, followers, photo_link from rave.users where id = $id".query[User]
      .option
      .transact(transactor)
  }

  def createUser(user: User): F[Either[UserAlreadyExistsError.type, User]] = {
    sql"""insert into rave.users (username, followers, photo_link) values (
      ${user.username}, ${user.followers}, ${user.photoLink})"""
      .update
      .withUniqueGeneratedKeys[User]("username", "followers", "photo_link")
      .transact(transactor)
      .attemptSomeSqlState {
        case sqlstate.class23.UNIQUE_VIOLATION => UserAlreadyExistsError
      }
  }

  def deleteUser(id: Int): F[Either[UserNotFoundError.type, Unit]] = {
    sql"delete from rave.users where id = $id".update.run.map { affectedRows =>
      if (affectedRows == 1) {
        Right()
      } else {
        Left(UserNotFoundError)
      }
    }.transact(transactor)
  }

  def updateUser(user: User): F[Either[UserNotFoundError.type, User]] = {
    sql"""update users set followers = ${user.followers}, photo_link = ${user.photoLink}
    where username = ${user.username}"""
      .update
      .run
      .map { affectedRows =>
        if (affectedRows == 1) {
          Right(user)
        } else {
          Left(UserNotFoundError)
        }
      }
      .transact(transactor)
  }

}
