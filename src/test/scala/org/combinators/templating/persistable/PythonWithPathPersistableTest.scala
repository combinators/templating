/*
 * Copyright 2017-2025 Jan Bessai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.combinators.templating.persistable

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

import PythonWithPathPersistable._
import org.combinators.templating.twirl.Python
import org.scalatest._
import funspec._

class PythonWithPathPersistableTest
    extends FixtureAnyFunSpec
    with TempDirectoryFixture
    with GivenWhenThen {
  val persistable: PythonWithPathPersistable.Aux[PythonWithPath] =
    PythonWithPathPersistable.apply

  describe("Persisting a piece of Python code to a file") {
    it("should create a file named by its content") { (tmpDir: Path) =>
      Given("an element to persist")
      val elementToPersist: PythonWithPath =
        PythonWithPath(
          Python(s"""class Foo:
                    |    pass
                """.stripMargin),
          Paths.get("bar/test.py")
        )

      When("persisting it")
      persistable.persist(tmpDir.toAbsolutePath, elementToPersist)

      Then("its corresponding file should exist")
      val expectedPath =
        tmpDir.toAbsolutePath.resolve(elementToPersist.persistTo)
      assert(Files.exists(expectedPath))

      And("the file should have exactly the specified contents")
      assert(
        Files
          .readAllBytes(expectedPath)
          .sameElements(elementToPersist.code.getCode.getBytes)
      )

      And("trying to persist the same element again should raise an exception")
      assertThrows[FileAlreadyExistsException](
        persistable.persist(tmpDir.toAbsolutePath, elementToPersist)
      )

      Then(
        "it should not produce the exception when overwrite semantics is specified"
      )
      persistable.persistOverwriting(tmpDir.toAbsolutePath, elementToPersist)
    }
  }
}
