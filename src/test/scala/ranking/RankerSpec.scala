package test

import search.ranking._
import org.scalatest._
import org.scalamock.scalatest.MockFactory

class ContainsRankerSpec extends FlatSpec with MockFactory with Matchers {
  behavior of "A ContainsRanker"

  it should "rank zero when nothing in the search string is contained" in {
    val ranker = new ContainsRanker(_ => false, identity)
    ranker.rank("none of these words are contained") shouldEqual 0
  }

  it should "rank 100 when everything in the search string is contained" in {
    val tokenizer = mockFunction[Seq[String], Seq[String]]
    tokenizer expects (Seq("all are contained")) returning (Seq(
      "all",
      "are",
      "contained"
    ))

    val ranker = new ContainsRanker(_ => true, tokenizer)

    ranker.rank("all are contained") shouldEqual 100
  }

  it should "rank between 0 and 100 when only some words of the search string are contained" in {
    val tokenizer = mockFunction[Seq[String], Seq[String]]
    val contains = mockFunction[String, Boolean]
    tokenizer expects (Seq("some are contained")) returning (Seq(
      "some",
      "are",
      "contained"
    ))
    contains expects ("some") returning (false)
    contains expects ("are") returning (false)
    contains expects ("contained") returning (true)

    val ranker = new ContainsRanker(contains, tokenizer)

    val rank = ranker.rank("some are contained")
    rank should be >= 0
    rank should be <= 100
  }
}
