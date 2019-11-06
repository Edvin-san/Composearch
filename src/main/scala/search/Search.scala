package search

import search.tokenization.Tokenization
import search.ranking._
import search.indexing.Index._
import scala.io.Source
import java.io.File

object Search {
  case class SearchResult(identifier: String, rank: Int)

  trait Searcher {
    def includingSources(sources: Seq[Source]): Searcher
    def search(searchString: String): Seq[SearchResult]
  }

  case class SimpleSearcher(val sourceIndexes: List[SourceIndex] = List()) extends Searcher {
    // We look for words and pairs of subsequent words,
    // giving extra points to sources that better match the search string's order of words.
    val tokenize = Tokenization.extractWords andThen Tokenization.unionedNGrams(2)
    val indexer = new BasicIndexer(tokenize) with TxtFileIndexer with IgnoreCaseIndexer

    def includingFiles(files: Seq[File], encoding: String) =
      includingSources(files.filter(_.isFile).map { file =>
        val source = Source.fromFile(file, enc = encoding)
        source.descr = file.getName
        source
      })

    override def includingSources(sources: Seq[Source]) =
      SimpleSearcher(sourceIndexes ++ indexer.build(sources))

    override def search(searchString: String) =
      sourceIndexes.toSeq.map(sourceIndex => {
        val ranker = new ContainsRanker(sourceIndex.contains, tokenize)
        SearchResult(sourceIndex.description, ranker.rank(searchString))
      })
  }
}