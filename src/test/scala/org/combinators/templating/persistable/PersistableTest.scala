package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.apache.commons.io.FileUtils
import org.scalatest._

class PersistableTest extends fixture.FunSpec {
  val persistable: Persistable.Aux[String] = new Persistable {
    override type T = String
    override def rawText(elem: T): Array[Byte] = elem.getBytes
    override def path(elem: T): Path = Paths.get(elem)
  }

  type FixtureParam = Path
  def withFixture(test: OneArgTest) = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }

  describe("Persisting a String to a file named by its content") { tmpDir: Path =>

    describe("in a temporary directory") { tmpDir: Path =>
      val elementToPersist = "test"
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist)
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist)
      it("should create a file named by its content") { tmpDir: Path =>
        assert(Files.exists(expectedPath))
      }
      it("should write exactly the specified contents") { tmpDir: Path =>
        assert(Files.readAllBytes(expectedPath).sameElements(elementToPersist.getBytes))
      }
      it("should fail to persist the same thing twice") { tmpDir: Path =>
        assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersist))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") { tmpDir: Path =>
        persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
      }
    }

    describe("in a subdirectory of a temporary directory") { tmpDir: Path =>
      val elementToPersistInSubdir = Paths.get("foo", "test").toString
      persistable.persist(tmpDir.toAbsolutePath, elementToPersistInSubdir)
      val expectedSubdir = tmpDir.toAbsolutePath.resolve("foo")

      it("should create a subdirectory when the content specifies one") { tmpDir: Path =>
        assert(Files.exists(expectedSubdir))
        assert(Files.isDirectory(expectedSubdir))
        expectedSubdir.toFile
      }
      val expecedFileInSubdir = expectedSubdir.resolve("test")
      it("should create the file in that subdir") { tmpDir: Path =>
        assert(Files.exists(expecedFileInSubdir))
      }
      it("should write exactly the specified contents") { tmpDir: Path =>
        assert(Files.readAllBytes(expecedFileInSubdir).sameElements(elementToPersistInSubdir.getBytes))
      }
      it("should fail to persist the same thing twice") { tmpDir: Path =>
        assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersistInSubdir))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") { tmpDir: Path =>
        persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersistInSubdir)
      }
    }
  }
}
