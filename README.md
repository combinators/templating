# org.combinators.templating 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.combinators/templating_2.13/badge.png?style=flat)](https://search.maven.org/search?q=g:org.combinators%20AND%20a:templating%2A)
[![Test code, update coverage, and release master branch](https://github.com/combinators/templating/actions/workflows/test_and_release.yml/badge.svg?branch=master)](https://github.com/combinators/templating/actions/workflows/test_and_release.yml)
[![Coverage Status](https://coveralls.io/repos/github/combinators/templating/badge.svg?branch=master)](https://coveralls.io/github/combinators/templating?branch=master)
[![Join the chat at https://gitter.im/combinators/cls-scala](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/combinators/cls-scala)
## Templating support for multiple languages

This project adds templating support for multiple languages.

For more information see our [documentation project](https://combinators.github.io/).

Existing users please refer to the [CHANGELOG](CHANGELOG.md) for news.

## Installation

Add the following dependency to your existing sbt project: 
```scala
libraryDependencies += "org.combinators" %% "templating" % "<VERSION>"
```
The string `<VERSION>` has to be replaced by the version you want.
You can search for released versions [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.combinators%22%20AND%20a%3A%22templating%22).

To obtain the latest unreleased development version, clone the repository and run `sbt publishLocal`.

Currently, Scala 2.13, 3.3 and 3.6 are supported.

## Examples

The [tests](src/test/scala/org/combinators/templating) provide some examples, for the rest check the [documentation project](https://combinators.github.io/).

## Help and Contributions

Join [combinators/cls-scala](https://gitter.im/combinators/cls-scala) on Gitter.

### Main Authors

- Jan Bessai
- Boris Düdder
- Geroge T. Heineman

### Contributers

-
##### Your name here?
Just the usual: open pull requests and or issues.
Feel free to add yourself to the list in this file, if you contributed something.
