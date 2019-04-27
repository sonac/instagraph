package sonac.github.io.instagraph.services

import scala.util.Properties.envOrElse
import org.http4s.Method.POST
import fs2.Stream
import io.circe._
import io.circe.literal._
import io.circe.syntax._
import org.http4s.client._
import org.http4s.client.dsl.io._
import org.http4s.circe._
import org.http4s._
import org.http4s.headers._
import io.circe.generic.auto._
import org.typelevel.jawn
import cats.effect.IO

class Instaparse[F[_]](client: Client[IO]) {

  case class Reqtal(username: String, password: String)

  implicit val decoder = jsonOf[IO, Reqtal]

  def parseFollowers(username: String): IO[String] = {
    val username = envOrElse("IG_USERNAME", "username")
    val password = envOrElse("IG_PASSWORD", "password")
    val req = POST(
      Reqtal(username, password).asJson,
      Uri.uri("http://localhost:5000/api/parse-followers/andreysumko")
      ).map(_.withHeaders(`Content-Type`(MediaType.application.json)))
    client.expect(req)(jsonOf[IO, String])
  }

}
