package com.snekyx.franz.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.snekyx.franz.api.assets.{AssetError, AssetInfo, AssetResponse, Issued}
import io.circe.generic.auto._
import io.circe.syntax._
import com.snekyx.franz.utils.CirceSupport._

import scala.concurrent.Future

trait AssetCommands extends CommandParams with MultiChainConnector {
  val GETASSETINFO = "getassetinfo"
  val ISSUE = "issue"
  val ISSUE_FROM = "issuefrom"
  val ISSUEMORE = "issuemore"
  val ISSUEMOREFROM = "issuemorefrom"
  val LISTASSETS = "listassets"

  case class AssetCommand(id: String, method: String, params: Seq[Param])

  case class AssetParams(name: String, open: Boolean)

  def issue(address: String, assetName: String, quantity: Double, smallestUnit: Double = 1.0, amount: Double = 0, open: Boolean = false): Future[AssetResponse] = {

    val params = AssetParams(assetName, open).asJson.noSpaces

    val cmd = AssetCommand(uuid, ISSUE, List(address, params, quantity, smallestUnit)).asJson.noSpaces
      .replace("\\\"", "\"")
      .replace("\"{\"name", "{\"name")
      .replace(""+open+"}\"", "" + open + "}")

    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Issued(assetName)
      case resp: HttpResponse                                  =>
        println("Error")
        println(resp.entity)
        AssetError(0, "Error")
    }
  }
  def issueFrom(fromAddress: String, toAddress: String, assetName: String, quantity: Double, smallestUnit: Double = 1.0, amount: Double = 0, open: Boolean = false): Future[AssetResponse] = {

    val params = AssetParams(assetName, open).asJson.noSpaces

    val cmd = AssetCommand(uuid, ISSUE_FROM, List(fromAddress, toAddress, params, quantity, smallestUnit)).asJson.noSpaces
      .replace("\\\"", "\"")
      .replace("\"{\"name", "{\"name")
      .replace(""+open+"}\"", "" + open + "}")

    sendToMultiChain(cmd) map {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Issued(assetName)
      case resp: HttpResponse                                  =>
        println("Error")
        println(resp.entity)
        AssetError(0, "Error")
    }
  }

  def getAssetInfo(assetName: String): Future[AssetResponse] = {
    val cmd = AssetCommand(uuid, GETASSETINFO, List(assetName)).asJson.noSpaces

    case class Wrapper(result: AssetInfo)

    sendToMultiChain(cmd) flatMap {
      case resp: HttpResponse if resp.status == StatusCodes.OK =>
        Unmarshal(resp).to[Wrapper].map(_.result) recover {
            case err => AssetError(0, s"$err")
          }
      case resp: HttpResponse                                  =>
        println("Error")
        Future.successful(AssetError(0, resp.entity.toString))
    }
  }
}
