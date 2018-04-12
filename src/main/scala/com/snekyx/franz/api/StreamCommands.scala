package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.streams._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait StreamCommands extends MultiChainCommands with MultiChainConnector {
  val CREATE: String = "create"
  val CREATEFROM: String = "createfrom"
  val LISTSTREAMS: String = "liststreams"
  val LISTSTREAMITEMS: String = "LISTSTREAMITEMS".toLowerCase
  val PUBLISH: String = "publish"
  val PUBLISHFROM = "publishfrom"
  val SUBSCRIBE = "subscribe"
  val UNSUBSCRIBE = "unsubscribe"

  def listStreams(streamName: String, verbose: Boolean, count: Int, start: Int)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = ListStreams(uuid, LISTSTREAMS, List(streamName, verbose, count, start)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x, multiChainUri)
  }

  def listStreams(streamName: String = "*", verbose: Boolean = false, count: Int = 10)(implicit credentials: Credentials): Future[HttpEntity] = {
    val x = ListStreams(uuid, LISTSTREAMS, List(streamName, verbose, count)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x, multiChainUri) map { item =>
      item.entity
    }
  }

  def create(streamName: String, open: Boolean = false): Future[StreamResponse] = {
    val x = Create(uuid, CREATE, List("stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x, multiChainUri) map {
      case HttpResponse(statusCode, headers, entity, _) if statusCode == StatusCodes.OK => CreationResponse()
      case HttpResponse(statusCode, headers, entity, _) => CreationError(statusCode.intValue(), 0, "Error")
    }
  }

  def createFrom(addressFrom: String, streamName: String, open: Boolean = false)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = CreateFrom(uuid, CREATEFROM, List(addressFrom, "stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x, multiChainUri)
  }

  def subscribe(streamName: String) = {
    val x = Subscribe(uuid, SUBSCRIBE, List(streamName)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x, multiChainUri)
  }

  def unsubscribe() = {}

  def publish() = {}

  def publishFrom() = {}

  private def sendToMultiChain(x: String, uri: Uri) = {
    println("XXXXXXXXXXXXXXXXxx " + uri)
    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(HttpMethods.POST, uri, List(auth), x))
    responseFuture
  }
}

