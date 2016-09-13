package com.iquant.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.regex.Pattern;

/**
 * Created by yonggangli on 2016/8/29.
 */
public class Size {

    private static final Pattern PATTERN = Pattern.compile("[\\d]+[\\s]*(B|byte(s)?|KB|KiB|kilobyte(s)?|MB|MiB|megabyte(s)?|GB|GiB|gigabyte(s)?|TB|TiB|terabyte(s)?)");
    private static final ImmutableMap<String, SizeUnit> SUFFIXES;
    private final long count;
    private final SizeUnit unit;

    public static Size bytes(long count) {
        return new Size(count, SizeUnit.BYTES);
    }

    public static Size kilobytes(long count) {
        return new Size(count, SizeUnit.KILOBYTES);
    }

    public static Size megabytes(long count) {
        return new Size(count, SizeUnit.MEGABYTES);
    }

    public static Size gigabytes(long count) {
        return new Size(count, SizeUnit.GIGABYTES);
    }

    public static Size terabytes(long count) {
        return new Size(count, SizeUnit.TERABYTES);
    }

    private static long parseCount(String s) {
        Preconditions.checkArgument(PATTERN.matcher(s).matches(), "Invalid size: %s", new Object[]{s});
        String value = CharMatcher.WHITESPACE.removeFrom(s);
        return Long.parseLong(CharMatcher.JAVA_LETTER.trimTrailingFrom(value));
    }

    private static SizeUnit parseUnit(String s) {
        String value = CharMatcher.WHITESPACE.removeFrom(s);
        String suffix = CharMatcher.DIGIT.trimLeadingFrom(value).trim();
        return (SizeUnit) SUFFIXES.get(suffix);
    }

    private Size(long count, SizeUnit unit) {
        this.count = count;
        this.unit = (SizeUnit) Preconditions.checkNotNull(unit);
    }

    public long toBytes() {
        return SizeUnit.BYTES.convert(this.count, this.unit);
    }

    public long toKilobytes() {
        return SizeUnit.KILOBYTES.convert(this.count, this.unit);
    }

    public long toMegabytes() {
        return SizeUnit.MEGABYTES.convert(this.count, this.unit);
    }

    public long toGigabytes() {
        return SizeUnit.GIGABYTES.convert(this.count, this.unit);
    }

    public long toTerabytes() {
        return SizeUnit.TERABYTES.convert(this.count, this.unit);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Size size = (Size) obj;
            return this.count == size.count && this.unit == size.unit;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return 31 * (int) (this.count ^ this.count >>> 32) + this.unit.hashCode();
    }

    public String toString() {
        String units = this.unit.toString().toLowerCase();
        if (this.count == 1L) {
            units = units.substring(0, units.length() - 1);
        }

        return Long.toString(this.count) + ' ' + units;
    }

    static {
        Builder suffixes = ImmutableMap.builder();
        suffixes.put("B", SizeUnit.BYTES);
        suffixes.put("byte", SizeUnit.BYTES);
        suffixes.put("bytes", SizeUnit.BYTES);
        suffixes.put("KB", SizeUnit.KILOBYTES);
        suffixes.put("KiB", SizeUnit.KILOBYTES);
        suffixes.put("kilobyte", SizeUnit.KILOBYTES);
        suffixes.put("kilobytes", SizeUnit.KILOBYTES);
        suffixes.put("MB", SizeUnit.MEGABYTES);
        suffixes.put("MiB", SizeUnit.MEGABYTES);
        suffixes.put("megabyte", SizeUnit.MEGABYTES);
        suffixes.put("megabytes", SizeUnit.MEGABYTES);
        suffixes.put("GB", SizeUnit.GIGABYTES);
        suffixes.put("GiB", SizeUnit.GIGABYTES);
        suffixes.put("gigabyte", SizeUnit.GIGABYTES);
        suffixes.put("gigabytes", SizeUnit.GIGABYTES);
        suffixes.put("TB", SizeUnit.TERABYTES);
        suffixes.put("TiB", SizeUnit.TERABYTES);
        suffixes.put("terabyte", SizeUnit.TERABYTES);
        suffixes.put("terabytes", SizeUnit.TERABYTES);
        SUFFIXES = suffixes.build();
    }
}
