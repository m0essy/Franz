package com.snekyx.franz.api

import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

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
}
