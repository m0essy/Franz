package com.snekyx.franz.api.smartFilters

import com.snekyx.franz.api.{CommandParams, MultiChainConnector}

trait SmartFilterCommands extends CommandParams with MultiChainConnector {

  val APPROVEFROM       = "approvefrom"
  val CREATE            = "create"
  val CREATEFROM        = "createfrom"
  val GETFILTERCODE     = "getfiltercode"
  val LISTSTREAMFILTERS = "liststreamfilters"
  val LISTTXFILTERS     = "listtxfilters"
  val LISTUPGRADES      = "listupgrades"
  val RUNSTREAMFILTER   = "runstreamfilter"
  val RUNTXFILTER       = "runtxfilter"
  val TESTSTREAMFILTER  = "teststreamfilter"
  val TESTTXFILTER      = "testtxfilter"

  def create(filterType: String, name: String, restrictions: String, jsCode: String) = {
    //todo implement
  }

  def upgrade(name: String, open: Boolean = false, params: Seq[CommandParams] = Seq.empty) = {
    //todo implement
  }

  def createFrom() = {
    //todo implement
  }

  def upgradeFrom() = {
    //todo implement
  }

  def approveFrom() = {
    //todo implement
  }

  def getFilterCode(filterName: String) = {
    //todo implement
  }

  def listStreamFilters(filters: String = "*", verbose: Boolean = false) = {
    //todo implement
  }

  def listTxFilters(filters: String = "*", verbose: Boolean = false) = {
    //todo implement
  }

  def listUpgrades(upgrades: String = "*") = {
    //todo implement
  }

  def runStreamFilter(filter: String, transactionId: String, vout: Int = 0) = {
    //todo implement
  }

  def runTxFilter(filter: String, transactionId: String, vout: Int = 0) = {
    //todo implement
  }

  def testStreamFilter(restrictions: String, jsCode: String, transactionId: String, vout: Int = 0) = {
    //todo implement
  }

  def testTxFilter(restrictions: String, jsCode: String, transactionId: String, vout: Int = 0) = {
    //todo implement
  }
}
