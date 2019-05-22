package com.snekyx.franz.api.administration

import com.snekyx.franz.api.MultiChainException
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class RuntimeParamCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "runtimeParamTestsBlockChain"

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

  "runtime parameter commands" should {
    "get node information" in {

      val info = Await.result(runtimeParamCommands.getInfo(), 2 seconds)

      info.chainname mustEqual "runtimeParamTestsBlockChain"
      info.protocol mustEqual "multichain"
    }
  }
}
