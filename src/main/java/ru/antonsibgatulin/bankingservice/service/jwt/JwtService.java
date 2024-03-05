package ru.antonsibgatulin.bankingservice.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;


    public String generateToken(UserDetails userDetails) {


        Map<String, Object> claims = new HashMap<>();


        return Jwts.builder()

                .setClaims(claims)

                .setSubject(userDetails.getUsername())

                .setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 31 days

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();

    }

    public boolean validateToken(String token) {

        return !isTokenExpired(token, secretKey);

    }


    public boolean isTokenExpired(String token, String secretKey) {

        return getClaims(token, secretKey).getExpiration().before(new Date());

    }


    public Claims getClaims(String token, String secretKey) {

        return Jwts.parser()

                .setSigningKey(secretKey)

                .parseClaimsJws(token)

                .getBody();

    }

    public String extractUsername(String token) {

        Claims claims = parseClaims(token);

        return claims.getSubject();

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims parseClaims(String token) {

        return Jwts.parser()

                .setSigningKey(secretKey)

                .parseClaimsJws(token)

                .getBody();

    }
}

