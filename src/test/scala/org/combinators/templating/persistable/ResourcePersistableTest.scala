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

import org.scalatest._
import funspec._

class ResourcePersistableTest extends FixtureAnyFunSpec with TempDirectoryFixture with GivenWhenThen {

  val persistableInstance: Persistable.Aux[BundledResource] = ResourcePersistable.apply

  describe("Persisting a picture from the resources folder") {
    it("should create a file named by its content") { (tmpDir: Path) =>
      Given("a resource to persist")
      val elementToPersist: BundledResource =
        BundledResource("res/GammaMtau.png", Paths.get("test", "path", "picture.png"), getClass())

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
