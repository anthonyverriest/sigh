package norswap.sigh.ast;

public enum ChannelOperator {

    IO("<-");

    public final String string;

    ChannelOperator (String string) {
        this.string = string;
    }
}
