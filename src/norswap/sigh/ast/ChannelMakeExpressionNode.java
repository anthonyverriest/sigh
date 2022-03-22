package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelMakeExpressionNode extends ExpressionNode
{

    public final TypeNode type;
    public final String name = "make";

    public ChannelMakeExpressionNode (Span span, Object type) {
        super(span);
        this.type = Util.cast(type, TypeNode.class);

    }

    @Override public String contents () {
        return "make " + type;
    }

}
