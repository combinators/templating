/*
 * Copyright 2017 Jan Bessai
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

import java.io.InputStream
import java.nio.file.{Files, Path, Paths}

import scala.io.Source


case class BundledRessource(name: String, persistTo: Path, classToLoadResource: Class[_] = getClass)

trait ResourcePersistableInstances {
  def bundledResourceInstance: ResourcePersistable.Aux = new Persistable {
    override type T = BundledRessource
    override def path(elem: BundledRessource): Path = elem.persistTo
    override def rawText(elem: BundledRessource): Array[Byte] =
      Files.readAllBytes(Paths.get(elem.classToLoadResource.getResource(elem.name).toURI))
  }
}

object ResourcePersistable extends ResourcePersistableInstances {
  type Aux = Persistable { type T = BundledRessource }
  implicit def apply: Aux = bundledResourceInstance
}
