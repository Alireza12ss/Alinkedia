package org.example.DataBaseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCheck {
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


}
