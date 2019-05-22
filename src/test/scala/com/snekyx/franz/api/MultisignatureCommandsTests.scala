package com.snekyx.franz.api

import com.snekyx.franz.api.addresses.NewAddressResponse
import com.snekyx.franz.api.permissions.Permission
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.Await
import scala.concurrent.duration._

class MultisignatureCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "multisigTestsBlockChain"

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

  "multisignature address" should {
    "be created" in {
      val addresses = Await.result(multiChainCommands.getAddressList(), 2 seconds)

      val secondAddress = Await.result(multiChainCommands.getNewAddress(), 2 seconds) match {
        case a: NewAddressResponse => a
      }

      Await.result(multiChainCommands.grant(secondAddress.result, List(Permission.Send, Permission.Receive)), 2 seconds)

      val firstAddress = addresses.head
      val multisigResponse = Await.result(multiChainCommands.addMultisigAddress(2, List(firstAddress, secondAddress.result)), 2 seconds)

      multisigResponse mustNotEqual ""
    }
  }
}
