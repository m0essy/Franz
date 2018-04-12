package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.streams.CreationResponse
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
class MultiChainStreamTests extends Specification with MultiChainSetup with BeforeAfterAll {

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
    "create Stream" in {
      val result = Await.result(multiChainStreams.create("stream1", open = true), 2 seconds)
      result mustEqual CreationResponse()
    }
  }

  private def multiChainStreams = {
    new StreamCommands {
      override implicit val system: ActorSystem = ActorSystem()
      override implicit val materializer: ActorMaterializer = ActorMaterializer()
      override implicit val credentials: Credentials = Credentials("localhost", multiChainPort, multiChainUser, multiChainPassword)
      override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    }
  }
}
