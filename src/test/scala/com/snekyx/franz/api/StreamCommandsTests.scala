package com.snekyx.franz.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.util.MultiChainSetup
import org.specs2.execute.Result
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class StreamCommandsTests extends Specification with MultiChainSetup with BeforeAfterAll {

  sequential

  override val multiChainName = "streamTestsBlockChain"

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

  val streamName = "stream1"

  "Multichain" should {
    "create a Stream" in {
      val result = Await.result(multiChainStreams.create(streamName, open = true), 2 seconds)
      result mustEqual CreationResponse()
    }

    "publish an item to the stream" in {
      val result = Await.result(multiChainStreams.publish(streamName, "Key1", """{data: "Data..."}""""), 2 seconds)
      result mustEqual Published(streamName)
    }

    "list streams" in {
      val result = Await.result(multiChainStreams.listStreams(), 2 seconds)
      result must have size 2
    }

    "list subscribe to stream" in {
      val result = Await.result(multiChainStreams.subscribe(streamName), 2 seconds)
      result mustEqual Subscribed(streamName)

      val listResult = Await.result(multiChainStreams.listStreams(), 2 seconds)
      val stream = (listResult filter {
        case s: StreamDetails => s.name == streamName && s.subscribed
        case _ => false
      })

      stream must have size 1
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
