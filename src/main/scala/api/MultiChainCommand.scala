package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait StreamCommands extends CommandUtils with MultichainConnector {
  val LISTSTREAMITEMS: String = "LISTSTREAMITEMS".toLowerCase

  def listStreams(streamName: String, verbose: Boolean, count: Int, start: Int)(implicit credentials: Credentials) = {
    val x = ListStreams(generateId, LISTSTREAMITEMS, List(streamName, verbose, count, start)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    val responseFuture: Future[HttpResponse] = sendToMultiChain(x, multichainUri)

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(err)   => sys.error("something wrong: " + err)
      }
  }

  def listStreams(streamName: String, verbose: Boolean, count: Int)(implicit credentials: Credentials) = {
    val x = ListStreams(generateId, LISTSTREAMITEMS, List(streamName, verbose, count)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    val responseFuture: Future[HttpResponse] = sendToMultiChain(x, multichainUri)

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(err)   => sys.error("something wrong: " + err)
      }
  }

  def createFrom(addressFrom: String, streamName: String, open: Boolean)(implicit credentials: Credentials) = {
    val x = CreateFrom(generateId, "createfrom", List(addressFrom, "stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    val responseFuture: Future[HttpResponse] = sendToMultiChain(x, multichainUri)

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(err)   => sys.error("something wrong: " + err)
      }
  }

  private def sendToMultiChain(x: String, uri: Uri) = {
    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(HttpMethods.POST, uri, List(auth), x))
    responseFuture
  }
}

case class GetWalletTransaction(implicit cmd: multichain.command.MultiChainCommand) {
  cmd.getWalletTransactionCommand
}