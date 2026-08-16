package com.daniel.todolist_api.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKay (){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKay())
                .compact();
    }

    public String extrairEmail(String token){
        return extrairClaim(token, Claims::getSubject);
    }

    public boolean tokenValido(String token, String email){
        String emailDoToken = extrairEmail(token);
        return emailDoToken.equals(email) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token){
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

   private <T> T extrairClaim(String token, Function<Claims, T> resolver){
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKay())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
   }
}
