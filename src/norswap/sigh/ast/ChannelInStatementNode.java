package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelInStatementNode extends StatementNode{

    public final ReferenceNode channel;
    public final ExpressionNode value;

    public ChannelInStatementNode (Span span, Object channel, Object value){
        super(span);
        this.channel = Util.cast(channel, ReferenceNode.class);
        this.value = Util.cast(value, ExpressionNode.class);
    }

    @Override public String contents () {
        return channel + " <- " + value;
    }
}
