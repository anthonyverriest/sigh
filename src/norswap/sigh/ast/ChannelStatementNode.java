package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelStatementNode extends StatementNode
{
    public final ExpressionNode expression;

    public ChannelStatementNode (Span span, Object expression) {
        super(span);
        this.expression = Util.cast(expression, ExpressionNode.class);
    }

    @Override public String contents () {
        return expression.contents();
    }
}
