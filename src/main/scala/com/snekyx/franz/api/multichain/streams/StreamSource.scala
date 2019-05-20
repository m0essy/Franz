package com.snekyx.franz.api.multichain.streams

import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

//https://doc.akka.io/docs/akka/2.5.4/scala/stream/stream-customize.html

//https://doc.akka.io/docs/akka/current/stream/operators/Source/actorRef.html
class StreamSource extends GraphStage[SourceShape[String]]{

  val out: Outlet[String] = Outlet("StreamSource")

  override def shape: SourceShape[String] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {



    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, "1")
      }
    })
  }

}
