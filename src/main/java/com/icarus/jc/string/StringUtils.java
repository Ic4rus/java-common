package com.icarus.jc.string;

import java.util.List;
import java.util.Map;

public class StringUtils {

    /**
     * Check empty for a String
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * Check empty for an Array
     *
     * @param array the array to test
     * @return true if the array is empty or null
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check empty for a List
     *
     * @param l
     * @return
     */
    public static boolean isEmpty(List<?> l) {
        return l == null || l.isEmpty();
    }

    /**
     * Check empty for a Map
     *
     * @param m
     * @return
     */
    public static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static String middlePad(String str, int length, String padding) {
        StringBuilder sb = new StringBuilder(str);
        int offset = length;
        while (sb.length() > offset) {
            sb = sb.insert(offset, padding);
            offset += length + padding.length();
        }
        return sb.toString();
    }

}
