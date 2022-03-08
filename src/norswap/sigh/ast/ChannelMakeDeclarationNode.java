package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.sigh.types.ChanType;
import norswap.utils.Util;
import java.util.List;

public class ChannelMakeDeclarationNode extends ExpressionNode
{

    public final TypeNode type;
    public final String name = "make";

    @SuppressWarnings("unchecked")
    public ChannelMakeDeclarationNode
        (Span span, Object type) {
        super(span);
        this.type = Util.cast(type, TypeNode.class);

    }


    @Override public String contents () {
        return "make " + type;
    }

  /*  @Override
    public String name () {
        return "make";
    }

    @Override public String declaredThing () {
        return "function";
    }*/
}
