package sonac.github.io.instagraph

import cats.effect.Sync
import cats.implicits._
import io.circe.Json
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import sonac.github.io.instagraph.services._

object InstagraphRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }
  
  def instaRoutes[F[_]: Sync](I: Instadata[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "user" / username =>
        for {
          user <- I.user(username)
          resp <- Ok(user)
        } yield resp
      case GET -> Root / "followers" / username =>
        for {
          users <- I.getFollowers(username)
          resp <- Ok(users)
        } yield resp
    }
  }

}