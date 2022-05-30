package owmii.lib.util;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Util {
    public static boolean anyMatch(int[] arr, int value) {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }

    public static boolean anyMatch(long[] arr, long value) {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }

    public static int safeInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long safeLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return 0L;
        }
    }

    static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();

    static {
        SUFFIXES.put(1_000L, "k");
        SUFFIXES.put(1_000_000L, "M");
        SUFFIXES.put(1_000_000_000L, "B");
        SUFFIXES.put(1_000_000_000_000L, "T");
        SUFFIXES.put(1_000_000_000_000_000L, "P");
        SUFFIXES.put(1_000_000_000_000_000_000L, "E");
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public static String numFormat(long value) {
        if (value == Long.MIN_VALUE) return numFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + numFormat(-value);
        if (value < 1000) return Long.toString(value);

        Map.Entry<Long, String> e = SUFFIXES.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String addCommas(long value) {
        return NumberFormat.getInstance().format(value);
    }
}
