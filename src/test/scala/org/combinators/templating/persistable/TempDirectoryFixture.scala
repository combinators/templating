package org.combinators.templating.persistable

import java.nio.file.{Files, Path}

import org.apache.commons.io.FileUtils
import org.scalatest.{Outcome, funspec}
import funspec._

trait TempDirectoryFixture { self: FixtureAnyFunSpec =>
  type FixtureParam = Path
  override def withFixture(test: OneArgTest): Outcome = {
    val tmpDir = Files.createTempDirectory("inhabitants")
    try withFixture(test.toNoArgTest(tmpDir))
    finally FileUtils.deleteDirectory(tmpDir.toFile)
  }
}
