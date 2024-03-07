package com.vikgoj.webtech2;

import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Yap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class helper {

    public static String encodeString(String s){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(s.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static ArrayList<String> extractHashtags(String input) {
        ArrayList<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\w+?)(?=(\\s|$|#))");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }


}


