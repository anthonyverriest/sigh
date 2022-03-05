package norswap.sigh.types;

public class ChanFloatType extends ChanType{

    public static final ChanFloatType INSTANCE = new ChanFloatType();
    private ChanFloatType () {}

    @Override
    public String name () {
        return "ChanFloat";
    }
}
