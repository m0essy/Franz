package com.snekyx.franz.api.util

import com.snekyx.franz.api.Credentials

import scala.io.Source
import scala.sys.process._
import scala.util.{Failure, Success, Try}

//noinspection SpellCheckingInspection
trait MultiChainSetup {


  val multiChainName = "test1bc"

  def multiChainUser: String = {
    val homeDirectory = System.getProperty("user.home")
    val filename = s"$homeDirectory\\AppData\\Roaming\\MultiChain\\$multiChainName\\multichain.conf"
    extractString(filename, "rpcuser=")
  }

  def multiChainPort: Int = {
    val homeDirectory = System.getProperty("user.home")
    val filename = s"$homeDirectory\\AppData\\Roaming\\MultiChain\\$multiChainName\\params.dat"
    val portString = extractString(filename, "default-rpc-port = ")
    portString.split('#').head.trim.toInt
  }

  def multiChainPassword: String = {
    val homeDirectory = System.getProperty("user.home")
    val filename = s"$homeDirectory\\AppData\\Roaming\\MultiChain\\$multiChainName\\multichain.conf"
    extractString(filename, "rpcpassword=")
  }

  private def extractString(filename: String, phrase: String) = {
    val source = Source.fromFile(filename)
    val password = (for {
      line <- source.getLines
      if line.startsWith(phrase)
    } yield line.split('=').last).toList.head
    source.close()
    password
  }

  protected def startBlockChainDaemon: Process = {
    println("multichain starting: ")
    Process(s"multichaind $multiChainName -daemon").run()
  }

  protected def createBlockChain(): Unit = {
    println(s"multichain creating block chain: $multiChainName")
    val multiChainCreation = s"multichain-util create $multiChainName" !!

    println("creating multichain resulted in: " + multiChainCreation)
  }

  protected def deleteBlockChain(): Unit = {
    val homeDirectory = System.getProperty("user.home")
    Try {
      val deleteResult = s"rm -r $homeDirectory\\AppData\\Roaming\\MultiChain\\$multiChainName" !!

      println("multichain was deleted." + deleteResult)
    } match {
      case Success(v) =>
      case Failure(err) => println("Delete failed. " + err)
    }
  }

  protected def stopBlockChain(): Unit = {
    val stopResult = s"multichain-cli $multiChainName stop" !!

    println("multichain was stopped: " + stopResult)
  }
}
