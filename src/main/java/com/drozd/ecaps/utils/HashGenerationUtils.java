package com.drozd.ecaps.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.function.Predicate;

public class HashGenerationUtils {
    private HashGenerationUtils(){}
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RND = new SecureRandom();
    private static final char[] CHARS = new char[]{'A', 'a', '7', 'K', 'x', 'd', 'F', 'z', '6', 'Y'};
    private static final Logger log = LoggerFactory.getLogger(HashGenerationUtils.class);

    public static String getUniqueHash(Predicate<String> uniquenessChecker, int minLength) {
        String hash = HashGenerationUtils.getHash(60);
        int numberOfTries = 0;
        while (uniquenessChecker.test(hash)) {
            hash = HashGenerationUtils.getHash(minLength);
            if(numberOfTries++ > 10){
                minLength++;
            }
        }
        if(numberOfTries > 1){
            log.warn("Unique hash generator generated hash that is not unique and retried {} times.", numberOfTries);
        }
        return hash;
    }


    public static String getHash(int minLength) {
        final int randomHashPartLength = minLength < 25 ? 10 : minLength - 13;
        final String hashBasedOnTime = getHashBasedOnTime();
        final String randomHashPart = getRandomHashPart(randomHashPartLength);
        return hashBasedOnTime.substring(0, 5) +
                randomHashPart.substring(0, randomHashPartLength / 3) +
                hashBasedOnTime.substring(5) +
                randomHashPart.substring(randomHashPartLength / 3);
    }

    private static String getHashBasedOnTime() {
        Long time = System.currentTimeMillis();
        String timeString = String.valueOf(time);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < timeString.length(); j++) {
            sb.append(CHARS[Integer.parseInt(timeString.substring(j, j + 1))]);
        }
        return sb.toString();
    }

    private static String getRandomHashPart(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(ALPHANUMERIC.charAt(RND.nextInt(ALPHANUMERIC.length())));
        return sb.toString();
    }
}
