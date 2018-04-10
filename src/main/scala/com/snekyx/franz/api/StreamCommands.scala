package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.streams.{Create, CreateFrom, ListStreams}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait StreamCommands extends CommandUtils with MultichainConnector {
  val CREATE: String = "create"
  val CREATEFROM: String = "createfrom"
  val LISTSTREAMS: String = "liststreams"
  val LISTSTREAMITEMS: String = "LISTSTREAMITEMS".toLowerCase

  def listStreams(streamName: String, verbose: Boolean, count: Int, start: Int)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = ListStreams(generateId, LISTSTREAMS, List(streamName, verbose, count, start)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    sendToMultiChain(x, multichainUri)
  }

  def listStreams(streamName: String = "*", verbose: Boolean = false, count: Int = 10)(implicit credentials: Credentials): Future[HttpEntity] = {
    val x = ListStreams(generateId, LISTSTREAMS, List(streamName, verbose, count)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    sendToMultiChain(x, multichainUri) map { item =>
      item.entity
    }
  }

  def create(streamName: String, open: Boolean = false)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = Create(generateId, CREATE, List("stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    sendToMultiChain(x, multichainUri)
  }

  def createFrom(addressFrom: String, streamName: String, open: Boolean = false)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = CreateFrom(generateId, CREATEFROM, List(addressFrom, "stream", streamName, open)).asJson.noSpaces
    println("payload: " + x)
    println("sending to: " + multichainUri)

    sendToMultiChain(x, multichainUri)
  }

  def subscribe() = {}

  def publish() = {}

  private def sendToMultiChain(x: String, uri: Uri) = {
    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(HttpMethods.POST, uri, List(auth), x))
    responseFuture
  }
}

case class GetWalletTransaction(implicit cmd: multichain.command.MultiChainCommand) {
  cmd.getWalletTransactionCommand
}