package net.marios271.thermals;

public class Helpers {
    public static String doubleAsSinglePrecisionString(double val) {
        return String.format("%.1f", val);
    }

    public static long getAvgOfLongArray(long[] arr) {
        long avg = 0;
        for (long l : arr) {
            avg += l;
        }
        avg /= arr.length;
        return avg;
    }
}
