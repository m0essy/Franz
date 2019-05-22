package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.permissions._
import com.snekyx.franz.api.permissions.Permission.Permission
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future
// todo: implement entity.issue and entity.write permission
trait PermissionCommands extends CommandParams with MultiChainConnector {
  val GRANT = "grant"

  def grant(address: String, permissions: Seq[Permission]): Future[PermissionResponse] = {
    val json = MultichainCommand(uuid, GRANT, List(address, permissions2String(permissions))).asJson.noSpaces
    //val json = MultichainCommand(uuid, GRANT, List(address, "asdfasdfasdfasdfasd")).asJson.noSpaces

    sendToMultiChain(json) flatMap {
      case response: HttpResponse if response.status == StatusCodes.OK =>
        Future.successful(PermissionsGranted(address))
      case response: HttpResponse =>
        Unmarshal(response).to[PermissionError].map({
          case result: PermissionError =>
            result
          case err => PermissionError("-1", MultiChainError(0, s"grant encountered unknown error: $err"))
      }) recover {
        case err => PermissionError("-1", MultiChainError(0, s"grant map to PermissionsGranted encountered unknown error: $err"))
      }
    }
  }
}
