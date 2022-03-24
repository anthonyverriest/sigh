package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;
import java.util.List;

public final class RoutineFunCallNode extends StatementNode
{
    public final String name = "routine";
    public final FunCallNode function;

    @SuppressWarnings("unchecked")
    public RoutineFunCallNode (Span span, Object function) {
        super(span);
        this.function = Util.cast(function, FunCallNode.class);
    }

    @Override public String contents ()
    {
        return name +  function.contents();
    }
}
