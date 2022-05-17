package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelOutAssignmentNode extends ExpressionNode{

    public final ReferenceNode channel;

    public ChannelOutAssignmentNode (Span span, Object channel) {
        super(span);
        this.channel = Util.cast(channel, ReferenceNode.class);
    }

    @Override
    public String contents () {
        return "Channel assignment " + channel;
    }
}
