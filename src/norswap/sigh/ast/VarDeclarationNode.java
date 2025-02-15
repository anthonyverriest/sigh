package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;
import java.util.Objects;

public final class VarDeclarationNode extends DeclarationNode
{
    public final String name;
    public final TypeNode type;
    public final ExpressionNode initializer;

    public VarDeclarationNode (Span span, Object name, Object type, Object initializer) {
        super(span);
        this.name = Util.cast(name, String.class);
        this.type = Util.cast(type, TypeNode.class);
        this.initializer = Util.cast(initializer, ExpressionNode.class);
    }

    @Override public String name () {
        return name;
    }

    @Override public String contents () {
        return "var " + name;
    }

    @Override public String declaredThing () {
        return "variable";
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        VarDeclarationNode that = (VarDeclarationNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode () {
        return Objects.hash(super.hashCode(), name, type, initializer);
    }
}
