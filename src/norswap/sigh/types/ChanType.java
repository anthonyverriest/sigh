package norswap.sigh.types;

public class ChanType extends Type{

    public static final ChanType INSTANCE = new ChanType();
    protected ChanType() {}

    @Override public String name() {
        return "ChanType";
    }



}


