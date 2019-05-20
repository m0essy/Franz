package com.snekyx.franz.api.administration

import com.snekyx.franz.api.{CommandParams, MultiChainConnector}

trait MiningCommands extends CommandParams with MultiChainConnector  {

  def pauseMining() = {

  }

  def resumeMining() = {

  }
}
