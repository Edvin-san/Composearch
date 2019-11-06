package search.indexing

import search.tokenization.Tokenization.Tokenizer
import scala.io.Source

object Index {
  type TokenStore = scala.collection.Set[String]

  case class SourceIndex(
      val description: String,
      val contains: String => Boolean
  )

  abstract class Indexer {
    def build(sources: Seq[Source]): List[SourceIndex]
    protected def tokenize: Tokenizer
    protected def acceptedSource(source: Source): Boolean
    protected def contains(tokenStore: TokenStore)(token: String): Boolean
    protected def tokenStore(source: Source): TokenStore
  }

  class BasicIndexer(tokenizer: Tokenizer) extends Indexer {
    override def build(sources: Seq[Source]) =
      sources
        .filter(acceptedSource)
        .map(source => {
          val tokenStoreOfSource = tokenStore(source)
          SourceIndex(source.descr, contains(tokenStoreOfSource))
        })
        .toList
    override def tokenize = tokenizer
    override def acceptedSource(source: Source) = true
    override def contains(tokenStore: TokenStore)(token: String) =
      tokenStore.contains(token)
    override def tokenStore(source: Source) =
      source
        .getLines()
        .flatMap(line => tokenize(Seq(line)))
        .toSet
  }

  trait TxtFileIndexer extends Indexer {
    abstract override def acceptedSource(source: Source) =
      source.descr.endsWith(".txt") && super.acceptedSource(source)
  }

  trait IgnoreCaseIndexer extends Indexer {
    abstract override def tokenize =
      ((strings: Seq[String]) => strings.map(_.toLowerCase)) andThen super.tokenize
    abstract override def contains(tokenStore: TokenStore)(token: String) =
      super.contains(tokenStore)(token.toLowerCase)
  }
}