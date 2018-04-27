package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.addresses.NewAddressResponse
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import org.specs2.matcher.MatcherMacros
import scala.language.experimental.macros

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class AddressCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll with MatcherMacros {

  sequential

  override val multiChainName = "addressTestsBlockChain"

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
    "create a new Address" in {
      val result = Await.result(multiChainAddresses.getNewAddress(), 2 seconds)

      result match {
        case x: NewAddressResponse => success
        case _ => failure("expected Result is NewAddressResponse")
      }
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
