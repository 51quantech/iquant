package com.iquant.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by yonggangli on 2016/8/29.
 */
public class Duration {
    private static final Pattern PATTERN = Pattern.compile("[\\d]+[\\s]*(ns|nanosecond(s)?|us|microsecond(s)?|ms|millisecond(s)?|s|second(s)?|m|minute(s)?|h|hour(s)?|d|day(s)?)");
    private static final ImmutableMap<String, TimeUnit> SUFFIXES;
    private final long count;
    private final TimeUnit unit;

    public static Duration nanoseconds(long count) {
        return new Duration(count, TimeUnit.NANOSECONDS);
    }

    public static Duration microseconds(long count) {
        return new Duration(count, TimeUnit.MICROSECONDS);
    }

    public static Duration milliseconds(long count) {
        return new Duration(count, TimeUnit.MILLISECONDS);
    }

    public static Duration seconds(long count) {
        return new Duration(count, TimeUnit.SECONDS);
    }

    public static Duration minutes(long count) {
        return new Duration(count, TimeUnit.MINUTES);
    }

    public static Duration hours(long count) {
        return new Duration(count, TimeUnit.HOURS);
    }

    public static Duration days(long count) {
        return new Duration(count, TimeUnit.DAYS);
    }

    private static long parseCount(String s) {
        Preconditions.checkArgument(PATTERN.matcher(s).matches(), "Invalid duration: %s", new Object[]{s});
        String value = CharMatcher.WHITESPACE.removeFrom(s);
        return Long.parseLong(CharMatcher.JAVA_LETTER.trimTrailingFrom(value));
    }

    private static TimeUnit parseUnit(String s) {
        String value = CharMatcher.WHITESPACE.removeFrom(s);
        String suffix = CharMatcher.DIGIT.trimLeadingFrom(value);
        return (TimeUnit)SUFFIXES.get(suffix);
    }

    private Duration(long count, TimeUnit unit) {
        this.count = count;
        this.unit = (TimeUnit)Preconditions.checkNotNull(unit);
    }

    public long toNanoseconds() {
        return TimeUnit.NANOSECONDS.convert(this.count, this.unit);
    }

    public long toMicroseconds() {
        return TimeUnit.MICROSECONDS.convert(this.count, this.unit);
    }

    public long toMilliseconds() {
        return TimeUnit.MILLISECONDS.convert(this.count, this.unit);
    }

    public long toSeconds() {
        return TimeUnit.SECONDS.convert(this.count, this.unit);
    }

    public long toMinutes() {
        return TimeUnit.MINUTES.convert(this.count, this.unit);
    }

    public long toHours() {
        return TimeUnit.HOURS.convert(this.count, this.unit);
    }

    public long toDays() {
        return TimeUnit.DAYS.convert(this.count, this.unit);
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj != null && this.getClass() == obj.getClass()) {
            Duration duration = (Duration)obj;
            return this.count == duration.count && this.unit == duration.unit;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return 31 * (int)(this.count ^ this.count >>> 32) + this.unit.hashCode();
    }

    public String toString() {
        String units = this.unit.toString().toLowerCase();
        if(this.count == 1L) {
            units = units.substring(0, units.length() - 1);
        }

        return Long.toString(this.count) + ' ' + units;
    }

    static {
        Builder suffixes = ImmutableMap.builder();
        suffixes.put("ns", TimeUnit.NANOSECONDS);
        suffixes.put("nanosecond", TimeUnit.NANOSECONDS);
        suffixes.put("nanoseconds", TimeUnit.NANOSECONDS);
        suffixes.put("us", TimeUnit.MICROSECONDS);
        suffixes.put("microsecond", TimeUnit.MICROSECONDS);
        suffixes.put("microseconds", TimeUnit.MICROSECONDS);
        suffixes.put("ms", TimeUnit.MILLISECONDS);
        suffixes.put("millisecond", TimeUnit.MILLISECONDS);
        suffixes.put("milliseconds", TimeUnit.MILLISECONDS);
        suffixes.put("s", TimeUnit.SECONDS);
        suffixes.put("second", TimeUnit.SECONDS);
        suffixes.put("seconds", TimeUnit.SECONDS);
        suffixes.put("m", TimeUnit.MINUTES);
        suffixes.put("minute", TimeUnit.MINUTES);
        suffixes.put("minutes", TimeUnit.MINUTES);
        suffixes.put("h", TimeUnit.HOURS);
        suffixes.put("hour", TimeUnit.HOURS);
        suffixes.put("hours", TimeUnit.HOURS);
        suffixes.put("d", TimeUnit.DAYS);
        suffixes.put("day", TimeUnit.DAYS);
        suffixes.put("days", TimeUnit.DAYS);
        SUFFIXES = suffixes.build();
    }
}
