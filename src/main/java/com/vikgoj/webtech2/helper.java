package com.vikgoj.webtech2;

import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Yap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
}


