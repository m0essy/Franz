package com.snekyx.franz.examples

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api
import com.snekyx.franz.api.addresses.NewAddressResponse
import com.snekyx.franz.api.permissions.Permission
import com.snekyx.franz.api.{Credentials, MultiChainCommands}
import com.snekyx.franz.examples.BasicStreamExample.{create, subscribe}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object BasicAssetExample extends MultiChainCommands {

  private val host = "localhost"
  private val rpcPort = 6834
  private val rpcUser = "multichainrpc"
  private val rpcPassword = "Ghmt3dmWv6TfFz3vVnDYhnyH2ZpLEdkrsUPJ5xYcHwAK"

  override implicit val system: ActorSystem = ActorSystem("BasicStreamExample")
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val credentials: api.Credentials = Credentials(host, rpcPort, rpcUser, rpcPassword)
  override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val timeout = 1 minutes

  def main(args: Array[String]): Unit = {

    // create new addresses
    val fromAddress = Await.result(getNewAddress(), 2 seconds) match {
      case a: NewAddressResponse => a.result
    }
    val toAddress1 = Await.result(getNewAddress(), 2 seconds) match {
      case a: NewAddressResponse => a.result
    }
    val toAddress2 = Await.result(getNewAddress(), 2 seconds) match {
      case a: NewAddressResponse => a.result
    }

    // set permissions
    Await.result(grant(fromAddress, List(Permission.Issue)), 2 seconds)
    Await.result(grant(toAddress1, List(Permission.Receive, Permission.Send)), 2 seconds)
    Await.result(grant(toAddress2, List(Permission.Receive)), 2 seconds)

    // issue asset
    Await.result(issueFrom(fromAddress, toAddress1, "chain21", 100, 0.01, 100, true), 2 seconds)

    // send from toAddress1 to toAddress2
    Await.result(sendAssetFrom(toAddress1, toAddress2, "chain21", 1), 2 seconds)
  }
}