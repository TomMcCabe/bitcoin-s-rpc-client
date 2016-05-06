package org.bitcoins.rpc.marshallers.blockchain

import org.bitcoins.rpc.bitcoincore.blockchain.BlockchainInfo
import org.scalatest.{FlatSpec, MustMatchers}
import spray.json._

/**
 * Created by Tom on 1/11/2016.
 */
class BlockchainInfoMarshallerTest extends FlatSpec with MustMatchers {
  val str =
    """
      |{
      |    "chain" : "test",
      |    "blocks" : 632532,
      |    "headers" : 632532,
      |    "bestblockhash" : "00000000c3dbad564236cc6b127ae53126e76632825f8b68bca89251b85e43b4",
      |    "difficulty" : 1.00000000,
      |    "verificationprogress" : 0.99999997,
      |    "chainwork" : "00000000000000000000000000000000000000000000000730748cb316d3d01f"
      |}
    """.stripMargin

  val json = str.parseJson

  "BlockchainInfoMarshaller" must "parse blockchain information" in {
    val detail : BlockchainInfo = BlockchainInfoRPCMarshaller.BlockchainInfoFormatter.read(json)
    detail.chain must be ("test")
    detail.blockCount must be (632532)
    detail.headerCount must be (632532)
    detail.bestBlockHash must be ("00000000c3dbad564236cc6b127ae53126e76632825f8b68bca89251b85e43b4")
    detail.difficulty must be (1.00000000)
    detail.verificationProgress must be (0.99999997)
    detail.chainWork must be ("00000000000000000000000000000000000000000000000730748cb316d3d01f")
  }

  it must "write blockchain info" in {
    val json = str.parseJson
    val detail : BlockchainInfo = BlockchainInfoRPCMarshaller.BlockchainInfoFormatter.read(json)
    val writtenBlockchain = BlockchainInfoRPCMarshaller.BlockchainInfoFormatter.write(detail)
    writtenBlockchain.asJsObject.fields("chain") must be (JsString("test"))
    writtenBlockchain.asJsObject.fields("blocks") must be (JsNumber(632532))
    writtenBlockchain.asJsObject.fields("headers") must be (JsNumber(632532))
    writtenBlockchain.asJsObject.fields("bestblockhash") must be (JsString("00000000c3dbad564236cc6b127ae53126e76632825f8b68bca89251b85e43b4"))
    writtenBlockchain.asJsObject.fields("difficulty") must be (JsNumber(1.00000000))
    writtenBlockchain.asJsObject.fields("verificationprogress") must be (JsNumber(0.99999997))
    writtenBlockchain.asJsObject.fields("chainwork") must be (JsString("00000000000000000000000000000000000000000000000730748cb316d3d01f"))
  }

}
