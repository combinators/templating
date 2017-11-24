@(clsName: Python, text: Python, body: Python, bodyTight: Python)
class @{clsName}(object):
    def __init__(self):
        @body.indentExceptFirst.indentExceptFirst

    def test(self):
@bodyTight.indent.indent

if __name__ == "__main__":
    x = new @{clsName}()
    print(@text)
    print(x.blah)