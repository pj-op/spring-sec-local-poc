package com.poc.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Slf4j
public class JwtService {
    //    https://generate.plus/en/hex/secret-key
    private static final String SECRET = "c499b208371ab1e64b3d37dabe3e00794bfb621d4ad264a33a3e70554c367f0fc04dec578e2690779411d767bc0c65a9b942e03da796558afec9d2b45bd6ada1";

    public static String getGeneratedToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public Jws<Claims> parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token);
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        log.info("{}, {}", isTokenExpired(token), extractUserName(token));
        return !isTokenExpired(token) && extractUserName(token).equals(userDetails.getUsername());
    }

    private static boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public static String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(10L, ChronoUnit.MINUTES)))
                .signWith(getSignedKey())
                .compact();
    }

    private static Key getSignedKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());
    }

    public static void main(String[] args) {
        System.out.println(getGeneratedToken("pranjal"));
    }

}