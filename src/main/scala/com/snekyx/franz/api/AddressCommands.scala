package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.addresses.{AddressCommandError, AddressResponse, GetNewAddress, NewAddressResponse}
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait AddressCommands extends CommandParams with MultiChainConnector{
  val GETNEWADDRESS = "getnewaddress"

  def getNewAddress(): Future[AddressResponse] = {
    val json = GetNewAddress(uuid, GETNEWADDRESS).asJson.noSpaces

    sendToMultiChain(json) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        println("XXXXXXXXXX111" + resp)
        Unmarshal(resp).to[NewAddressResponse].map({
          case result: NewAddressResponse =>
            result
          case err                        => AddressCommandError(0, 0, s"getNewAddress encountered unknown error: $err")
        }) recover {
          case err => AddressCommandError(0, 0, s"getNewAddress map to NewAddressResponse encountered unknown error: $err")
        }
      case HttpResponse(statusCode, _, entity, _)        => Future.successful(AddressCommandError(statusCode.intValue(), 0, s"getNewAddress received: $entity"))
    }
  }
}
