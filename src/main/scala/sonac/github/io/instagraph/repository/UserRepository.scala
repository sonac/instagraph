package sonac.github.io.instagraph.repository

import cats.effect.{IO, Sync}
import fs2.Stream
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import sonac.github.io.instagraph.model.{User, UserNotFoundError}

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

  def createUser(user: User): F[User] = {
    sql"""insert into rave.users (followers, photo_link) values (
      ${user.followers}, ${user.photoLink})"""
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .map { id =>
        user.copy(id = id)
      }
      .transact(transactor)
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
    sql"update users set followers = ${user.followers}, photo_link = ${user.photoLink} where id = ${user.id}"
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
