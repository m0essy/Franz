package com.snekyx.franz.examples

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api
import com.snekyx.franz.api.{Credentials, MultiChainCommands}

import scala.concurrent.ExecutionContext

object MultisigNode2 extends MultiChainCommands {

  private val host = "localhost"
  private val rpcPort = 7834
  private val rpcUser = "multichainrpc"
  private val rpcPassword = "YADFBofteUyuafUxom5LxY4L6PPEjd5NAoENmfPRbeH"

  override implicit val system: ActorSystem = ActorSystem("BasicStreamExample")
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val credentials: api.Credentials = Credentials(host, rpcPort, rpcUser, rpcPassword)
  override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
  }
}
