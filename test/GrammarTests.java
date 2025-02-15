import norswap.autumn.AutumnTestFixture;
import norswap.sigh.SighGrammar;
import norswap.sigh.ast.*;
import norswap.sigh.types.ChanIntType;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;
import static norswap.sigh.ast.BinaryOperator.*;

public class GrammarTests extends AutumnTestFixture {
    // ---------------------------------------------------------------------------------------------

    private final SighGrammar grammar = new SighGrammar();
    private final Class<?> grammarClass = grammar.getClass();

    // ---------------------------------------------------------------------------------------------

    private static IntLiteralNode intlit (long i) {
        return new IntLiteralNode(null, i);
    }

    private static FloatLiteralNode floatlit (double d) {
        return new FloatLiteralNode(null, d);
    }

    private static StringLiteralNode stringLit (String s) {
        return new StringLiteralNode(null, s);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void testLiteralsAndUnary () {
        rule = grammar.expression;

        successExpect("42", intlit(42));
        successExpect("42.0", floatlit(42d));
        successExpect("\"hello\"", new StringLiteralNode(null, "hello"));
        successExpect("(42)", new ParenthesizedNode(null, intlit(42)));
        successExpect("[1, 2, 3]", new ArrayLiteralNode(null, asList(intlit(1), intlit(2), intlit(3))));
        successExpect("true", new ReferenceNode(null, "true"));
        successExpect("false", new ReferenceNode(null, "false"));
        successExpect("null", new ReferenceNode(null, "null"));
        successExpect("!false", new UnaryExpressionNode(null, UnaryOperator.NOT, new ReferenceNode(null, "false")));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void testNumericBinary () {
        successExpect("1 + 2", new BinaryExpressionNode(null, intlit(1), ADD, intlit(2)));
        successExpect("2 - 1", new BinaryExpressionNode(null, intlit(2), SUBTRACT,  intlit(1)));
        successExpect("2 * 3", new BinaryExpressionNode(null, intlit(2), MULTIPLY, intlit(3)));
        successExpect("2 / 3", new BinaryExpressionNode(null, intlit(2), DIVIDE, intlit(3)));
        successExpect("2 % 3", new BinaryExpressionNode(null, intlit(2), REMAINDER, intlit(3)));

        successExpect("1.0 + 2.0", new BinaryExpressionNode(null, floatlit(1), ADD, floatlit(2)));
        successExpect("2.0 - 1.0", new BinaryExpressionNode(null, floatlit(2), SUBTRACT, floatlit(1)));
        successExpect("2.0 * 3.0", new BinaryExpressionNode(null, floatlit(2), MULTIPLY, floatlit(3)));
        successExpect("2.0 / 3.0", new BinaryExpressionNode(null, floatlit(2), DIVIDE, floatlit(3)));
        successExpect("2.0 % 3.0", new BinaryExpressionNode(null, floatlit(2), REMAINDER, floatlit(3)));

        successExpect("2 * (4-1) * 4.0 / 6 % (2+1)", new BinaryExpressionNode(null,
            new BinaryExpressionNode(null,
                new BinaryExpressionNode(null,
                    new BinaryExpressionNode(null,
                        intlit(2),
                        MULTIPLY,
                        new ParenthesizedNode(null, new BinaryExpressionNode(null,
                            intlit(4),
                            SUBTRACT,
                            intlit(1)))),
                    MULTIPLY,
                    floatlit(4d)),
                DIVIDE,
                intlit(6)),
            REMAINDER,
            new ParenthesizedNode(null, new BinaryExpressionNode(null,
                intlit(2),
                ADD,
                intlit(1)))));
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testArrayStructAccess () {
        rule = grammar.expression;
        successExpect("[1][0]", new ArrayAccessNode(null,
            new ArrayLiteralNode(null, asList(intlit(1))), intlit(0)));
        successExpect("[1].length", new FieldAccessNode(null,
            new ArrayLiteralNode(null, asList(intlit(1))), "length"));
        successExpect("p.x", new FieldAccessNode(null, new ReferenceNode(null, "p"), "x"));
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testDeclarations() {
        rule = grammar.statement;

        successExpect("var x: Int = 1", new VarDeclarationNode(null,
            "x", new SimpleTypeNode(null, "Int"), intlit(1)));

        successExpect("struct P {}", new StructDeclarationNode(null, "P", asList()));

        successExpect("struct P { var x: Int; var y: Int }",
            new StructDeclarationNode(null, "P", asList(
                new FieldDeclarationNode(null, "x", new SimpleTypeNode(null, "Int")),
                new FieldDeclarationNode(null, "y", new SimpleTypeNode(null, "Int")))));

        successExpect("fun f (x: Int): Int { return 1 }",
            new FunDeclarationNode(null, "f",
                asList(new ParameterNode(null, "x", new SimpleTypeNode(null, "Int"))),
                new SimpleTypeNode(null, "Int"),
                new BlockNode(null, asList(new ReturnNode(null, intlit(1))))));

        /* VIBE */
        successExpect("var x: ChanInt = make(ChanInt)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanInt"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanInt"), new IntLiteralNode(null, 1))));
        successExpect("var x: ChanFloat = make(ChanFloat)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanFloat"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanFloat"), new IntLiteralNode(null, 1))));
        successExpect("var x: ChanString = make(ChanString)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanString"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanString"), new IntLiteralNode(null, 1))));

        successExpect("var x: ChanInt = make(ChanInt, 3)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanInt"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanInt"), new IntLiteralNode(null, 3))));
        successExpect("var x: ChanFloat = make(ChanFloat, 3)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanFloat"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanFloat"), new IntLiteralNode(null, 3))));
        successExpect("var x: ChanString = make(ChanString, 3)", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "ChanString"), new ChannelMakeExpressionNode(null, new SimpleTypeNode(null, "ChanString"), new IntLiteralNode(null, 3))));


        failure("var x: ChanInt = make(ChanInt) + 3");
        failure("var x : ChanInt[] = make(chanInt[])");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testStatements() {
        rule = grammar.statement;

        successExpect("return", new ReturnNode(null, null));
        successExpect("return 1", new ReturnNode(null, intlit(1)));
        successExpect("print(1)", new ExpressionStatementNode(null,
            new FunCallNode(null, new ReferenceNode(null, "print"), asList(intlit(1)))));
        successExpect("{ return }", new BlockNode(null, asList(new ReturnNode(null, null))));


        successExpect("if true return 1 else return 2", new IfNode(null, new ReferenceNode(null, "true"),
            new ReturnNode(null, intlit(1)),
            new ReturnNode(null, intlit(2))));

        successExpect("if false return 1 else if true return 2 else return 3 ",
            new IfNode(null, new ReferenceNode(null, "false"),
                new ReturnNode(null, intlit(1)),
                new IfNode(null, new ReferenceNode(null, "true"),
                    new ReturnNode(null, intlit(2)),
                    new ReturnNode(null, intlit(3)))));

        successExpect("while 1 < 2 { return } ", new WhileNode(null,
            new BinaryExpressionNode(null, intlit(1), LOWER, intlit(2)),
            new BlockNode(null, asList(new ReturnNode(null, null)))));


        /* VIBE */
        successExpect("close(x)", new ChannelCloseStatementNode(null, new ReferenceNode(null, "x")));
    }

    @Test public void testSend(){
        rule = grammar.statement;

        successExpect("fizz <- 4", new ChannelInStatementNode(null, new ReferenceNode(null, "fizz"), intlit(4)));
        successExpect("fizz <- \"hey\"", new ChannelInStatementNode(null, new ReferenceNode(null, "fizz"), stringLit("hey")));
        successExpect("fizz <- 9.0", new ChannelInStatementNode(null, new ReferenceNode(null, "fizz"), floatlit(9.0)));

        failure("fizz <- 3 + 5");
        failure("fizz <- \"hey\" + 5");
        failure("fizz <- buzz <- 4");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testReceive(){
        /* VIBE */
        rule = grammar.statement;

        successExpect("var x: Int = <-fizz", new VarDeclarationNode(null, "x", new SimpleTypeNode(null, "Int"), new ChannelOutAssignmentNode(null, new ReferenceNode(null, "fizz"))));
        successExpect("x = <-fizz", new ExpressionStatementNode(null, new AssignmentNode(null, new ReferenceNode(null, "x"), new ChannelOutAssignmentNode(null, new ReferenceNode(null, "fizz")))));

        failure("var x : ChanInt = make(ChanInt) ; x <- 4 ; var z: Int = 5 + <-x ; close(x)");
        failure("var x : ChanInt = make(ChanInt) ; x <- 4 ; var z: Int = <-x - 9 ; close(x)");
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void testRoutine(){
        /* VIBE */
        rule = grammar.statement;

        successExpect("routine f(1)", new RoutineFunCallNode(null, new FunCallNode(null, new ReferenceNode(null, "f"), asList(intlit(1)))));

        failure("var x: Int = routine f(1)");
        failure("routine f");
    }
}
