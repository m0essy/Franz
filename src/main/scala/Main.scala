import java.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.{Credentials, StreamCommands}
import multichain.`object`.Address
import multichain.command._

import collection.JavaConverters._
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Main extends StreamCommands {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val credentials = Credentials("localhost", 4754, "multichainrpc","6cLax9e9AdmGqKsUHgkqivtPpb4ZCFY7Fd6XTyFxV9H1")
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {

//    createFrom("1MmR7v5DtVxhVHNUdvogFxhPNxqHtiNpVk7n9u", "s2", true).onComplete {
    listStreams().onComplete {
      case Success(res) => println(res)
      case Failure(err) => sys.error("something wrong: " + err)
    }
  }


  //def createCommand = MultiChainCommand("localhost", "4754", "multichainrpc","6cLax9e9AdmGqKsUHgkqivtPpb4ZCFY7Fd6XTyFxV9H1")

}
