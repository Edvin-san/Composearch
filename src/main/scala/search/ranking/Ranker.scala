package search.ranking

import search.tokenization.Tokenization.Tokenizer

trait Ranker {
	// Returns the rank score of the search string.
	// A high score means it is a good match.
  def rank(searchString: String): Int
}

class ContainsRanker(
    val containsToken: String => Boolean,
    val tokenize: Tokenizer
) extends Ranker {
  override def rank(searchString: String) = {
    val tokens = tokenize(Seq(searchString))
    val numHits = tokens.filter(containsToken).size
    if (tokens.size > 0) numHits * 100 / tokens.size else 0
  }
}