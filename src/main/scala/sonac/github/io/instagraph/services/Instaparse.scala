package sonac.github.io.instagraph.services

import scala.util.Properties.envOrElse
import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.client.blaze.BlazeClientBuilder
import cats.implicits._
import org.http4s._
import fs2.Stream
import org.http4s.client._
import io.circe._
import io.circe.literal._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io._
import sonac.github.io.instagraph.model.User

class Instaparse[F[_]](client: Client[F]) {

  def parseFollowers(username: String): Stream[F, Byte] = {
    val username = envOrElse("IG_USERNAME", "username")
    val password = envOrElse("IG_PASSWORD", "password")
    val req = POST(json"""{"username": $username, "password": $password}""",
      Uri.uri("http://localhost:5000/api/parse-followers/andreysumko"))
    client.stream(req).flatMap(_.body)
  }

}
