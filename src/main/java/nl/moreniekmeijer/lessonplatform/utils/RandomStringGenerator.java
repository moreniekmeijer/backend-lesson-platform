package nl.moreniekmeijer.lessonplatform.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

//    public static String generateAlphaNumeric(int length) {
//        if (length <= 0) {
//            throw new IllegalArgumentException("Length must be greater than 0");
//        }
//
//        StringBuilder sb = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
//        }
//
//        return sb.toString();
//    }
}
