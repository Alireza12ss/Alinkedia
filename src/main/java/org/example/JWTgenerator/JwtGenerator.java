package org.example.JWTgenerator;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtGenerator {
    public JwtGenerator() {}

    private static final Key secretKey = Keys.hmacShaKeyFor("youThinkDarknessIsYourAllyYouMerelyAdoptedTheDarkIwasBornInIt".getBytes());
    private final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public String createToken(Map<String, Object> payload, int expirationMinutes) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + (long) expirationMinutes * 60 * 1000);

        return Jwts.builder().setClaims(payload).setIssuedAt(now).setExpiration(exp).signWith(secretKey, algorithm).compact();
    }

    public static boolean tokenIsValid(String token){
        return decodeToken(token) != null;
    }

    public static Map<String, Object> decodeToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return new HashMap<>(claims);
        } catch (JwtException e) {
            System.out.println("Invalid or expired token: " + e.getMessage());
            return null;
        }
    }

}