package com.example.devicemanagement.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;



@Service
public class JwtService {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            System.err.println("Invalid token: " + e.getMessage());
            return false;
        }
    }
}
