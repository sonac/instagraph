package sonac.github.io.instagraph.repository

import cats.effect.IO
import fs2.Stream
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import sonac.github.io.instagraph.model.{User, UserNotFoundError}

class UserRepository(transactor: Transactor[IO]) {

  def getUsers: Stream[IO, User] = {
    sql"select id, followers, photo_link from rave.users".query[User].stream.transact(transactor)
  }

  def getUser(id: Int): IO[Either[UserNotFoundError.type, User]] = {
    sql"select id, followers, photo_link from rave.users where id = $id".query[User]
      .option
      .transact(transactor)
      .map {
        case Some(user) => Right(user)
        case None => Left(UserNotFoundError)
      }
  }

  def createUser(user: User): IO[User] = {
    sql"""insert into rave.users (followers, photo_link) values (
      ${user.followers}, ${user.photoLink})"""
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(transactor)
      .map { id =>
        user.copy(id = id)
      }
  }

  def deleteUser(id: Int): IO[Either[UserNotFoundError.type, Unit]] = {
    sql"delete from rave.users where id = $id".update.run.transact(transactor).map { affectedRows =>
      if (affectedRows == 1) {
        Right()
      } else {
        Left(UserNotFoundError)
      }
    }
  }

  def updateUser(user: User): IO[Either[UserNotFoundError.type, User]] = {
    sql"update users set followers = ${user.followers}, photo_link = ${user.photoLink} where id = ${user.id}"
      .update
      .run
      .transact(transactor)
      .map { affectedRows =>
        if (affectedRows == 1) {
          Right(user)
        } else {
          Left(UserNotFoundError)
        }
      }
  }

}
