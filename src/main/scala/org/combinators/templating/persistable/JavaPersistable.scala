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

import java.nio.file.Paths

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.{Name, NameExpr}
import com.github.javaparser.ast.visitor.GenericVisitorAdapter

import scala.collection.immutable._
import scala.collection.compat._
import scala.jdk.CollectionConverters._

trait JavaPersistableInstances {
  /** Persistable instance for a compilation unit.
    * Derives path and file names from the package and the name of the first declared type.
    */
  implicit def compilationUnitInstance: JavaPersistable.Aux[CompilationUnit] =
    new Persistable {
      type T = CompilationUnit
      override def rawText(compilationUnit: CompilationUnit) = compilationUnit.toString.getBytes
      override def path(compilationUnit: CompilationUnit) = {
        val pkg: Seq[String] =
          compilationUnit.getPackageDeclaration.orElse(null) match {
            case null => Seq.empty
            case somePkg =>
              somePkg.accept(new GenericVisitorAdapter[Seq[String], Unit] {
                override def visit(name: NameExpr, arg: Unit): Seq[String] = Seq(name.getNameAsString)
                override def visit(name: Name, arg: Unit): Seq[String] =
                  Option(name.getQualifier.orElse(null))
                    .map((q: Name) => q.accept(this, arg))
                    .getOrElse(Seq.empty[String]) :+ name.getIdentifier
              },
                ()
              )
          }
        val clsName = s"${compilationUnit.getTypes.asScala.head.getName}.java"
        val fullPath = "src" +: "main" +: "java" +: pkg :+ clsName
        Paths.get(fullPath.head, fullPath.tail : _*)
      }
    }
}

object JavaPersistable extends JavaPersistableInstances {
  type Aux[TT] = Persistable { type T = TT }
  def apply[T](implicit persistable: Aux[T]): Aux[T] = persistable
}