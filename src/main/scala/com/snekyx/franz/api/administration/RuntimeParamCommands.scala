package com.snekyx.franz.api.administration

import com.snekyx.franz.api.{CommandParams, MultiChainConnector}

trait RuntimeParamCommands extends CommandParams with MultiChainConnector  {

  val GETINFO             = "getinfo"
  val GETBLOCKCHAINPARAMS = "getblockchainparams"
  val GETRUNTIMEPARAMS    = "getruntimeparams"
  val SETRUNTUMEPARAMS    = "setruntimeparam"
  val STOP = "stop"

  def getInfo() = {

  }

  def stop(): {

  }
}
