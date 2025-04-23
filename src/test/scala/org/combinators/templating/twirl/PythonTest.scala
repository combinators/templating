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

import org.scalatest._
import funspec._

class PythonTest extends AnyFunSpec {
  val expected: String =
    """
      |class Foo(object):
      |    def __init__(self):
      |        self.blah = "eggs!"
      |        if true:
      |            pass
      |        else:
      |            pass
      |
      |    def test(self):
      |        if false:
      |            pass
      |        else:
      |            pass
      |
      |if __name__ == "__main__":
      |    x = new Foo()
      |    print("Spam and ")
      |    print(x.blah)""".stripMargin

  val clsName: Python = Python("Foo")
  val body: Python =
    Python("""self.blah = "eggs!"
        |if true:
        |    pass
        |else:
        |    pass""".stripMargin)
  val bodyTight: Python = Python("""if false:
      |    pass
      |else:
      |    pass""".stripMargin)
  val text: Python = Python(""""Spam and """")

  describe("Rendering a Python template") {
    val rendered = org.combinators.templating.py.PythonTemplateTest(
      clsName = clsName,
      text = text,
      body = body,
      bodyTight = bodyTight
    )
    it("should produce exactly the expected substitution") {
      assert(expected.trim == rendered.getCode.trim)
    }
  }
}
