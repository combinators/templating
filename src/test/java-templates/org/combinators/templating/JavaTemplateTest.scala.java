@(imp: ImportDeclaration,
    className: SimpleName,
    singleStatement: Statement,
    multiStatements: Seq[Statement],
    someString: Expression,
    qualifiedName: Name,
    nameExpression: NameExpr,
    singleDecl: BodyDeclaration[_],
    multiDecls: Seq[BodyDeclaration[_]],
    fieldDeclarations: Seq[FieldDeclaration],
    methodDeclarations: Seq[MethodDeclaration],
    constructors: Seq[ConstructorDeclaration],
    interfaceMethod: BodyDeclaration[_],
    tpe: Type)

@Java(imp)
import foo.*;
import static Foo.*;

/* This tests twirl Templating support for Java */
public class @Java(className) extends XXX {

    { @Java(singleStatement) }
    static { @Java(multiStatements) }

    // Also with single line comments
    /* And multi line comments
     * With Strings in "them"!
     * And lots of other //stuff
     */
    String foo = @Java(someString);
    @Java(tpe) xoo = null;
    @Java(fieldDeclarations)

    public static void messUpEnviornment() {
        @Java(qualifiedName) = @Java(nameExpression);
    }
    @Java(constructors)

    @Java(singleDecl)
    @Java(multiDecls)
    @Java(methodDeclarations)
}

public interface FooI {
    @Java(interfaceMethod)
}