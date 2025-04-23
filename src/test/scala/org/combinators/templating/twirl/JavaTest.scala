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

package org.combinators.templating.twirl

import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.`type`.Type
import com.github.javaparser.ast.body.{BodyDeclaration, ConstructorDeclaration, FieldDeclaration, MethodDeclaration}
import com.github.javaparser.ast.expr._
import com.github.javaparser.ast.stmt.Statement
import org.scalatest._
import funspec._
import scala.collection.immutable._

class JavaTest extends AnyFunSpec {
  val expected: String =
    """
       |import bar.Bar;
       |import foo.*;
       |import static Foo.*;
       |
       |/* This tests twirl Templating support for Java */
       |public class YYY$_ZZZ extends XXX {
       |
       |    { System.out.println("never do this in practice!"); }
       |    static {
       |      int x = 1; // I don't know
       |      for (int y = 10; y > x; y--) {
       |        System.out.println("why I do this");
       |      }
       |    }
       |
       |    // Also with single line comments
       |    /* And multi line comments
       |     * With Strings in "them"!
       |     * And lots of other //stuff
       |     */
       |    String foo = "Ei cän \" dö Ünicöð€";
       |    Hululu.Lalala[][] xoo = null;
       |
       |    static X xoo = null;
       |    Y yoo = new Yoo();
       |    Z[] zoos;
       |
       |    public static void messUpEnviornment() {
       |        some.EnvironmentClass.xoo = xoo;
       |    }
       |    /** By default do crazyness. */
       |    public YYY$_ZZZ() {
       |      System.disco();
       |    }
       |    protected YYY$_ZZZ(YYY$_ZZZ copy) {
       |      this.zoos = copy.zoos;
       |    }
       |    public YYY$_ZZZ(Z[] zs) {
       |      super(zs);
       |      System.disco();
       |    }
       |
       |
       |    public static void main(String[] args) {
       |      System.out.println("The application has crashed");
       |      System.out.println("Never run this application again");
       |    }
       |
       |    public static volatile String gargh = "GAAAAAAARRRG!!";
       |    @nobodyunderstands public int getReason() { return reason; }
       |    public int reason = 42;
       |
       |    private void hiddenEffect() {
       |      System.evil();
       |    }
       |    public void doSomethingInnocent(XXX withThisArg) {
       |      System.out.println(withThisArg);
       |      hiddenEffect(); // I break my contract }:->
       |    }
       |}
       |
       |public interface FooI {
       |    public default void doFoos(YYY$_ZZZ x) { System.out.println(x.toString()); }
       |}
    """.stripMargin

  val importDecl: ImportDeclaration = Java("import bar.Bar;").importDeclaration()
  val className: SimpleName = Java("YYY$_ZZZ").simpleName()
  val singleStatement: Statement = Java(s"""System.out.println("never do this in practice!");""").statement()
  val multiStatements: Seq[Statement] = Java(
    s"""
       |int x = 1; // I don't know
       |for (int y = 10; y > x; y--) {
       |  System.out.println("why I do this");
       |}""".stripMargin).statements()
  val someString: StringLiteralExpr = Java(""""Ei cän \" dö Ünicöð€"""").expression()
  val tpe: Type = Java("Hululu.Lalala[][]").tpe()
  val fieldDeclarations: Seq[FieldDeclaration] =
    Java(
      s"""
         |static X xoo = null;
         |Y yoo = new Yoo();
         |Z[] zoos;""".stripMargin).fieldDeclarations()
  val name: Name = Java("some.EnvironmentClass.xoo").name()
  val nameExpr: NameExpr = Java("xoo").nameExpression()
  val constructors: Seq[ConstructorDeclaration] =
    Java(
      s"""
         |/** By default do crazyness. */
         |public YYY$$_ZZZ() {
         |  System.disco();
         |}
         |protected YYY$$_ZZZ(YYY$$_ZZZ copy) {
         |  this.zoos = copy.zoos;
         |}
         |public YYY$$_ZZZ(Z[] zs) {
         |  super(zs);
         |  System.disco();
         |}""".stripMargin).constructors()
  val singleDecl: BodyDeclaration[?] =
    Java(
      s"""
         |public static void main(String[] args) {
         |  System.out.println("The application has crashed");
         |  System.out.println("Never run this application again");
         |}""".stripMargin).classBodyDeclaration()
  val multiDecls: Seq[BodyDeclaration[?]] =
    Java(
      s"""
         |public static volatile String gargh = "GAAAAAAARRRG!!";
         |@nobodyunderstands public int getReason() { return reason; }
         |public int reason = 42;""".stripMargin).classBodyDeclarations()

  val methodDecls: Seq[MethodDeclaration] =
    Java(
      s"""
         |private void hiddenEffect() {
         |      System.evil();
         |    }
         |    public void doSomethingInnocent(XXX withThisArg) {
         |      System.out.println(withThisArg);
         |      hiddenEffect(); // I break my contract }:->
         |    }""".stripMargin).methodDeclarations()
  val interfaceMethod: BodyDeclaration[?] =
    Java("public default void doFoos(YYY$_ZZZ x) { System.out.println(x.toString()); }").interfaceBodyDeclaration()

  describe("Rendering a Java template with lots of stuff and strings in it") {
    val rendered: JavaFormat.Appendable = org.combinators.templating.java.JavaTemplateTest.render(
      imp = importDecl,
      className = className,
      singleStatement = singleStatement,
      multiStatements = multiStatements,
      someString = someString,
      qualifiedName = name,
      nameExpression = nameExpr,
      singleDecl = singleDecl,
      multiDecls = multiDecls,
      fieldDeclarations = fieldDeclarations,
      methodDeclarations = methodDecls,
      constructors = constructors,
      interfaceMethod = interfaceMethod,
      tpe = tpe)
    describe("when parsing it") {
      val parsedResult = rendered.compilationUnit()
      it("should be equal to the expected result") {
        assert(Java(expected).compilationUnit().toString == parsedResult.toString)
      }
    }
  }
}
