package core;

public class Region {
    final long begin;
    final long end;

    public Region(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() { return begin; }

    public long getEnd() { return end; }
}
