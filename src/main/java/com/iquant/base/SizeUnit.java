package com.iquant.base;

/**
 * Created by yonggangli on 2016/8/29.
 */
public enum SizeUnit {
    BYTES(8L),
    KILOBYTES(8192L),
    MEGABYTES(8388608L),
    GIGABYTES(8589934592L),
    TERABYTES(8796093022208L);

    private final long bits;

    private SizeUnit(long bits) {
        this.bits = bits;
    }

    public long convert(long size, SizeUnit unit) {
        return size * unit.bits / this.bits;
    }

    public long toBytes(long l) {
        return BYTES.convert(l, this);
    }

    public long toKilobytes(long l) {
        return KILOBYTES.convert(l, this);
    }

    public long toMegabytes(long l) {
        return MEGABYTES.convert(l, this);
    }

    public long toGigabytes(long l) {
        return GIGABYTES.convert(l, this);
    }

    public long toTerabytes(long l) {
        return TERABYTES.convert(l, this);
    }
}
