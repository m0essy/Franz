package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.util.MultiChainSetup
import com.snekyx.franz.api.wallet.{AddressBalance, AddressTransaction}
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class WalletCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll {

  sequential

  override val multiChainName = "walletTestsBlockChain"

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

  "wallet commands" should {
    "show all address balances" in {
      val address = Await.result(multiChainCommands.getAddressList(), 2 seconds).head
      Await.result(multiChainCommands.issue(address, "MyCoin", 100, 0.001), 2 seconds)
      val balances  = Await.result(multiChainCommands.getAddressBalances(address), 2 seconds) map {
        case a: AddressBalance => a
      }

      balances must have size 1
      balances.head.name mustEqual "MyCoin"
      balances.head.qty mustEqual 100
    }

    "list all transactions for an address" in {
      val address = Await.result(multiChainCommands.getAddressList(), 2 seconds).head
      val transactions = Await.result(multiChainCommands.listAddressTransactions(address), 2 seconds) map {
        case a: AddressTransaction => a
      }

      transactions must have size 2
    }
  }
}
