package org.example.DataBaseHandler;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.sql.*;
import java.util.regex.Pattern;

public class DataCheck {
    private static final String key = "AESEncryptionKey"; // 128-bit key
    private static final String initVector = "encryptionIntVec"; // 16 bytes IV


    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean CheckEmail(String email) {
        return patternMatches(email);
    }

    public static boolean uniqueEmail(String email){
        try {
            Connection connection = DAO.CreateConnection();

            String sql = "select * from users where email = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);


            stmt.setString(1 , email);

            ResultSet set = stmt.executeQuery();
            return !set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        return letters != 0 && digits != 0;
    }

    public static boolean CheckPass(String pass) {
        return isValidPassword(pass);
    }

    public static String encrypt(String password) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedPassword) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            System.out.println(encryptedPassword);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));

            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
