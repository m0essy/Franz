package com.snekyx.franz.api

object HexStringUtil {
  // convert normal string to hex bytes string
  def string2hex(str: String): String = {
    str.toList.map(_.toInt.toHexString).mkString
  }

  // convert hex bytes string to normal string
  def hex2string(hex: String): String = {
    hex.sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toChar).mkString
  }
}
