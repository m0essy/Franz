package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.streams.StreamError
import com.snekyx.franz.api.transaction._
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait TransactionCommands extends CommandParams with MultiChainConnector {
  val SEND = "SEND"
  val SEND_ASSET = "sendasset"
  val SEND_ASSET_FROM = "sendassetfrom"

  case class TransactionCommand(id: String, method: String, params: Seq[Param])

  def send(address: String, amount: Double): Future[TransactionResponse] = {
    val cmd = TransactionCommand(uuid, SEND, List(address, amount)).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => SuccessResponse(resp.entity.toString)
      case resp                                                => TransactionError(0, resp.entity.toString)
    }
  }

  def sendAsset(address: String, asset: String, qty: Double): Future[TransactionResponse] = {
    val cmd = TransactionCommand(uuid, SEND_ASSET, List(address, asset, qty)).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => SuccessResponse(resp.entity.toString)
      case resp                                                => TransactionError(0, resp.entity.toString)
    }
  }

  def sendAssetFrom(fromAddress: String, toAddress: String, asset: String, qty: Double): Future[TransactionResponse] = {
    val cmd = TransactionCommand(uuid, SEND_ASSET, List(fromAddress, toAddress, asset, qty)).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => SuccessResponse(resp.entity.toString)
      case resp                                                => TransactionError(0, resp.entity.toString)
    }
  }
}
