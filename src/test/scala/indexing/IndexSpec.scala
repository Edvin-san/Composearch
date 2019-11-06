package test

import search.indexing.Index._
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import scala.io.Source

class BasicIndexerSpec extends FlatSpec with MockFactory with Matchers {
  behavior of "A BasicIndexer"

  it should "build a source index that contains all tokens produced by the tokenizer" in {
    val tokenizer = mockFunction[Seq[String], Seq[String]]
    tokenizer expects (Seq("A B")) returning Seq("A", "B")
    val source = Source.fromString("A B")

    val indexer = new BasicIndexer(tokenizer)
    val sourceIndexes = indexer.build(Seq(source))

    sourceIndexes should have size 1
    sourceIndexes.head.contains("A") shouldEqual true
    sourceIndexes.head.contains("B") shouldEqual true
  }

  it should "build a source index that does not contain other tokens produced by the tokenizer" in {
    val source = Source.fromString("")

    val sourceIndexes = new BasicIndexer(_ => Seq()).build(Seq(source))

    sourceIndexes should have size 1
    sourceIndexes.head.contains("A") shouldEqual false
  }

  it should "build a source index that has the same description as the description of the source" in {
    val source = Source.fromString("")
    source.descr = "expected description"

    val sourceIndexes = new BasicIndexer(_ => Seq()).build(Seq(source))

    sourceIndexes should have size 1
    sourceIndexes.head should have('description ("expected description"))
  }
}