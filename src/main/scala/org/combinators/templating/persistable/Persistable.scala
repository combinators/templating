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

import _root_.java.nio.file.{FileAlreadyExistsException, Files, Path}
import java.io.File

/** Type class for persistable objects (inhabitants). */
trait Persistable {

  /** The type of the object to persist */
  type T

  /** Serialized representation of the object */
  def rawText(elem: T): Array[Byte]

  /** Path where to store the object `elem` (relative to some later specified
    * root)
    */
  def path(elem: T): Path

  /** Computes the full path where to place `elem` relative to `basePath`.
    */
  def fullPath(basePath: Path, elem: T): Path = {
    basePath.resolve(path(elem))
  }

  /** Persists this object to an object dependent path under `basePath` and
    * returns the persisted file. Overwrites any pre-existing files under
    * `basePath` / `path`.
    */
  def persistOverwriting(basePath: Path, elem: T): File = {
    val fp = fullPath(basePath, elem)
    if (!Files.exists(fp.getParent))
      Files.createDirectories(fp.getParent)
    Files.write(fp, rawText(elem))
    fp.toFile
  }

  /** Persists this object to an object dependent path under `basePath` and
    * returns the persisted file. Throws an `FileAlreadyExistsException` if the
    * file already exists.
    */
  def persist(basePath: Path, elem: T): File = {
    val fp = fullPath(basePath, elem)
    if (Files.exists(fp)) throw new FileAlreadyExistsException(fp.toString)
    else persistOverwriting(basePath, elem)
  }
}

/** Helper to obtain a type class instance for persistable objects. */
object Persistable {
  type Aux[TT] = Persistable { type T = TT }

  def apply[T](implicit persistable: Aux[T]): Aux[T] = persistable
}
