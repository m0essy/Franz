package com.snekyx.franz.api

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{Uri, headers}
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait MultiChainConnector {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val credentials: Credentials
  implicit val ec: ExecutionContext

  def multiChainUri = Uri("http://" + credentials.ip + ":" + credentials.port)

  def auth = headers.Authorization(BasicHttpCredentials(credentials.login, credentials.password))

  def uuid: String = UUID.randomUUID().toString

}
