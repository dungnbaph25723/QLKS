package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomCode {

    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String specials = "~=+%^*/()[]{}/!@#$?|";
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static Random generator = new Random();
    private ReadWriteData readWriteData = new ReadWriteData();

    public String createCode(String firstCode, String fileName) {
        firstCode = firstCode.toUpperCase();
        int lastCode = 0;
        List<Integer> list = new ArrayList<>();

        try {
            for (int i : readWriteData.doc(fileName)) {
                list.add(i);
            }
        } catch (IOException ex) {
            Logger.getLogger(RandomCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RandomCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        int temp;
        if (!list.isEmpty()) {
            for (int j = 0; j < list.size(); j++) {
                for (int i = 0; i < list.size() - 1; i++) {
                    if (list.get(j) < list.get(i)) {
                        temp = list.get(j);
                        list.add(j, list.get(i));
                        list.add(i, temp);
                    }
                }
            }
            lastCode = list.get(0);
        }

        System.out.println(lastCode);
        String code = firstCode;
        if (lastCode < 10) {
            lastCode = lastCode + 1;
            code = firstCode + "0" + String.valueOf(lastCode);
        } else {
            lastCode = lastCode + 1;
            code = firstCode + String.valueOf(lastCode);
        }
        System.out.println(code);
        return code;
    }

    public String randomAlphaNumeric(int numberOfCharactor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }

    public static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args) {

    }
}
