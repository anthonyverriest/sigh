package norswap.sigh.types;

public class ChanStringType extends Type{

    public static final ChanStringType INSTANCE = new ChanStringType();
    private ChanStringType () {}

    @Override
    public String name () {
        return "ChanStringType";
    }
}
