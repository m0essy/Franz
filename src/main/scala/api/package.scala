package object api {
  case class Credentials(ip: String, port: Int, login: String, password: String)

  sealed trait Param {
    val value: Any
  }

  case class StringParam(value: String) extends Param

  case class IntParam(value: Int) extends Param

  case class BooleanParam(value: Boolean) extends Param

  case class CreateFrom(id: String, method: String, params: Seq[Param])

  case class ListStreams(id: String, method: String, params: Seq[Param])

}
