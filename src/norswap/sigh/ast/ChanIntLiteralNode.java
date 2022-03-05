package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;
import java.util.List;

public class ChanIntLiteralNode extends ChanLiteralNode
{

    @SuppressWarnings("unchecked")
    public ChanIntLiteralNode (Span span, Object components) {
        super(span, Util.cast(components, List.class));
    }

    @Override public String contents ()
    {
        if (components.size() == 0)
            return "[]";

        int budget = contentsBudget() - 2; // 2 == "[]".length()
        StringBuilder b = new StringBuilder("{");
        int i = 0;

        for (Object item: components)
        {
            int it = (int) item;
            if (i > 0) b.append(", ");
            b.append(it);
            //String contents = it.contents();


           /* budget -= 2 + contents.length();
            if (i == components.size() - 1) {
                if (budget < 0) break;
            } else {
                if (budget - ", ...".length() < 0) break;
            }
            b.append(contents);*/
            ++i;
        }

        if (i < components.size())
            b.append("...");

        return b.append('}').toString();
    }

}
