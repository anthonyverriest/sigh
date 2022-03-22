package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

public class ChannelCloseStatementNode extends StatementNode
{

    public final ReferenceNode ref;
    public final String name = "close";

    @SuppressWarnings("unchecked")
    public ChannelCloseStatementNode
        (Span span, Object type) {
        super(span);
        this.ref = Util.cast(type, ReferenceNode.class);

    }

    @Override public String contents () {
        return "close " + ref;
    }

}
