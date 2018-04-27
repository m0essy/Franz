package com.snekyx.franz.api

import akka.http.scaladsl.model._
import com.snekyx.franz.api.streams._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.Future


trait StreamCommands extends CommandParams with MultiChainConnector {
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

    sendToMultiChain(x)
  }

  def listStreams(streamName: String = "*", verbose: Boolean = false, count: Int = 10)(implicit credentials: Credentials): Future[HttpEntity] = {
    val x = ListStreams(uuid, LISTSTREAMS, List(streamName, verbose, count)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x) map { item =>
      item.entity
    }
  }

  def create(streamName: String, open: Boolean = false): Future[StreamResponse] = {
    val x = Create(uuid, CREATE, List("stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x) map {
      case HttpResponse(statusCode, headers, entity, _) if statusCode == StatusCodes.OK => CreationResponse()
      case HttpResponse(statusCode, headers, entity, _) => CreationError(statusCode.intValue(), 0, "Error")
    }
  }

  def createFrom(addressFrom: String, streamName: String, open: Boolean = false)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = CreateFrom(uuid, CREATEFROM, List(addressFrom, "stream", streamName, open)).asJson.noSpaces

    sendToMultiChain(x)
  }

  def subscribe(streamName: String) = {
    val x = Subscribe(uuid, SUBSCRIBE, List(streamName)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x)
  }

  def unsubscribe() = {}

  def publish() = {}

  def publishFrom() = {}
}

