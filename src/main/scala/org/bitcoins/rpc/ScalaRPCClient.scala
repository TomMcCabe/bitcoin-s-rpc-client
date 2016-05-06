package org.bitcoins.rpc

import org.bitcoins.rpc.marshallers.RPCMarshallerUtil
import org.bitcoins.rpc.marshallers.blockchain.{BlockchainInfoRPCMarshaller, ConfirmedUnspentTransactionOutputMarshaller, MemPoolInfoMarshaller}
import org.bitcoins.rpc.marshallers.mining.MiningInfoMarshaller
import org.bitcoins.rpc.marshallers.networking.NetworkRPCMarshaller
import org.bitcoins.rpc.marshallers.wallet.WalletMarshaller
import org.bitcoins.protocol.Address
import org.bitcoins.rpc.bitcoincore.blockchain.{BlockchainInfo, ConfirmedUnspentTransactionOutput, MemPoolInfo}
import org.bitcoins.rpc.bitcoincore.mining.GetMiningInfo
import org.bitcoins.rpc.bitcoincore.networking.{NetworkInfo, PeerInfo}
import org.bitcoins.rpc.bitcoincore.wallet.WalletInfo
import spray.json._

import scala.sys.process._

/**
  * Created by Tom on 1/14/2016.
  */
class ScalaRPCClient (client : String, network : String) extends RPCMarshallerUtil {
  /**
    * Refer to this reference for list of RPCs
    * https://bitcoin.org/en/developer-reference#rpcs
    *
    * @param command
    * @return
    */
  def sendCommand(command : String) : String = {
    val cmd = client + " " + network + " " + command
    val result = cmd.!!
    result
  }

  /**
    * This will stop the server
    *
    * @return
    */
  def stopServer = sendCommand("stop")

  /**
    * The number of blocks in the local best block chain. For a new node with only the hardcoded genesis block,
    * this number will be 0
    * https://bitcoin.org/en/developer-reference#getblockcount
    *
    * @return
    */
  def getBlockCount : Int = sendCommand("getblockcount").trim.toInt

  /**
    * The getmempoolinfo RPC returns information about the node's current transaction memory pool.
    * https://bitcoin.org/en/developer-reference#getmempoolinfo
    *
    * @return
    */
  def getMemPoolInfo : MemPoolInfo = {
    val result : String = sendCommand("getmempoolinfo")
    MemPoolInfoMarshaller.MemPoolInfoFormatter.read(result.parseJson)
  }

  /**
    * Information about the current state of the local block chain.
    * https://bitcoin.org/en/developer-reference#getblockchaininfo
    *
    * @return
    */
  def getBlockChainInfo : BlockchainInfo = {
    val result : String = sendCommand("getblockchaininfo")
    BlockchainInfoRPCMarshaller.BlockchainInfoFormatter.read(result.parseJson)
  }

  /**
    * The gettxoutsetinfo RPC returns statistics about the confirmed unspent transaction output (UTXO) set.
    * Note that this call may take some time and that it only counts outputs from confirmed transactions—it does
    * not count outputs from the memory pool.
    * https://bitcoin.org/en/developer-reference#gettxoutsetinfo
    *
    * @return
    */
  def getTxOutSetInfo : ConfirmedUnspentTransactionOutput = {
    val result : String = sendCommand("gettxoutsetinfo")
    ConfirmedUnspentTransactionOutputMarshaller.ConfirmedUnspentTransactionOutputFormatter.read(result.parseJson)
  }

  /**
    * The getmininginfo RPC returns various mining-related information.
    * https://bitcoin.org/en/developer-reference#getmininginfo
    *
    * @return
    */
  def getMiningInfo : GetMiningInfo = {
    val result : String = sendCommand("getmininginfo")
    MiningInfoMarshaller.MiningInfoFormatter.read(result.parseJson)
  }

  /**
    * The getnetworkinfo RPC returns information about the node’s connection to the network.
    * https://bitcoin.org/en/developer-reference#getnetworkinfo
    *
    * @return
    */
  def getNetworkInfo : NetworkInfo = {
    val result : String = sendCommand("getnetworkinfo")
    NetworkRPCMarshaller.NetworkInfoFormatter.read(result.parseJson)
  }

  /**
    * The getpeerinfo RPC returns data about each connected network node.
    * https://bitcoin.org/en/developer-reference#getpeerinfo
    *
    * @return
    */
  def getPeerInfo : Seq[PeerInfo] = {
    val result : String = sendCommand("getpeerinfo")
    val json = result.parseJson
    convertToPeerInfoSeq(json)
  }

  /**
    * The getwalletinfo RPC provides information about the wallet.
    * https://bitcoin.org/en/developer-reference#getwalletinfo
    *
    * @return
    */
  def getWalletInfo : WalletInfo = {
    val result : String = sendCommand("getwalletinfo")
    WalletMarshaller.WalletFormatter.read(result.parseJson)
  }

  /**
    * The difficulty of creating a block with the same target threshold (nBits) as the highest-height block in the local
    * best block chain. The number is a a multiple of the minimum difficulty
    * https://bitcoin.org/en/developer-reference#getdifficulty
    *
    * @return
    */
  def getDifficulty : Double = sendCommand("getdifficulty").trim.toDouble

  /**
    * The getnewaddress RPC returns a new Bitcoin address for receiving payments. If an account is specified,
    * payments received with the address will be credited to that account.
    * https://bitcoin.org/en/developer-reference#getnewaddress
    *
    * @return
    */
  def getNewAddress : Address = Address(sendCommand("getnewaddress").trim)

  /**
    * The getrawchangeaddress RPC returns a new Bitcoin address for receiving change. This is for use with raw
    * transactions, not normal use.
    * https://bitcoin.org/en/developer-reference#getrawchangeaddress
    *
    * @return
    */
  def getRawChangeAddress : Address = Address(sendCommand("getrawchangeaddress").trim)

  /**
    * The getbalance RPC gets the balance in decimal bitcoins across all accounts or for a particular account.
    * https://bitcoin.org/en/developer-reference#getbalance
    *
    * @return
    */
  def getBalance : Double = sendCommand("getbalance").toDouble

  /**
    * Returns block hash of a given block height
    * @param height
    * @return
    */
  def getBlock(height : Int) : String = {
    sendCommand("getblockhash " + height).trim
  }

  /**
    * Given an address, returns a 1-of-1 p2sh address and adds it to the wallet.
    * https://bitcoin.org/en/developer-reference#addmultisigaddress
    *
    * @param address
    * @return
    */

  def generateOneOfOneMultiSigAddress(address: String) : Address = {
    val addressInJson = "[\"" + address + "\"]"
    val cmd = "addmultisigaddress 1 " + " " + addressInJson
    val result : String = sendCommand(cmd)
    Address(result.trim)
  }

}

