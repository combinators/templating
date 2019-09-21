### Version 1.1.0
- Adds Support for Scala 2.13
- Dependency updates:
  - JavaParser: 3.5.14 -> 3.14.14
  - Apache commons-text: 1.2 -> 1.8

API changes:
- Sequences in templates are now `scala.collection.immutable.Seq` instead of
  `scala.collection.Seq`.

### Version 1.0.0
This is the first public release of templating.
The most notable changes to prior (internal development) versions are:
- The namespace is now `org.combinators`.
- The project has been extracted to be standalone.
- New `BundledResource` to persist resources from the classpath
- Dependency updates, especially javaparser-3.5.7, sbt-twirl-1.3.13 .
- Partial indentation for Python code with `indentExceptFirst`.
- New Persistable 'PythonWithPath'
