package org.bitcoins.rpc.marshallers.blockchain

import org.bitcoins.rpc.marshallers.RPCMarshallerUtil
import org.bitcoins.rpc.bitcoincore.blockchain.{BlockChainInfoImpl, BlockchainInfo}
import spray.json._

/**
 * Created by Tom on 1/11/2016.
 */
object BlockchainInfoRPCMarshaller extends DefaultJsonProtocol with RPCMarshallerUtil{
  val chainKey = "chain"
  val blockCountKey = "blocks"
  val headerCountKey = "headers"
  val bestBlockHashKey = "bestblockhash"
  val difficultyKey = "difficulty"
  val verificationProgressKey = "verificationprogress"
  val chainWorkKey = "chainwork"

  implicit object BlockchainInfoFormatter extends RootJsonFormat[BlockchainInfo] {
    override def read (value : JsValue) : BlockchainInfo = {
      val obj = value.asJsObject
      val chain = obj.fields(chainKey).convertTo[String]
      val blockCount = obj.fields(blockCountKey).convertTo[Int]
      val headerCount = obj.fields(headerCountKey).convertTo[Int]
      val bestBlockHash = obj.fields(bestBlockHashKey).convertTo[String]
      val difficulty = obj.fields(difficultyKey).convertTo[Double]
      val verificationProgress = obj.fields(verificationProgressKey).convertTo[Double]
      val chainWork = obj.fields(chainWorkKey).convertTo[String]
      BlockChainInfoImpl(chain, blockCount, headerCount, bestBlockHash, difficulty, verificationProgress, chainWork)
    }

    override def write (detail : BlockchainInfo) : JsValue = {
      val m : Map[String, JsValue] = Map (
        chainKey -> JsString(detail.chain),
        blockCountKey -> JsNumber(detail.blockCount),
        headerCountKey -> JsNumber(detail.headerCount),
        bestBlockHashKey -> JsString(detail.bestBlockHash),
        difficultyKey -> JsNumber(detail.difficulty),
        verificationProgressKey -> JsNumber(detail.verificationProgress),
        chainWorkKey -> JsString(detail.chainWork)
        )
      JsObject(m)
    }
  }
}