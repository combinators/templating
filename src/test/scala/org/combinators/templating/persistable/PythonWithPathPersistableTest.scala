package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import PythonWithPathPersistable._
import org.combinators.templating.twirl.Python
import org.scalatest._
import funspec._

class PythonWithPathPersistableTest extends FixtureAnyFunSpec with TempDirectoryFixture with GivenWhenThen {
  val persistable: PythonWithPathPersistable.Aux[PythonWithPath] = PythonWithPathPersistable.apply

  describe("Persisting a piece of Python code to a file") {
    it("should create a file named by its content") { (tmpDir: Path) =>
      Given("an element to persist")
      val elementToPersist: PythonWithPath =
        PythonWithPath(
          Python(s"""class Foo:
                    |    pass
                """.stripMargin),
          Paths.get("bar/test.py"))

      When("persisting it")
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist)

      Then("its corresponding file should exist")
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      assert(Files.exists(expectedPath))

      And("the file should have exactly the specified contents")
      assert(Files.readAllBytes(expectedPath).sameElements(elementToPersist.code.getCode.getBytes))

      And("trying to persist the same element again should raise an exception")
      assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersist))

      Then("it should not produce the exception when overwrite semantics is specified")
      persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
    }
  }
}
