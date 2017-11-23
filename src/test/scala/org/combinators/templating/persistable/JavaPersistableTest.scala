package org.combinators.templating.persistable

import java.nio.file.Paths
import com.github.javaparser.ast.CompilationUnit

import JavaPersistable._
import org.combinators.templating.twirl.Java

import org.scalatest._

class JavaPersistableTest extends FunSpec {
  val className = "Foo"
  val classText = s"import whatever; class $className {}"
  val persistableInstance: Persistable.Aux[CompilationUnit] = implicitly[Persistable.Aux[CompilationUnit]]
  val pathPrefix = Seq("src", "main", "java")

  describe("Persisting a Java compilation unit") {
    describe(s"when it contains only one class $className and no package") {
      it(s"should use 'src/main/java/$className.java' as path") {
        val expectedPathComponents = pathPrefix :+ s"$className.java"
        assert(persistableInstance.path(Java(classText).compilationUnit())
          == Paths.get(expectedPathComponents.head, expectedPathComponents.tail:_*))
      }
    }
    describe("when it contains multiple classes and interfaces") {
      val otherClassText = "interface FooI {}\n class Bar {}"
      it("should use the name of the first class to determine the path") {
        val expectedPathComponents = pathPrefix :+ s"$className.java"
        assert(persistableInstance.path(Java(Seq(classText, otherClassText).mkString("\n")).compilationUnit())
          == Paths.get(expectedPathComponents.head, expectedPathComponents.tail:_*))
      }
    }
    describe("when it contains a package declaration") {
      val packages = Seq("foo", "bar")
      val packagesText = s"package ${packages.mkString(".")};"
      it("should prefix the path with a directory structure for the package") {
        val expectedPathComponents = pathPrefix ++ packages :+ s"$className.java"
        assert(persistableInstance.path(Java(Seq(packagesText, classText).mkString("\n")).compilationUnit())
          == Paths.get(expectedPathComponents.head, expectedPathComponents.tail:_*))
      }
    }
    it("should store text that parses back to an equal CompilationUnit") {
      val originalUnit = Java(classText).compilationUnit()
      assert(Java(persistableInstance.rawText(originalUnit)).compilationUnit() == originalUnit)
    }
  }
}

