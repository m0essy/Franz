package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.transaction.SuccessResponse
import com.snekyx.franz.api.wallet._
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait WalletCommands extends CommandParams with MultiChainConnector {

  val GET_ADDRESS_BALANCES = "getaddressbalances"
  val GET_TOTAL_BALANCES = "gettotalbalances"
  val GET_MULTI_BALANCES = "getmultibalances"
  val LIST_ADDRESS_TRANSACTIONS = "listaddresstransactions"
  val LIST_WALLET_TRANSACTIONS = "listwallettransactions"

  case class WalletCommand(id: String, method: String, params: Seq[Param])

  def getAddressBalances(address: String, minConf: Int = 1): Future[Seq[WalletResponse]] = {
    val cmd = WalletCommand(uuid, GET_ADDRESS_BALANCES, List(address, minConf)).asJson.noSpaces

    case class Wrapper(result: Seq[AddressBalance])

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map({
          case a: Wrapper => a.result
          case err => Seq(WalletError(0, s"getaddresses returnd an unknown error $err"))

        }) recover {
          case err => Seq(WalletError(0, s"$err"))
        }
    }
  }

  def getTotalBalances() = {

  }

  def getMultiBalances() = {

  }

  def listAddressTransactions() = {

  }

  def listWalletTransactions() = {

  }
}
