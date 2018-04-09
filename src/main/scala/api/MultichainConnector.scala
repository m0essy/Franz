package api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{Uri, headers}
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait MultichainConnector {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val credentials: Credentials
  implicit val ec: ExecutionContext

  val multichainUri = Uri("http://" + credentials.ip + ":" + credentials.port)

  val auth = headers.Authorization(BasicHttpCredentials(credentials.login, credentials.password))

}
