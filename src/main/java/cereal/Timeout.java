package cereal;

public class Timeout {
    public static final Timeout BLOCK = new Timeout(-1);
    public static final Timeout NONE = new Timeout(0);

    public final int millis;

    public Timeout(final int millis) {
        this.millis = (millis >= 0) ? millis : -1;
    }

    public final boolean isBlock() { return BLOCK.equals(this); }
    public final boolean isNone() { return NONE.equals(this); }
    public final boolean isMillis() { return !this.isBlock() && !this.isNone(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timeout timeout = (Timeout) o;

        return millis == timeout.millis;
    }

    @Override
    public int hashCode() {
        return millis;
    }
}
