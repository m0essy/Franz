package com.snekyx.franz.api.administration

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.runtime.NodeInfo
import com.snekyx.franz.api.transaction.AssetSent
import com.snekyx.franz.api.{CommandParams, MultiChainConnector, MultiChainException}
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait RuntimeParamCommands extends CommandParams with MultiChainConnector {

  val GET_INFO = "getinfo"
  val GET_BLOCKCHAIN_PARAMS = "getblockchainparams"
  val GET_RUNTIME_PARAMS = "getruntimeparams"
  val SET_RUNTUME_PARAMS = "setruntimeparam"
  val STOP = "stop"

  def getInfo(): Future[NodeInfo] = {
    val cmd = MultichainCommand(uuid, GET_INFO, Seq.empty).asJson.noSpaces

    case class Wrapper(result: NodeInfo)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case info:Wrapper => info.result
        }) recover {
          case err =>
            throw MultiChainException(0, s"${err.getMessage}")
        }
      case resp =>
        Unmarshal(resp).to[ErrorWrapper].map({
          err: ErrorWrapper => throw err.error
        }) recover {
          case err =>
            throw MultiChainException(0, s"$err")
        }
    }
  }

  def stop() = {

  }
}
