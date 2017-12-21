package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import PythonWithPathPersistable._
import org.apache.commons.io.FileUtils
import org.combinators.templating.twirl.Python
import org.scalatest._

class PythonWithPathPersistableTest extends fixture.FunSpec {
  val persistable: PythonWithPathPersistable.Aux[PythonWithPath] = PythonWithPathPersistable.apply
  val elementToPersist: PythonWithPath =
    PythonWithPath(
      Python(s"""class Foo:
                |    pass
                """.stripMargin),
      Paths.get("bar/test.py"))

  type FixtureParam = Path
  def withFixture(test: OneArgTest) = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }

  describe("Persisting a piece of Python code to a file") {
    describe("in a temporary directory") { tmpDir: Path =>
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist)
      val expectedPath = tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      it("should create a file named by its content") { tmpDir: Path =>
        assert(Files.exists(expectedPath))
      }
      it("should write exactly the specified contents") { tmpDir: Path =>
        assert(Files.readAllBytes(expectedPath).sameElements(elementToPersist.code.getCode.getBytes))
      }
      it("should fail to persist the same thing twice") { tmpDir: Path =>
        assertThrows[FileAlreadyExistsException](persistable.persist(tmpDir.toAbsolutePath, elementToPersist))
      }
      it("should not fail to persist the same thing twice when overwriting semantics is expected") { tmpDir: Path =>
        persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
      }
    }
  }
}
