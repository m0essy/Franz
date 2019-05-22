package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait MultisignatureCommands extends CommandParams with MultiChainConnector {
  val ADD_MULTISIG_ADDRESS = "addmultisigaddress"

  def addMultisigAddress(nRequired: Int, addresses: List[String]): Future[String] = {
    val keys = addresses.asJson.noSpaces
    val cmd = MultichainCommand(uuid, ADD_MULTISIG_ADDRESS, List(nRequired, keys)).asJson.noSpaces
      .replace("\"[\\\"" , "[\"")
      .replace("\\\",\\\"" , "\",\"")
      .replace("\\\"]\"" , "\"]")

    case class Wrapper(result: String)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case w: Wrapper => w.result
          case w => throw MultiChainException(0, w.toString)
        })
      case resp: HttpResponse => throw MultiChainException(0, resp.entity.toString)
    }
  }
}
