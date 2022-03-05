package norswap.sigh.types;

public class ChanStringType extends ChanType{

    public static final ChanStringType INSTANCE = new ChanStringType();
    private ChanStringType () {

    }

    @Override
    public String name () {
        return "ChanString";
    }
}
