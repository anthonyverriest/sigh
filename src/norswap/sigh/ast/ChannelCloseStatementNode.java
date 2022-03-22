package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelCloseStatementNode extends StatementNode
{

    public final ReferenceNode channel;
    public final String name = "close";

    @SuppressWarnings("unchecked")
    public ChannelCloseStatementNode
        (Span span, Object type) {
        super(span);
        this.channel = Util.cast(type, ReferenceNode.class);

    }

    @Override public String contents () {
        return "close " + channel;
    }

}
