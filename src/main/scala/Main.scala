package main

import search.tokenization.Tokenization
import search.ranking._
import search.indexing.Index._
import search.Search._
import java.io.File
import scala.util.Try

object Main extends App {
  Program
    .readFiles(args)
    .fold(
      println, 
      directory =>
        Program.iterate({
          val searcher = SimpleSearcher().includingFiles(directory.listFiles.toIndexedSeq, "ISO-8859-1")
          println(s"Added ${searcher.sourceIndexes.size} files to index:\n" + 
            searcher.sourceIndexes.map(_.description).mkString("\n"))
          searcher
        })
    )
}

object Program {
  import scala.io.StdIn.readLine

  sealed trait ReadFileError

  case object MissingPathArg extends ReadFileError
  case class NotDirectory(error: String) extends ReadFileError
  case class FileNotFound(t: Throwable) extends ReadFileError

  def readFiles(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }

  def iterate(searcher: Searcher): Unit = {
    print(s"search> ")
    readLine() match {
      case "" => {
        println()
        iterate(searcher)
      }
      case ":quit" =>
      case searchString => {
        val topTen = searcher
          .search(searchString)
          .sortBy(_.rank)
          .reverse
          .filter(_.rank > 0)
          .take(10)
          .map {
            case SearchResult(identifier, rank) => s"${identifier} : ${rank}%"
          }
        val message = topTen match {
          case List() => "No matches found"
          case v      => v.mkString("\n")
        }
        println(message)

        iterate(searcher)
      }
    }
  }
}