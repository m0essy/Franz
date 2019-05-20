package com.snekyx.franz

import com.snekyx.franz.api.permissions.Permission.Permission

package object api {

  case class Credentials(ip: String, port: Int, login: String, password: String)

  sealed trait Param {
    val value: Any
  }

  case class StringParam(value: String) extends Param

  case class IntParam(value: Int) extends Param

  case class BooleanParam(value: Boolean) extends Param

  case class DoubleParam(value: Double) extends Param

  case class MultiChainError(code: Int, message: String)

  object addresses {

    // commands
    case class GetNewAddress(id: String, method: String)

    case class GetAddresses(id: String, method: String, params: Seq[Param])

    // responses
    sealed trait AddressResponse

    case class AddressCommandError(statusCode: Int, errorCode: Int, message: String) extends AddressResponse

    case class NewAddressResponse(result: String) extends AddressResponse

    case class Address(address: String, ismine: Boolean, iswatchonly: Boolean, isscript: Boolean, pubkey: String,
                       iscompressed: Boolean, account: String, synchronized: Boolean) extends AddressResponse

  }

  object permissions {

    sealed trait PermissionResponse

    object Permission extends Enumeration {
      type Permission = Value
      val Connect = Value("connect")
      val Send = Value("send")
      val Receive = Value("receive")
      val Issue = Value("issue")
      val Mine = Value("mine")
      val Activate = Value("activate")
      val Admin = Value("admin")
    }


    case class Grant(id: String, method: String, params: Seq[Param])

    case class PermissionsGranted(address: String) extends PermissionResponse

    case class PermissionError(result: String, error: MultiChainError, id: Option[String] = None) extends PermissionResponse

    def permissions2String(permissions: Seq[Permission]): String = {
      val p = permissions.foldLeft("")((str, item) => str match {
        case "" => item.toString
        case _ => str + "," + item
      })
      p
    }
  }

  // todo create Multichain Error case class with code, and message
  // todo add id to the error response case classes

  object streams {

    sealed trait StreamResponse

    case class StreamError(statusCode: Int, message: String) extends StreamResponse

    case class Create(id: String, method: String, params: Seq[Param])

    case class CreateFrom(id: String, method: String, params: Seq[Param])

    case class CreationResponse() extends StreamResponse

    case class CreationError(statusCode: Int, errorCode: Int, message: String) extends StreamResponse

    case class Subscribed(streamName: String) extends StreamResponse

    case class Published(streamName: String) extends StreamResponse

    case class StreamDetails(name: String,
                             createtxid: String,
                             streamref: Option[String] = None,
                             subscribed: Boolean = false,
                             synchronized: Option[Boolean] = None,
                             items: Option[Int] = None,
                             confirmed: Option[Int] = None,
                             keys: Option[Int] = None,
                             publishers: Option[Int] = None) extends StreamResponse

  }

  object assets {
    sealed trait AssetResponse

    case class AssetError(statusCode: Int, message: String) extends AssetResponse

    case class Issued(assetName: String) extends AssetResponse

    case class AssetInfo(info: String) extends AssetResponse
  }
}


