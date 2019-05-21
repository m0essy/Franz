package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.addresses.NewAddressResponse
import com.snekyx.franz.api.permissions.Permission
import com.snekyx.franz.api.util.MultiChainSetup
import com.snekyx.franz.api.wallet.AddressBalance
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.specs2.matcher.MatcherMacros

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class TransactionCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "transactionTestsBlockChain"

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

  "transaction commands" should {
    "send asset to address" in {
      val asset = "SteelUnit"

      val oldAddress = Await.result(multiChainCommands.getAddressList(), 2 seconds).head

      Await.result(multiChainCommands.issue(oldAddress, asset, 100, 0.1), 2 seconds)

      val newAddress = Await.result(multiChainCommands.getNewAddress(), 2 seconds) match {
        case a: NewAddressResponse => a
      }

      Await.result(multiChainCommands.grant(newAddress.result, Seq(Permission.Receive)), 2 seconds)

      Await.result(multiChainCommands.sendAsset(newAddress.result , asset, 0.1), 2 seconds)

      val balances = Await.result(multiChainCommands.getAddressBalances(newAddress.result), 2 seconds)

      balances mustEqual List(AddressBalance("SteelUnit",0.1,None))
    }

    "send asset from address to address" in {
      val asset = "SteelUnit123"

      val fromAddress = Await.result(multiChainCommands.getAddressList(), 2 seconds).head

      Await.result(multiChainCommands.issue(fromAddress, asset, 100, 0.1), 2 seconds)

      val toAddress = Await.result(multiChainCommands.getNewAddress(), 2 seconds) match {
        case a: NewAddressResponse => a
      }

      Await.result(multiChainCommands.grant(toAddress.result, Seq(Permission.Receive)), 2 seconds)

      Await.result(multiChainCommands.sendAssetFrom(fromAddress, toAddress.result , asset, 0.1), 2 seconds)

      val balances = Await.result(multiChainCommands.getAddressBalances(toAddress.result), 2 seconds)

      balances mustEqual List(AddressBalance("SteelUnit123",0.1,None))
    }
  }
}
