package search.tokenization

object Tokenization {
  type Tokenizer = Seq[String] => Seq[String]

  def extractWords: Tokenizer = (strings) => strings.flatMap(_.split("\\W+"))

  def unionedNGrams(n: Int): Tokenizer =
    (tokens) =>
      if (n <= 1) tokens
      else
        nGrams(n, tokens).map(_.mkString(" ")) ++ unionedNGrams(n - 1)(tokens)

  private def nGrams[A](n: Int, items: Seq[A]): Seq[Seq[A]] =
    items.sliding(n, 1).toSeq
}