package org.apache.spark.nlp

import java.io.StringReader

import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.{CoreAnnotations, CoreLabel, HasWord}
import edu.stanford.nlp.parser.lexparser.LexicalizedParser
import edu.stanford.nlp.process.{CoreLabelTokenFactory, DocumentPreprocessor, PTBTokenizer}
import edu.stanford.nlp.trees.Tree

import scala.annotation.tailrec
import scala.collection.JavaConversions._

object NLPTools {
  val PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"

  val CLASSIFIER_FILE = "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz"

  lazy val parser = new Parser

  lazy val classifier = new Classifier
}

class Classifier {
  lazy val classifier = CRFClassifier.getClassifier(NLPTools.CLASSIFIER_FILE)

  def getEntities(sentence: String) = {

    val labels = classifier.classify(sentence).toList.flatMap(x => x.toList)
      .map(word => (word.word(), word.get(classOf[CoreAnnotations.AnswerAnnotation])))
    getSpans(labels).map(l => (l.reverse.map(x => x._1).mkString(" "), l(0)._2)).filter(_._2 != "O").reverse
  }


  def getSpans(labels: List[(String, String)]): List[List[(String,String)]] = {
    @tailrec
    def recurse(remainingList:List[(String, String)], currentSpan: List[(String, String)], allSpans:List[List[(String, String)]]):List[List[(String,String)]] = {
      remainingList match {
        case Nil => currentSpan:: allSpans
        case (word, annotation) :: tail => currentSpan match {
          case (w, a) :: tail2 if a == annotation => recurse(tail, (word,annotation) :: currentSpan, allSpans)
          case term :: tail2 => recurse(tail, List((word, annotation)), currentSpan :: allSpans)
          case Nil => recurse(tail, List((word, annotation)), allSpans)
        }
      }
    }
    recurse(labels, List(), List())
  }

}

class Parser {

  lazy val tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true")

  lazy val parser = LexicalizedParser.loadModel(NLPTools.PCG_MODEL)

  def parse(str: String): Tree = {
    val tokens = tokenize(str)
    parser.apply(tokens)
  }

  def parse(words: List[HasWord]): Tree = {
    parser.apply(words)
  }

  def tokenize(str: String): java.util.List[CoreLabel] = {
    val tokenizer =
      tokenizerFactory.getTokenizer(
        new StringReader(str))
    tokenizer.tokenize()
  }

  def splitSentences(str: String): List[List[HasWord]] = {
    val processor = new DocumentPreprocessor(new StringReader(str))
    processor.iterator().toList.map(_.toList)
  }
}
