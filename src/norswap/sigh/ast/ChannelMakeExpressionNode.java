package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelMakeExpressionNode extends ExpressionNode
{

    public final TypeNode type;
    public final String name = "make";
    public final IntLiteralNode buffer;

    public ChannelMakeExpressionNode (Span span, Object type, Object buffer) {
        super(span);
        this.type = Util.cast(type, TypeNode.class);

        this.buffer = buffer == null
            ? new IntLiteralNode(new Span(span.start, span.start), 1)
            : Util.cast(buffer, IntLiteralNode.class);
    }

    @Override public String contents () {
        return "make(" + type + ")";
    }

}
