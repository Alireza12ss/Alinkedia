package org.example;

import java.util.HashMap;

public class Main {
    public static <Map> void main(String[] args) {

        try {
            JwtGenerator generator = new JwtGenerator();

            HashMap<String, String> claims = new HashMap<>();

            claims.put("action", "read");
            claims.put("sub", "pawel.spychalski");
            claims.put("email", "test@quadmeup.com");
            claims.put("aud", "*");

            String token = generator.generateJwt(claims);
            System.out.println( token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}