package com.snekyx.franz.api.administration

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.snekyx.franz.api.{CommandParams, MultiChainConnector}
import com.snekyx.franz.api.peer2peer.NodeCommand.NodeCommand
import com.snekyx.franz.api.peer2peer._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.Future

trait PeerToPeerCommands extends CommandParams with MultiChainConnector {
  val ADD_NODE = "addnode"

  def addNode(ip: String, command: NodeCommand): Future[Boolean] = {
    val cmd = MultichainCommand(uuid, ADD_NODE, List(ip, nodeCommand2String(command))).asJson.noSpaces

    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK => true
      case _                                                   => false
    }
  }
}
