package com.example.meusgastos.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.meusgastos.domain.model.Usuario;
import com.fasterxml.jackson.core.StreamReadConstraints.Builder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Value("${auth.jwt.secret}")
    private String jwtSecret;
    @Value("${jwtExpirationMilliseg}")
    private Long jwtExpirationMilliseg;

    public String gerarToken(Authentication authecation) {
        Date dataExpiracao = new Date(new Date().getTime() + jwtExpirationMilliseg);
        Usuario usuario = (Usuario) authecation.getPrincipal();
        try {
            Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
            return Jwts.builder()
            .setSubject(usuario.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(dataExpiracao)
            .signWith(secretKey)
            .compact();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    // O claims destrincha o token e retorna para usar na getUserName e
    // isValidNumber
    private Claims getClaims(String token) {
        try {
            Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
            Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getUserName(String token) {
        Claims claims = getClaims(token);
        if(claims == null) {
            return "";
        }
        return claims.getSubject();
    }

    public boolean isValidNumber(String token) {
        Claims claims = getClaims(token);
        if(claims == null) {
            return false;
        }
        String email = claims.getSubject();
        Date dataExpiracao = claims.getExpiration();
        Date agora = new Date(System.currentTimeMillis());
        if((email != null) && (agora.before(dataExpiracao))) {
            return true;
        }
        return false;
    }

}
