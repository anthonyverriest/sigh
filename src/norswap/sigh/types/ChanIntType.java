package norswap.sigh.types;

public class ChanIntType extends Type{

    public static final ChanIntType INSTANCE = new ChanIntType();
    private ChanIntType () {}

    @Override
    public String name () {
        return "ChanIntType";
    }
}
