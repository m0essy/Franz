package com.snekyx.franz.api.administration

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.{AddressCommands, Credentials}
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.specs2.matcher.MatcherMacros

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class MiningCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "miningTestsBlockChain"

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

  "Mining commands" should {
    "pause mining" in {
      val paused = Await.result(miningCommands.pauseMining(), 2 seconds)

      println(paused)
      success
    }

    "resume mining" in {
      val resumed = Await.result(miningCommands.resumeMining(), 2 seconds)

      println(resumed)
      success
    }
  }

  private def miningCommands = {
    new MiningCommands {
      override implicit val system: ActorSystem = ActorSystem()
      override implicit val materializer: ActorMaterializer = ActorMaterializer()
      override implicit val credentials: Credentials = Credentials("localhost", multiChainPort, multiChainUser, multiChainPassword)
      override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    }
  }
}
