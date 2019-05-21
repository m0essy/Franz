package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.addresses.{Address, NewAddressResponse}
import com.snekyx.franz.api.assets.{AssetInfo, Issued, Restrictions}
import com.snekyx.franz.api.permissions.Permission
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class AssetCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "assetTestsBlockChain"

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

  val assetName = "asset1"

  "Multichain" should {
    "issue a new asset" in {
      val address = Await.result(multiChainCommands.getAddressList(), 2 seconds).head
      val result = Await.result(multiChainCommands.issue(address, assetName, 100), 2 seconds)

      result mustEqual Issued(assetName)
    }

    "issue a new asset from a specific addres" in {
      val fromAddress = Await.result(multiChainCommands.getNewAddress(), 2 seconds) match {
        case a: NewAddressResponse => a
      }
      val toAddress = Await.result(multiChainCommands.getNewAddress(), 2 seconds) match {
        case a: NewAddressResponse => a
      }

      Await.result(multiChainCommands.grant(fromAddress.result, Seq(Permission.Issue)), 2 seconds)
      Await.result(multiChainCommands.grant(toAddress.result, Seq(Permission.Receive)), 2 seconds)

      Await.result(multiChainCommands.issueFrom(fromAddress.result, toAddress.result, "Test123", 10), 2 seconds)

      success

    }

    "retrieve asset information" in {
      val result = Await.result(multiChainCommands.getAssetInfo(assetName), 2 seconds)

      val assetName1 = "asdf"

      result match {
        case AssetInfo(`assetName`, _,_,_,_,_, 100,100,_) => success
        case _                                            => failure
      }
    }
  }
}
