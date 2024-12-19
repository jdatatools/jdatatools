package com.ainouss.datatools.jdatatools.util;


public final class DataUtils {

    public static <T extends CharSequence> boolean isBlank(T s) {
        if (s == null) {
            return true;
        }
        return skipSpaceTab(s, s.length()) == s.length();
    }

    public static boolean isNotBlank(String s) {
        if (s == null) {
            return false;
        }
        return trimToNull(s) != null;
    }

    private static int skipSpaceTab(CharSequence s, int endIndex) {
        for (int i = 0; i < endIndex; i++) {
            switch (s.charAt(i)) {
                case ' ':
                case '\t':
                    break;
                default:
                    return i;
            }
        }
        return endIndex;
    }
    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    public static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String trimToBlank(String s) {
        String s1 = trimToNull(s);
        if (s1 == null) {
            return "";
        }
        return s1;
    }


    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''");
    }
}
