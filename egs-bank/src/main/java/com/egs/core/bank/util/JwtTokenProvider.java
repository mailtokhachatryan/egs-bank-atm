package com.egs.core.bank.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token-secret}")
    private String jwtSecret;
    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;
    private Date exp = new Date(validityInMilliseconds);

    public String generateToken(String cardId) {
        return Jwts.builder()
                .setSubject(cardId)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setExpiration(exp)
                .compact();
    }
}
