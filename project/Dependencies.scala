import sbt._
import scala.xml.Elem

object Version {
  val spark     = "1.2.0"
  val hadoop    = "2.3.0"
  val slf4j     = "1.7.6"
  val logback   = "1.1.1"
  val scalaTest = "2.1.7"
  val mockito   = "1.9.5"
  val akka      = "2.3.3"
  val algebird  = "0.7.0"
  val play      = "2.2.1"
  val twitterUtil = "6.13.2"
  val dynamo = "1.8.11"
  val jackson = "2.3.2"
  val stanford = "3.4.1"
}

object Library {
  // workaround until 2.11 version for Spark Streaming's available
  val sparkCore = "org.apache.spark"  %% "spark-core" % Version.spark
 // val sparkMLLib     = "org.apache.spark"  %% "spark-mllib"     % Version.spark
  val akkaActor      = "com.typesafe.akka" %% "akka-actor"      % Version.akka
  val akkaTestKit    = "com.typesafe.akka" %% "akka-testkit"    % Version.akka
//  val hadoopClient   = "org.apache.hadoop" %  "hadoop-client"   % Version.hadoop
  val slf4jApi       = "org.slf4j"         %  "slf4j-api"       % Version.slf4j
  val logbackClassic = "ch.qos.logback"    %  "logback-classic" % Version.logback
  val scalaTest      = "org.scalatest"     %% "scalatest"       % Version.scalaTest
  val mockitoAll     = "org.mockito"       %  "mockito-all"     % Version.mockito
  val stanfordCoreNlp = "edu.stanford.nlp" % "stanford-corenlp" % Version.stanford
  val stanfordCoreNlp_models = "edu.stanford.nlp" % "stanford-corenlp" % Version.stanford classifier "models"

}

object Dependencies {

  import Library._

  val sparkAkkaHadoop = Seq(
    sparkCore % "provided",
    akkaActor % "provided",
    akkaTestKit,
//    hadoopClient % "provided",
    logbackClassic % "test",
    scalaTest % "test",
    mockitoAll % "test",
    stanfordCoreNlp,
    stanfordCoreNlp_models//,
    //sparkMLLib

  )
}





