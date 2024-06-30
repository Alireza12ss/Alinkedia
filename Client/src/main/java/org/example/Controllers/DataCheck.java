package org.example.demologin;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

public class DataCheck {
    private static final String key = "AESencryptionKey"; // 128 bit key
    private static final String initVector = "encryptionIntVec"; // 16 bytes IV


    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean CheckEmail(String email) {
        if (patternMatches(email)){
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password)
    {
        if (password.length() < 8){
            return false;
        }
        int digits = 0 , letters = 0;
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetterOrDigit(password.charAt(i))){
                return false;
            }
            if (Character.isDigit(password.charAt(i))){
                digits++;
            }else {
                letters++;
            }
        }
        if (letters == 0 || digits == 0 ){
            return false;
        }
        return true;
    }

    public static boolean CheckPass(String pass) {
        if (isValidPassword(pass)){
            return true;
        }
        return false;
    }

    public static String encrypt(String password) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedPassword) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));

            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
