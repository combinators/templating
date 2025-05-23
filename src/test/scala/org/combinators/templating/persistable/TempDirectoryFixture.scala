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

import java.nio.file.{Files, Path}

import org.apache.commons.io.FileUtils
import org.scalatest.{funspec, Outcome}
import funspec._

trait TempDirectoryFixture { self: FixtureAnyFunSpec =>
  type FixtureParam = Path
  override def withFixture(test: OneArgTest): Outcome = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }
}
