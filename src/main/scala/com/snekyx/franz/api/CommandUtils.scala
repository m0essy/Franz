package com.snekyx.franz.api

import java.util.UUID

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

trait CommandUtils {



  implicit def string2Param (param: String): Param   = StringParam(param)
  implicit def int2Param (param: Int): Param         = IntParam(param)
  implicit def boolean2Param (param: Boolean): Param = BooleanParam(param)

  implicit object RolesSeqEncoder extends Encoder[Seq[Param]] {
    override def apply(rs: Seq[Param]): Json = rs.flatMap(_.asJson.findAllByKey("value")).asJson
  }

  implicit val encodeParam: Encoder[Param] = Encoder.instance {
    case x: StringParam  => x.asJson
    case x: IntParam     => x.asJson
    case x: BooleanParam => x.asJson
  }

  def generateId = {
    UUID.randomUUID().toString
  }
}
