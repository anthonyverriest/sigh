package norswap.sigh.types;

public class ChanFloatType extends Type{

    public static final ChanFloatType INSTANCE = new ChanFloatType();
    protected ChanFloatType () {}

    @Override
    public String name () {
        return "ChanFloat";
    }
}
