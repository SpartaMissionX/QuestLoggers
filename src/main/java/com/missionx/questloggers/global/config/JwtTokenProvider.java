package com.missionx.questloggers.global.config;

import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.springframework.boot.origin.OriginTrackedValue.of;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 1시간

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + TOKEN_TIME);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .claim("apiKey", user.getApiKey())
                .claim("point", user.getPoint())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 파싱
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token); // 파싱이 안되면 예외 발생
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 헤더에서 Bearer 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // 토큰에서 email 추출
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰에서 role 추출
    public Role getRoleFromToken(String token) {
        return Role.of(parseClaims(token).get("role", String.class));
    }

    // 토큰에서 apiKey 추출
    public String getApiKeyFromToken(String token) {
        return parseClaims(token).get("apiKey", String.class);
    }

    // 토큰에서 point 추출
    public int getPointFromToken(String token) {
        return parseClaims(token).get("point", Integer.class);
    }
}
