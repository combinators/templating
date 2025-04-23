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
    Python(
      """self.blah = "eggs!"
        |if true:
        |    pass
        |else:
        |    pass""".stripMargin)
  val bodyTight: Python = Python(
    """if false:
      |    pass
      |else:
      |    pass""".stripMargin)
  val text: Python = Python(""""Spam and """")

  describe("Rendering a Python template") {
    val rendered = org.combinators.templating.py.PythonTemplateTest(
      clsName = clsName,
      text = text,
      body = body,
      bodyTight = bodyTight)
    it("should produce exactly the expected substitution") {
      assert(expected.trim == rendered.getCode.trim)
    }
  }
}
