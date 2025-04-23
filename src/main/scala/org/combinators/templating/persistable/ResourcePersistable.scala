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

import java.nio.file.{Files, Path, Paths}

/** A persistable resource on the classpath.
  *
  * @param name the name by which to locate the resource within the classpath.
  * @param persistTo the name of the file where to store the resource.
  * @param classToLoadResource the class which will be used to load the resource (@see
  */
case class BundledResource(name: String, persistTo: Path, classToLoadResource: Class[?] = classOf[BundledResource])

trait ResourcePersistableInstances {
  def bundledResourceInstance: ResourcePersistable.Aux = new Persistable {
    override type T = BundledResource
    override def path(elem: BundledResource): Path = elem.persistTo
    override def rawText(elem: BundledResource): Array[Byte] =
      Files.readAllBytes(Paths.get(elem.classToLoadResource.getResource(elem.name).toURI))
  }
}

object ResourcePersistable extends ResourcePersistableInstances {
  type Aux = Persistable { type T = BundledResource }
  implicit def apply: Aux = bundledResourceInstance
}
