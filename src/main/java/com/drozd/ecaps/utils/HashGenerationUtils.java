package com.drozd.ecaps.utils;

import java.security.SecureRandom;

public class HashGenerationUtils {
    private static final String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    private static final char[] chars = new char[]{'A', 'a', '7', 'K', 'x', 'd', 'F', 'z', '6', 'Y'};

    public static String getHash() {
        final String hashBasedOnTime = getHashBasedOnTime();
        final String randomHashPart = getRandomHashPart(10);
        return hashBasedOnTime.substring(0, 5) +
                randomHashPart.substring(0, 5) +
                hashBasedOnTime.substring(5) +
                randomHashPart.substring(5);
    }

    private static String getHashBasedOnTime() {
        Long time = System.currentTimeMillis();
        String timeString = String.valueOf(time);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < timeString.length(); j++) {
            sb.append(chars[Integer.parseInt(timeString.substring(j, j + 1))]);
        }
        return sb.toString();
    }

    private static String getRandomHashPart(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(alphanumeric.charAt(rnd.nextInt(alphanumeric.length())));
        return sb.toString();
    }
}
