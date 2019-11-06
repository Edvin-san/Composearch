package test

import search.tokenization._
import org.scalatest._

class ExtractWordsSpec extends FlatSpec with Matchers {
  behavior of "extractWords"

  it should "ignore special characters" in {
    Tokenization.extractWords(Seq(",. ,. ,.,\n%¤#\"#\"!#")) shouldEqual Seq()
  }

  it should "split a sentence into words" in {
    Tokenization.extractWords(Seq("This is a sentence.")) should contain theSameElementsAs
      Seq("This", "is", "a", "sentence")
  }
}

class UnionedNGramsSpec extends FlatSpec with Matchers {
  behavior of "unionedNGrams"

  it should "yield the same sequence when n is 1" in {
    Tokenization.unionedNGrams(1)(Seq("A", "B", "C")) should contain theSameElementsAs
      Seq("A", "B", "C")
  }

  it should "yield singletons and space-separated pairs of tokens when n is 2" in {
    Tokenization.unionedNGrams(2)(Seq("A", "B", "C")) should contain theSameElementsAs
      Seq("A", "B", "C", "A B", "B C")
  }
}
