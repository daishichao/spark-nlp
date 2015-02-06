package org.apache.spark.nlp

import java.util.concurrent.Executors

import com.typesafe.config.{ Config, ConfigFactory }
import edu.stanford.nlp.ling.CoreAnnotations
import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.SparkContext._

class SparkNLPDriver extends Serializable {
  val conf: Config = ConfigFactory.load()
  val jar = this.getClass.getProtectionDomain.getCodeSource.getLocation.toURI.toString

  def run(args: Array[String]) = {
    val executor = Executors.newFixedThreadPool(1)

    val sparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("sparknlp")
      .set("spark.executor.memory", "3g")

    val sc = new SparkContext(sparkConf)

    val articlesRDD = sc.wholeTextFiles("/Users/tristan/Downloads/articles/")

    val splitSentences = articlesRDD.flatMap(
      {case (fileName, content) =>
        val sentences = NLPTools.parser.splitSentences(content)

        sentences.map(x => (fileName, x))

      })

    val entities = splitSentences.map({case (fileName, content) => (fileName, NLPTools.classifier.getEntities(content.map(_.word()).mkString(" ")))})
      .flatMap({ case (fileName, list) => list.map(x => (fileName, x))}).groupByKey

     entities.take(100).foreach(println)

      sc.stop()
  }

}

object SparkNLPDriver {

  def main(args: Array[String]) = {
    val sparkNLPDriver = new SparkNLPDriver
    sparkNLPDriver.run(args)
  }
}

