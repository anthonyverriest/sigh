import norswap.autumn.AutumnTestFixture;
import norswap.autumn.positions.LineMapString;
import norswap.sigh.SemanticAnalysis;
import norswap.sigh.SighGrammar;
import norswap.sigh.ast.SighNode;
import norswap.uranium.Reactor;
import norswap.uranium.UraniumTestFixture;
import norswap.utils.visitors.Walker;
import org.testng.annotations.Test;

/**
 * NOTE(norswap): These tests were derived from the {@link InterpreterTests} and don't test anything
 * more, but show how to idiomatically test semantic analysis. using {@link UraniumTestFixture}.
 */
public final class SemanticAnalysisTests extends UraniumTestFixture
{
    // ---------------------------------------------------------------------------------------------

    private final SighGrammar grammar = new SighGrammar();
    private final AutumnTestFixture autumnFixture = new AutumnTestFixture();

    {
        autumnFixture.rule = grammar.root();
        autumnFixture.runTwice = false;
        autumnFixture.bottomClass = this.getClass();
    }

    private String input;

    @Override protected Object parse (String input) {
        this.input = input;
        return autumnFixture.success(input).topValue();
    }

    @Override protected String astNodeToString (Object ast) {
        LineMapString map = new LineMapString("<test>", input);
        return ast.toString() + " (" + ((SighNode) ast).span.startString(map) + ")";
    }

    // ---------------------------------------------------------------------------------------------

