package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import org.apache.commons.io.FileUtils
import org.scalatest._

class ResourcePersistableTest extends fixture.FunSpec {
  val elementToPersist: BundledResource =
    BundledResource("res/GammaMtau.png", Paths.get("test", "path", "picture.png"), getClass)
  val persistableInstance: Persistable.Aux[BundledResource] = ResourcePersistable.apply

  type FixtureParam = Path
  def withFixture(test: OneArgTest) = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }

  describe("Persisting a picture from the resources folder") { tmpDir: Path =>
    describe("in a temporary directory") { tmpDir: Path =>
      persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist)
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      it("should create a file named by its content") { tmpDir: Path =>
        assert(Files.exists(expectedPath))
      }
      it("should write exactly the specified contents") { tmpDir: Path =>
        val resourceBytes = Files.readAllBytes(Paths.get(getClass.getResource("res/GammaMtau.png").toURI))
        assert(Files.readAllBytes(expectedPath).sameElements(resourceBytes))
      }
      it("should fail to persist the same thing twice") { tmpDir: Path =>
        assertThrows[FileAlreadyExistsException](persistableInstance.persist(tmpDir.toAbsolutePath, elementToPersist))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") { tmpDir: Path =>
        persistableInstance.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
      }
    }
  }
}