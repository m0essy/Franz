package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.addresses.NewAddressResponse
import com.snekyx.franz.api.permissions.Permission._
import com.snekyx.franz.api.permissions.PermissionsGranted
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class PermissionCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {
  sequential

  override val multiChainName = "permissionsTestsBlockChain"

  override def beforeAll(): Unit = {
    deleteBlockChain()
    createBlockChain()
    startBlockChainDaemon

    Thread.sleep(3000)
  }

  override def afterAll(): Unit = {
    stopBlockChain()
    Thread.sleep(3000)
    deleteBlockChain()
  }

  "Multichain" should {
    "grant permissions to address" in {
      Await.result(multiChainCmds.getNewAddress(), 2 seconds) match {
        case NewAddressResponse(address) =>
          Await.result(multiChainCmds.grant(address, Seq(Connect, Receive, Send, Issue, Mine, Activate, Admin)), 2 seconds) match {
            case granted: PermissionsGranted => success
            case other => failure(s"unexpected result. expected PermissionGranted. $other")
          }
        case err                         => failure(s"expected new address. Instead got: $err")
      }
    }
  }

  private def multiChainCmds = {
    new PermissionCommands with AddressCommands {
      override implicit val system: ActorSystem = ActorSystem()
      override implicit val materializer: ActorMaterializer = ActorMaterializer()
      override implicit val credentials: Credentials = Credentials("localhost", multiChainPort, multiChainUser, multiChainPassword)
      override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    }
  }
}
