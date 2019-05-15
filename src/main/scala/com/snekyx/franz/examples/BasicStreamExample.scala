package com.snekyx.franz.examples

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream._
import scala.concurrent.Await
import scala.concurrent.duration._

object BasicStreamExample {
  implicit val actorSystem = ActorSystem("BasicStreamExample")
  implicit val executionContext = actorSystem.dispatcher
  val settings = ActorMaterializerSettings(actorSystem)
  implicit val mat = ActorMaterializer(settings)

  val timeout = 1 minutes

  def main(args: Array[String]): Unit = {

//    val streamIrems = Source[String, Unit] = Source()



    Await.result(actorSystem.whenTerminated, timeout)
  }
}