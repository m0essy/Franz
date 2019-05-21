package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.streams.StreamError
import com.snekyx.franz.api.transaction._
import com.snekyx.franz.api.wallet.AddressBalance
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

    case class Wrapper(result: AssetSent)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => a.result
          case err => TransactionError(0, s"getaddresses returnd an unknown error $err")

        }) recover {
          case err => TransactionError(0, s"$err")
        }
      case resp => Future.successful(TransactionError(0, resp.entity.toString))
    }
  }

  def sendAsset(address: String, asset: String, qty: Double): Future[TransactionResponse] = {
    val cmd = TransactionCommand(uuid, SEND_ASSET, List(address, asset, qty)).asJson.noSpaces

    case class Wrapper(result: String)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => AssetSent(a.result)
          case err => TransactionError(0, s"getaddresses returnd an unknown error $err")
        }) recover {
          case err => TransactionError(0, s"$err")
        }
      case resp => Future.successful(TransactionError(0, resp.entity.toString))
    }
  }

  def sendAssetFrom(fromAddress: String, toAddress: String, asset: String, qty: Double): Future[TransactionResponse] = {
    val cmd = TransactionCommand(uuid, SEND_ASSET_FROM, List(fromAddress, toAddress, asset, qty)).asJson.noSpaces

    case class Wrapper(result: String)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => AssetSent(a.result)
          case err => TransactionError(0, s"getaddresses returnd an unknown error $err")
        }) recover {
          case err => TransactionError(0, s"$err")
        }
      case resp => Future.successful(TransactionError(0, resp.entity.toString))
    }
  }
}
