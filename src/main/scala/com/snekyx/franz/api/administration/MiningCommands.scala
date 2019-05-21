package com.snekyx.franz.api.administration

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.snekyx.franz.api.mining.{Paused, Resumed}
import com.snekyx.franz.api.{CommandParams, MultiChainConnector, Param}
import io.circe.generic.auto._
import io.circe.syntax._

trait MiningCommands extends CommandParams with MultiChainConnector  {

  val PAUSE = "pause"
  val RESUME = "resume"

  case class Cmd(id: String, method: String, params: Seq[Param])

  def pauseMining() = {
    val cmd = Cmd(uuid, PAUSE, Seq("mining")).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        println("XXXXXXXXXX")
        println(resp)
        Paused()
    }
  }

  def resumeMining() = {
    val cmd = Cmd(uuid, RESUME, Seq("mining")).asJson.noSpaces
    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        println("XXXXXXXXXX")
        println(resp)
        Resumed()
    }
  }
}
