package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.scalatest._

class ResourcePersistableTest extends fixture.FunSpec with TempDirectoryFixture with GivenWhenThen {

  val persistableInstance: Persistable.Aux[BundledResource] = ResourcePersistable.apply

  describe("Persisting a picture from the resources folder") {
    it("should create a file named by its content") { tmpDir: Path =>
      Given("a resource to persist")
      val elementToPersist: BundledResource =
        BundledResource("res/GammaMtau.png", Paths.get("test", "path", "picture.png"), getClass)

      When("persisting it")
      persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist)

      Then("its corresponding file should exist")
      val expectedPath: Path = tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      assert(Files.exists(expectedPath))

      And("the file should have exactly the specified contents")
      val resourceBytes = Files.readAllBytes(Paths.get(getClass.getResource("res/GammaMtau.png").toURI))
      assert(Files.readAllBytes(expectedPath).sameElements(resourceBytes))

      And("trying to persist the same element again should raise an exception")
      assertThrows[FileAlreadyExistsException](persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist))

      Then("it should not produce the exception when overwrite semantics is specified")
      persistableInstance.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
    }
  }
}