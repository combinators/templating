package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.scalatest._

class PersistableTest extends FunSpec {
  val persistable: Persistable.Aux[String] = new Persistable {
    override type T = String
    override def rawText(elem: T): Array[Byte] = elem.getBytes
    override def path(elem: T): Path = Paths.get(elem)
  }

  describe("Persisting a String to a file named by its content") {
    describe("in a temporary directory") {
      val tmpDir = Files.createTempDirectory("inhabitants")
      tmpDir.toFile.deleteOnExit()
      val elementToPersist = "test"
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist).deleteOnExit()
      val expectedPath = Paths.get(tmpDir.toAbsolutePath.toString, elementToPersist)
      it("should create a file named by its content") {
        assert(Files.exists(expectedPath))
      }
      it("should write exactly the specified contents") {
        assert(Files.readAllBytes(expectedPath).sameElements(elementToPersist.getBytes))
      }
      it("should fail to persist the same thing twice") {
        assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersist))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") {
        persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
      }
    }
  }
}
