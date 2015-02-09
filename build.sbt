import AssemblyKeys._

name := """spark-nlp"""

scalaVersion := "2.10.4"

resolvers ++= Seq(
	"Akka Repository" at "http://repo.akka.io/releases/",
	"Typesafe" at "http://repo.typesafe.com/typesafe/releases",
	"Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)

libraryDependencies ++= Dependencies.sparkAkkaHadoop

releaseSettings

initialCommands in console := """
  |import org.apache.spark._
  |import org.apache.spark.streaming._
  |import org.apache.spark.streaming.StreamingContext._
  |import org.apache.spark.streaming.dstream._
  |import akka.actor.{ActorSystem, Props}
  |import com.typesafe.config.ConfigFactory
  |""".stripMargin

assemblySettings
