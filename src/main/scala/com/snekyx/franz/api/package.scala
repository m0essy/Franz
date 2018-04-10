package com.snekyx.franz

package object api {
  case class Credentials(ip: String, port: Int, login: String, password: String)

  sealed trait Param {
    val value: Any
  }

  case class StringParam(value: String) extends Param

  case class IntParam(value: Int) extends Param

  case class BooleanParam(value: Boolean) extends Param

  case class MultiChainError(statusCode: Int)


  object streams {

    case class Create(id: String, method: String, params: Seq[Param])

    case class CreateFrom(id: String, method: String, params: Seq[Param])

    case class ListStreams(id: String, method: String, params: Seq[Param])

    case class ListStreamsResult(name: String, createtxid: String, streamref: String, open: Boolean, /*details, */ subscribed: Boolean, synchronized: Boolean, items: Int, confirmed: Int, keys: Int, publishers: Int)

  }
}
