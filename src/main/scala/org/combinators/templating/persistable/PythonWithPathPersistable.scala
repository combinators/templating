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
