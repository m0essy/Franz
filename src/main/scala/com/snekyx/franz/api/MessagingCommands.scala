package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.permissions.PermissionError
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

trait MessagingCommands extends CommandParams with MultiChainConnector {

  val SIGN_MESSAGE = "signmessage"
  val VERIFY_MESSAGE = "verifymessage"

  def signMessage(address: String, message: String) = {
    val cmd = MultichainCommand(uuid, SIGN_MESSAGE, Seq(address, message)).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case response: HttpResponse if response.status == StatusCodes.OK =>
        response.entity
      //        Unmarshal(response).to[MultiChainError].map({
      //
      //        }
    }
  }

//  def verifyMessage(address: String, signature: String, message: String) = {
//
//  }
}
