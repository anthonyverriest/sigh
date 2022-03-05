package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import java.util.List;

public abstract class ChanLiteralNode extends ExpressionNode {

    public final List<Object> components;

    public ChanLiteralNode (Span span, List<Object> components) {
        super(span);
        this.components = components;
    }
}
