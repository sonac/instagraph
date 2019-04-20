package sonac.github.io.instagraph.repository

import cats.effect.IO
import fs2.Stream
import sonac.github.io.instagraph.model.User

trait InstaRepository[F[_]] {
  def getUsers: F[Seq[User]]
}