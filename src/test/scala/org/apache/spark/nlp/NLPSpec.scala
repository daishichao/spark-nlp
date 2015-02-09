package org.apache.spark.nlp

import org.scalatest.{BeforeAndAfterAll, Assertions, FlatSpec}

/**
 * Created by tristan on 2/5/15
 */
class NLPSpec extends FlatSpec with Assertions with BeforeAndAfterAll {

  "classifier" should "get results" in {

    val parsed = NLPTools.classifier.getEntities("Good afternoon Rajat Raina, how are you today?")
    assert(parsed == List(("Rajat Raina", "PERSON")))
  }


}
