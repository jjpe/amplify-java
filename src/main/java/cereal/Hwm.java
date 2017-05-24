package cereal;

public class Hwm {
    public static final Hwm UNBOUNDED = new Hwm(0);

    public final int capacity;

    public Hwm(final int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity must >= 0");
        this.capacity = capacity;
    }

    public boolean isUnbounded() { return UNBOUNDED.equals(this); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hwm hwm = (Hwm) o;

        return capacity == hwm.capacity;
    }

    @Override
    public int hashCode() {
        return capacity;
    }
}
