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

import java.nio.file.Path

import org.combinators.templating.twirl.Python

/** A python fragment to be stored at `persistTo`. */
case class PythonWithPath(code: Python, persistTo: Path)

trait PythonWithPathPersistableInstances {
  /** Persistable instance for [PythonWithPath]. */
  implicit def pythonWithPathPersistable: PythonWithPathPersistable.Aux[PythonWithPath] = new Persistable {
    type T = PythonWithPath
    def rawText(elem: PythonWithPath): Array[Byte] = elem.code.getCode.getBytes
    def path(elem: PythonWithPath): Path = elem.persistTo
  }
}

object PythonWithPathPersistable extends PythonWithPathPersistableInstances {
  type Aux[TT] = Persistable { type T = TT }
  def apply[T](implicit persistable: Aux[T]): Aux[T] = persistable
}
