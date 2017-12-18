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
