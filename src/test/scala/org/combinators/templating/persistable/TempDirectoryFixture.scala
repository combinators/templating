package org.combinators.templating.persistable

import java.nio.file.{Files, Path}

import org.apache.commons.io.FileUtils
import org.scalatest.{Outcome, fixture}

trait TempDirectoryFixture { self: fixture.FunSpec =>
  type FixtureParam = Path
  def withFixture(test: OneArgTest): Outcome = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }
}
