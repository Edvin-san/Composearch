# Composearch
Composearch (pronounced compo-search) is a composable text search engine powered by Scala.

### Installation

You can run the interactive Composearch console application using [sbt](https://www.scala-sbt.org/) (tested with sbt 1.3.3).

### Demo

Composearch comes with a simple console application that runs a simple searcher.
An example session might look like:

```sh
$ sbt
sbt:Composearch> run tests/simple
Added 2 files to index:
giraffe.txt
multiline.txt
search> a long multiple line
giraffe.txt : 42%
multiline.txt : 14%
search> cats
no matches found
search> :quit
```

The simple searcher has the following behavior:
* It will only look for .txt files in the specified directory.
* It only cares about the words, and pairs of subsequent words, of the search string that are contained in the files.
* A file that has the same words as the search string but in the wrong order will get fewer points than on that has the exact words in the correct order. A file gets 100% if all subsequent pairs of words in the search string are found in the file.
* Uppercase/lowercase of the search string does not matter. 'CASE' is considered the same as 'case'.
* Only the ten highest ranked files are displayed.

### Future work

* Make it easier to compose the behavior of a searcher.
* Add possibility to build a custom searcher through the console application.
* Add support to add other sources than files to index.
* Add other ranking algorithms.
* Add option to save index to disk.

### Licence

* TODO
