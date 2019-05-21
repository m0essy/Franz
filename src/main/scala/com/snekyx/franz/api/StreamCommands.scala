package com.snekyx.franz.api

import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.streams._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.Future
import com.snekyx.franz.utils.HexStringUtil.string2hex
import com.snekyx.franz.utils.CirceSupport._
import com.snekyx.franz.utils.HexStringUtil

trait StreamCommands extends CommandParams with MultiChainConnector {
  val CREATE: String = "create"
  val CREATEFROM: String = "createfrom"
  val LISTSTREAMS: String = "liststreams"
  val LISTSTREAMITEMS: String = "liststreamitems"
  val PUBLISH: String = "publish"
  val PUBLISHFROM = "publishfrom"
  val SUBSCRIBE = "subscribe"
  val UNSUBSCRIBE = "unsubscribe"

  case class Create(id: String, method: String, params: Seq[Param])

  case class CreateFrom(id: String, method: String, params: Seq[Param])

  case class ListStreams(id: String, method: String, params: Seq[Param])

  case class ListStreamsResult(name: String, createtxid: String, streamref: String, open: Boolean, /*details, */ subscribed: Boolean, synchronized: Boolean, items: Int, confirmed: Int, keys: Int, publishers: Int)

  case class Subscribe(id: String, method: String, params: Seq[Param])

  case class Unsubscribe(id: String, method: String, params: Seq[Param])

  case class Publish(id: String, method: String, params: Seq[Param])

  case class PublishFrom(id: String, method: String, params: Seq[Param])

  case class ListStreamItems(id: String, method: String, params: Seq[Param])

  def listStreams(streamName: String, verbose: Boolean, count: Int, start: Int)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = ListStreams(uuid, LISTSTREAMS, List(streamName, verbose, count, start)).asJson.noSpaces
    println("payload: " + x)

    sendToMultiChain(x)
  }

  def listStreams(streamName: String = "*", verbose: Boolean = false, count: Int = 10): Future[Seq[StreamResponse]] = {
    val x = ListStreams(uuid, LISTSTREAMS, List(streamName, verbose, count)).asJson.noSpaces
    case class Wrapper(result: List[StreamDetails])

    sendToMultiChain(x) flatMap { item =>
      Unmarshal(item).to[Wrapper] map (_.result) recover {
        case err => Seq(StreamError(0, s"$err"))
      }
    }
  }

  def create(streamName: String, open: Boolean = false): Future[StreamResponse] = {
    val x = Create(uuid, CREATE, List("stream", streamName, open)).asJson.noSpaces
    sendToMultiChain(x) map {
      case HttpResponse(statusCode, _, entity, _) if statusCode == StatusCodes.OK => CreationResponse()
      case HttpResponse(statusCode, _, entity, _) => CreationError(statusCode.intValue(), 0, "Error" + entity)
    }
  }

  def createFrom(addressFrom: String, streamName: String, open: Boolean = false)(implicit credentials: Credentials): Future[HttpResponse] = {
    val x = CreateFrom(uuid, CREATEFROM, List(addressFrom, "stream", streamName, open)).asJson.noSpaces

    sendToMultiChain(x)
  }

  def subscribe(streamName: String): Future[StreamResponse] = {
    val x = Subscribe(uuid, SUBSCRIBE, List(streamName)).asJson.noSpaces
    sendToMultiChain(x) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => Subscribed(streamName)
      case HttpResponse(statusCode, _, entity, _) => StreamError(statusCode.intValue(), entity.toString)
    }
  }

  def unsubscribe(streamName: String): Future[StreamResponse] = {
    val x = Unsubscribe(uuid, UNSUBSCRIBE, List(streamName)).asJson.noSpaces
    sendToMultiChain(x) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => Subscribed(streamName)
      case HttpResponse(statusCode, _, entity, _) => StreamError(statusCode.intValue(), entity.toString)
    }
  }

  // todo implement offChain
  def publish(streamName: String, key: String, data: String, offChain: Boolean = false): Future[StreamResponse] = {
    val x = Publish(uuid, PUBLISH, List(streamName, key, string2hex(data))).asJson.noSpaces
    sendToMultiChain(x) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => Published(streamName)
      case HttpResponse(statusCode, _, entity, _) => StreamError(statusCode.intValue(), entity.toString)
    }
  }

  // todo add confirmed Optional boolean field
  def publishFrom(streamName: String, fromAddress: String): Future[StreamResponse] = {
    val x = PublishFrom(uuid, PUBLISHFROM, List(streamName)).asJson.noSpaces
    sendToMultiChain(x) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => Published(streamName)
      case HttpResponse(statusCode, _, entity, _) => StreamError(statusCode.intValue(), entity.toString)
    }
  }

  def listStreamItems(streamName: String, count: Int = 10, start: Int = -1): Future[Seq[StreamResponse]] = {
    val verbose = false
    val localOrdering = false
    val cmd = ListStreamItems(uuid, LISTSTREAMITEMS, List(streamName, verbose, count, start, localOrdering)).asJson.noSpaces

    case class Wrapper(result: Seq[StreamItem])

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map(w => {
          w.result map { item =>
            StreamItem(item.publishers,
              item.keys,
              item.offchain,
              HexStringUtil.hex2string(item.data),
              item.confirmations,
              item.txid)
          }
        }) recover {
          case err => Seq(StreamError(0, s"$err"))
        }
      case resp =>
        Future.successful(Seq(StreamError(0, s"$resp")))
    }
  }
}

