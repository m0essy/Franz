package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.assets.Issued
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
      val address = Await.result(multiChainAddresses.getAddressList(), 2 seconds).head
      val result = Await.result(multiChainAssets.issue(address, assetName, 100), 2 seconds)

      result mustEqual Issued(assetName)
    }

    "retrieve asset information" in {
      val result = Await.result(multiChainAssets.getAssetInfo(assetName), 2 seconds)

      println(result)
      success
    }
  }

  private def multiChainAssets = {
    new AssetCommands {
      override implicit val system: ActorSystem = ActorSystem()
      override implicit val materializer: ActorMaterializer = ActorMaterializer()
      override implicit val credentials: Credentials = Credentials("localhost", multiChainPort, multiChainUser, multiChainPassword)
      override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    }
  }

  private def multiChainAddresses = {
    new AddressCommands {
      override implicit val system: ActorSystem = ActorSystem()
      override implicit val materializer: ActorMaterializer = ActorMaterializer()
      override implicit val credentials: Credentials = Credentials("localhost", multiChainPort, multiChainUser, multiChainPassword)
      override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    }
  }
}
