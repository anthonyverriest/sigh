package norswap.sigh.types;

public class ChanIntType extends ChanType{

    public static final ChanIntType INSTANCE = new ChanIntType();
    private ChanIntType () {}

    @Override
    public String name () {
        return "ChanInt";
    }
}
