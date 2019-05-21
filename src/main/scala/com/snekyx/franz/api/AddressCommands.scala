package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.addresses.{Address, AddressCommandError, AddressResponse, NewAddressResponse}
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait AddressCommands extends CommandParams with MultiChainConnector {
  val GETNEWADDRESS = "getnewaddress"
  val GETADDRESSES = "getaddresses"

  case class GetNewAddress(id: String, method: String)

  case class GetAddresses(id: String, method: String, params: Seq[Param])

  /**
    * Returns a new address whose private key is added to the wallet.
    *
    * @return
    */
  def getNewAddress(): Future[AddressResponse] = {
    val json = GetNewAddress(uuid, GETNEWADDRESS).asJson.noSpaces

    sendToMultiChain(json) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[NewAddressResponse].map({
          case result: NewAddressResponse =>
            result
          case err => AddressCommandError(0, 0, s"getNewAddress encountered unknown error: $err")
        }) recover {
          case err => AddressCommandError(0, 0, s"$err")
        }
      case HttpResponse(statusCode, _, entity, _) => Future.successful(AddressCommandError(statusCode.intValue(), 0, s"getNewAddress received: $entity"))
    }
  }

  /**
    * Returns a list of addresses in this node’s wallet. Set verbose to true to get more information about
    * each address, formatted like the output of the validateaddress command. For more control see the new
    * listaddresses command.
    *
    * @return
    */
  def getAddresses(): Future[Seq[AddressResponse]] = {
    val verbose = true
    val json = GetAddresses(uuid, GETADDRESSES, Seq(verbose)).asJson.noSpaces

    case class Wrapper(result: Seq[Address])

    sendToMultiChain(json) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => a.result
          case err => Seq(AddressCommandError(0,0, s"getaddresses returnd an unknown error $err"))
        }) recover {
          case err => Seq(AddressCommandError(0, 0, s"$err"))
        }
    }
  }

  def getAddressList(): Future[Seq[String]] = {
    val verbose = false
    val json = GetAddresses(uuid, GETADDRESSES, Seq(verbose)).asJson.noSpaces

    case class Wrapper(result: List[String])

    sendToMultiChain(json) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        println(resp)
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => a.result
          case err => throw new Exception(s"getaddresses returnd an unknown error $err")
        })
    }
  }
}