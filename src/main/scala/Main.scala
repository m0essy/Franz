import java.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import com.snekyx.franz.api.addresses.{Address, NewAddressResponse}
import com.snekyx.franz.api.assets.Issued
import com.snekyx.franz.api.permissions.Permission
import com.snekyx.franz.api.{AddressCommands, AssetCommands, Credentials, PermissionCommands, StreamCommands}

import collection.JavaConverters._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

object Main extends StreamCommands
  with AssetCommands
  with AddressCommands
  with PermissionCommands {

  private val host = "localhost"
  private val rpcPort = 6834
  private val rpcUser = "multichainrpc"
  private val rpcPassword = "Ghmt3dmWv6TfFz3vVnDYhnyH2ZpLEdkrsUPJ5xYcHwAK"

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val credentials = Credentials(host, rpcPort, rpcUser,rpcPassword)
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
    val responses = Await.result(getAddresses(), 2 seconds)
    val address: Address = responses map {
      case a: Address => a
    } head

    println("getAllAddresses")
    println(address)

    //val issued = Await.result(issue(address.address, "MyCoin5", 10), 2 seconds)

    val newAddress = Await.result(getNewAddress() map {
      case a: NewAddressResponse => a
    }, 2 seconds)

    println("New Address " + newAddress)

    val granted = Await.result(grant(newAddress.result, Seq(Permission.Connect, Permission.Receive)), 2 seconds)
    println(granted)


  }
}
