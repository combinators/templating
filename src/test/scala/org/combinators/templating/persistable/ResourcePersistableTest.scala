package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.scalatest.FunSpec

import scala.io.Source

class ResourcePersistableTest extends FunSpec {
  val elementToPersist: BundledRessource =
    BundledRessource("res/GammaMtau.png", Paths.get("test", "path", "picture.png"), getClass)
  val persistableInstance: Persistable.Aux[BundledRessource] = ResourcePersistable.apply

  describe("Persisting a picture from the resources folder") {
    describe("in a temporary directory") {
      val tmpDir = Files.createTempDirectory("inhabitants")
      tmpDir.toFile.deleteOnExit()
      persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist).deleteOnExit()
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      it("should create a file named by its content") {
        assert(Files.exists(expectedPath))
      }
      it("should write exactly the specified contents") {
        val resourceBytes = Files.readAllBytes(Paths.get(getClass.getResource("res/GammaMtau.png").toURI))
        assert(Files.readAllBytes(expectedPath).sameElements(resourceBytes))
      }
      it("should fail to persist the same thing twice") {
        assertThrows[FileAlreadyExistsException](persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") {
        persistableInstance.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
      }
    }
  }
}