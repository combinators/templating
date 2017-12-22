package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.scalatest._

class PersistableTest extends fixture.FunSpec with TempDirectoryFixture with GivenWhenThen {
  val persistable: Persistable.Aux[String] = new Persistable {
    override type T = String
    override def rawText(elem: T): Array[Byte] = elem.getBytes
    override def path(elem: T): Path = Paths.get(elem)
  }
  describe("Persisting a String to a file named by its content") {
    it("should create a file named by its content") { tmpDir: Path =>
      Given("an element to persist")
      val elementToPersist = "test"

      When("persisting it")
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist)
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist)

      Then("its corresponding file should exist")
      assert(Files.exists(expectedPath))

      And("the file should have exactly the specified contents")
      assert(Files.readAllBytes(expectedPath).sameElements(elementToPersist.getBytes))

      And("trying to persist the same element again should raise an exception")
      assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersist))

      Then("it should not produce the exception when overwrite semantics is specified")
      persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
    }
  }

  describe("Persisting an element that specifies a subdirectory") {
    it("should create a subdirectory") { tmpDir: Path =>
      Given("an element to persist")
      val elementToPersistInSubdir = Paths.get("foo", "test").toString

      When("persisting it")
      persistable.persist(tmpDir.toAbsolutePath, elementToPersistInSubdir)
      val expectedSubdir = tmpDir.toAbsolutePath.resolve("foo")

      Then("its corresponding subdirectory should exist")
      assert(Files.exists(expectedSubdir) && Files.isDirectory(expectedSubdir))

      And("its corresponding file should exist")
      expectedSubdir.toFile
      val expectedFileInSubdir = expectedSubdir.resolve("test")
      assert(Files.exists(expectedFileInSubdir))

      And("the file should have exactly the specified contents")
      assert(Files.readAllBytes(expectedFileInSubdir).sameElements(elementToPersistInSubdir.getBytes))

      And("trying to persist the same element again should raise an exception")
      assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersistInSubdir))

      Then("it should not produce the exception when overwrite semantics is specified")
      persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersistInSubdir)
    }
  }
}