    @Override protected void configureSemanticAnalysis (Reactor reactor, Object ast) {
        Walker<SighNode> walker = SemanticAnalysis.createWalker(reactor);
        walker.walk(((SighNode) ast));
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testLiteralsAndUnary() {
        successInput("return 42");
        successInput("return 42.0");
        successInput("return \"hello\"");
        successInput("return (42)");
        successInput("return [1, 2, 3]");
        successInput("return true");
        successInput("return false");
        successInput("return null");
        successInput("return !false");
        successInput("return !true");
        successInput("return !!true");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testNumericBinary() {
        successInput("return 1 + 2");
        successInput("return 2 - 1");
        successInput("return 2 * 3");
        successInput("return 2 / 3");
        successInput("return 3 / 2");
        successInput("return 2 % 3");
        successInput("return 3 % 2");

        successInput("return 1.0 + 2.0");
        successInput("return 2.0 - 1.0");
        successInput("return 2.0 * 3.0");
        successInput("return 2.0 / 3.0");
        successInput("return 3.0 / 2.0");
        successInput("return 2.0 % 3.0");
        successInput("return 3.0 % 2.0");

        successInput("return 1 + 2.0");
        successInput("return 2 - 1.0");
        successInput("return 2 * 3.0");
        successInput("return 2 / 3.0");
        successInput("return 3 / 2.0");
        successInput("return 2 % 3.0");
        successInput("return 3 % 2.0");

        successInput("return 1.0 + 2");
        successInput("return 2.0 - 1");
        successInput("return 2.0 * 3");
        successInput("return 2.0 / 3");
        successInput("return 3.0 / 2");
        successInput("return 2.0 % 3");
        successInput("return 3.0 % 2");

        failureInputWith("return 2 + true", "Trying to add Int with Bool");
        failureInputWith("return true + 2", "Trying to add Bool with Int");
        failureInputWith("return 2 + [1]", "Trying to add Int with Int[]");
        failureInputWith("return [1] + 2", "Trying to add Int[] with Int");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testOtherBinary() {
        successInput("return true && false");
        successInput("return false && true");
        successInput("return true && true");
        successInput("return true || false");
        successInput("return false || true");
        successInput("return false || false");

        failureInputWith("return false || 1",
            "Attempting to perform binary logic on non-boolean type: Int");
        failureInputWith("return 2 || true",
            "Attempting to perform binary logic on non-boolean type: Int");

        successInput("return 1 + \"a\"");
        successInput("return \"a\" + 1");
        successInput("return \"a\" + true");

        successInput("return 1 == 1");
        successInput("return 1 == 2");
        successInput("return 1.0 == 1.0");
        successInput("return 1.0 == 2.0");
        successInput("return true == true");
        successInput("return false == false");
        successInput("return true == false");
        successInput("return 1 == 1.0");

        failureInputWith("return true == 1", "Trying to compare incomparable types Bool and Int");
        failureInputWith("return 2 == false", "Trying to compare incomparable types Int and Bool");

        successInput("return \"hi\" == \"hi\"");
        successInput("return [1] == [1]");

        successInput("return 1 != 1");
        successInput("return 1 != 2");
        successInput("return 1.0 != 1.0");
        successInput("return 1.0 != 2.0");
        successInput("return true != true");
        successInput("return false != false");
        successInput("return true != false");
        successInput("return 1 != 1.0");

        failureInputWith("return true != 1", "Trying to compare incomparable types Bool and Int");
        failureInputWith("return 2 != false", "Trying to compare incomparable types Int and Bool");

        successInput("return \"hi\" != \"hi\"");
        successInput("return [1] != [1]");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testVarDecl() {
        successInput("var x: Int = 1; return x");
        successInput("var x: Float = 2.0; return x");

        successInput("var x: Int = 0; return x = 3");
        successInput("var x: String = \"0\"; return x = \"S\"");

        failureInputWith("var x: Int = true", "expected Int but got Bool");
        failureInputWith("return x + 1", "Could not resolve: x");
        failureInputWith("return x + 1; var x: Int = 2", "Variable used before declaration: x");

        // implicit conversions
        successInput("var x: Float = 1 ; x = 2");

        /* VIBE */
        successInput("var x : ChanInt = null");
        successInput("var x : ChanString = null");
        successInput("var x : ChanFloat = null");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testRootAndBlock () {
        successInput("return");
        successInput("return 1");
        successInput("return 1; return 2");

        successInput("print(\"a\")");
        successInput("print(\"a\" + 1)");
        successInput("print(\"a\"); print(\"b\")");

        successInput("{ print(\"a\"); print(\"b\") }");

        successInput(
            "var x: Int = 1;" +
            "{ print(\"\" + x); var x: Int = 2; print(\"\" + x) }" +
            "print(\"\" + x)");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testCalls() {
        successInput(
            "fun add (a: Int, b: Int): Int { return a + b } " +
            "return add(4, 7)");

        successInput(
            "struct Point { var x: Int; var y: Int }" +
            "return $Point(1, 2)");

        successInput("var str: String = null; return print(str + 1)");

        failureInputWith("return print(1)", "argument 0: expected String but got Int");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testArrayStructAccess() {
        successInput("return [1][0]");
        successInput("return [1.0][0]");
        successInput("return [1, 2][1]");

        failureInputWith("return [1][true]", "Indexing an array using a non-Int-valued expression");

        // TODO make this legal?
        // successInput("[].length", 0L);

        successInput("return [1].length");
        successInput("return [1, 2].length");

        successInput("var array: Int[] = null; return array[0]");
        successInput("var array: Int[] = null; return array.length");

        successInput("var x: Int[] = [0, 1]; x[0] = 3; return x[0]");
        successInput("var x: Int[] = []; x[0] = 3; return x[0]");
        successInput("var x: Int[] = null; x[0] = 3");

        successInput(
            "struct P { var x: Int; var y: Int }" +
            "return $P(1, 2).y");

        successInput(
            "struct P { var x: Int; var y: Int }" +
            "var p: P = null;" +
            "return p.y");

        successInput(
            "struct P { var x: Int; var y: Int }" +
            "var p: P = $P(1, 2);" +
            "p.y = 42;" +
            "return p.y");

        successInput(
            "struct P { var x: Int; var y: Int }" +
            "var p: P = null;" +
            "p.y = 42");

        failureInputWith(
            "struct P { var x: Int; var y: Int }" +
            "return $P(1, true)",
            "argument 1: expected Int but got Bool");

        failureInputWith(
            "struct P { var x: Int; var y: Int }" +
            "return $P(1, 2).z",
            "Trying to access missing field z on struct P");
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void testIfWhile () {
        successInput("if (true) return 1 else return 2");
        successInput("if (false) return 1 else return 2");
        successInput("if (false) return 1 else if (true) return 2 else return 3 ");
        successInput("if (false) return 1 else if (false) return 2 else return 3 ");

        successInput("var i: Int = 0; while (i < 3) { print(\"\" + i); i = i + 1 } ");

        failureInputWith("if 1 return 1",
            "If statement with a non-boolean condition of type: Int");
        failureInputWith("while 1 return 1",
            "While statement with a non-boolean condition of type: Int");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testInference() {
        successInput("var array: Int[] = []");
        successInput("var array: String[] = []");
        successInput("fun use_array (array: Int[]) {} ; use_array([])");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testTypeAsValues() {
        successInput("struct S{} ; return \"\"+ S");
        successInput("struct S{} ; var type: Type = S ; return \"\"+ type");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testUnconditionalReturn()
    {
        successInput("fun f(): Int { if (true) return 1 else return 2 } ; return f()");

        // TODO: would be nice if this pinpointed the if-statement as missing the return,
        //   not the whole function declaration
        failureInputWith("fun f(): Int { if (true) return 1 } ; return f()",
            "Missing return in function");
    }

    /* VIBE */
    // ---------------------------------------------------------------------------------------------

    @Test public void testMake()
    {
        /* VIBE */
        successInput("var x : ChanInt = make(ChanInt)");
        successInput("var x : ChanString = make(ChanString)");
        successInput("var x : ChanFloat = make(ChanFloat)");

        successInput("var x : ChanInt = make(ChanInt, 5)");
        successInput("var x : ChanString = make(ChanString, 1)");
        successInput("var x : ChanFloat = make(ChanFloat, 5)");

        failureInput("var x : ChanString = make(ChanString, \"hello\")");
        failureInput("var x : ChanFloat = make(ChanFloat, null)");
        failureInput("var x : ChanFloat = make(ChanFloat, 5.0)");

        // Edge cases
        failureInputWith("var x : ChanInt = make(ChanInt, -1)", "only positive integer in buffer");
        failureInputWith("var x : ChanInt = make(ChanInt, 0)", "only positive integer in buffer");

        failureInputWith("var x : Int = 6 + make(ChanInt)",
            "Could not resolve: make");

        failureInputWith("var x : Int = make(Int)",
            "invalid type passed to function make");
        failureInputWith("var x : String = make(String)",
            "invalid type passed to function make");
        failureInputWith("var x : Float = make(Float)",
            "invalid type passed to function make");

        failureInputWith("var x : Int = make(ChanInt)",
            "expected Int but got ChanInt");
        failureInputWith("var x : String = make(ChanString)",
            "expected String but got ChanString");
        failureInputWith("var x : Float = make(ChanFloat)",
            "expected Float but got ChanFloat");

        failureInputWith("var x : ChanInt = 0",
            "expected ChanInt but got Int");
        failureInputWith("var x : ChanString = \"\"",
            "expected ChanString but got String");
        failureInputWith("var x : ChanFloat = 0.0",
            "expected ChanFloat but got Float");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testClose()
    {
        /* VIBE */
        successInput("var x : ChanString = make(ChanString) ; close(x)");
        successInput("var x : ChanInt = make(ChanInt) ; close(x)");
        successInput("var x : ChanFloat = make(ChanFloat) ; close(x)");

        // Edge cases
        failureInput("close(8.0)");
        failureInput("close(null)");
        failureInput("close(3)");
        failureInput("close(\"h\")");

        failureInputWith("var x : Int = 3 ; close(x)",
            "invalid type passed to function close");
        failureInputWith("var x : Float = 3.0 ; close(x)",
            "invalid type passed to function close");
        failureInputWith("var x : String = \"hey\" ; close(x)",
            "invalid type passed to function close");
    }

    @Test public void testSend(){
        /* VIBE */
        successInput("var x : ChanString = make(ChanString) ; x <- \"hello\" ; close(x)");
        successInput("var x : ChanInt = make(ChanInt) ; x <- 4 ; close(x)");
        successInput("var x : ChanFloat = make(ChanFloat) ; x <- 9.0 ; close(x)");

        failureInputWith("var x : ChanString = make(ChanString) ; x <- 4 ; close(x)",
            "invalid type passed to channel : ChanString <- Int");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testReceive(){
        /* VIBE */
        successInput("var x : ChanInt = make(ChanInt) ; x <- 4 ; var z: Int = <-x ; close(x)");
        successInput("var x : ChanFloat = make(ChanFloat) ; x <- 4.0 ; var z: Float = <-x ; close(x)");
        successInput("var x : ChanString = make(ChanString) ; x <- \"h\" ; var z: String = <-x ; close(x)");

        failureInputWith("var x : ChanInt = make(ChanInt) ; var z: Int = 5 ; x <- 4 ; var w: Int = <-z ; close(x)", "invalid type: Int");

        failureInputWith("var x : ChanInt = make(ChanInt) ; x <- 4 ; var z: String = <-x ; close(x)", "expected String but got Int");
        failureInputWith("var x : ChanFloat = make(ChanFloat) ; x <- 4.0 ; var z: String = <-x ; close(x)", "expected String but got Float");
        failureInputWith("var x : ChanString = make(ChanString) ; x <- \"h\" ; var z: Int = <-x ; close(x)", "expected Int but got String");
    }

    @Test public void testRoutine(){
        /* VIBE */
        successInput("fun f(): Void { print(\"void\") } ; routine f()");
        successInput("fun f(msg: String) { print(\"void\") } ; routine f(\"arg\")");

        failureInputWith("fun f(): Int { return 1 } ; routine f()", "routine only supports void functions");
    }
}
