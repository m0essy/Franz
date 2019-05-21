package com.snekyx.franz.examples

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.snekyx.franz.api
import com.snekyx.franz.api.{Credentials, MultiChainCommands}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object StreamConfidentialityExample extends MultiChainCommands {
  val timeout = 1 minutes

  private val host = "localhost"
  private val rpcPort = 6834
  private val rpcUser = "multichainrpc"
  private val rpcPassword = "Ghmt3dmWv6TfFz3vVnDYhnyH2ZpLEdkrsUPJ5xYcHwAK"

  override implicit val system: ActorSystem = ActorSystem("BasicStreamExample")
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val credentials: api.Credentials = Credentials(host, rpcPort, rpcUser, rpcPassword)
  override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {

    create("pubkeys", true)
    create("items", true)
    create("access", true)

    val address = getNewAddress()

    val streams = Await.result(listStreams(), 2 seconds)
    println(streams)

    Await.result(system.whenTerminated, timeout)
  }
}