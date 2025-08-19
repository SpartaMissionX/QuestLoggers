package com.missionx.questloggers.global.config;

import com.missionx.questloggers.domain.auth.exception.ExpiredTokenException;
import com.missionx.questloggers.domain.auth.exception.MalformedTokenException;
import com.missionx.questloggers.domain.auth.exception.NoneTokenException;
import com.missionx.questloggers.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long TOKEN_TIME = 60 * 60 * 3000L; // 1시간

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
                .claim("ownerCharId", user.getOwnerCharId())
                .claim("ownerCharName", user.getOwnerCharName())
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
            parseClaims(token); // 파싱 시도
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        } catch (MalformedJwtException | SecurityException | IllegalArgumentException e) {
            throw new MalformedTokenException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    // 헤더에서 Bearer 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new NoneTokenException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않거나 형식이 잘못되었습니다.");
        }
        return bearerToken.substring(7);
    }

    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // 보류
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public String getApiKeyFromToken(String token) {
        return parseClaims(token).get("apiKey", String.class);
    }

    public Integer getPointFromToken(String token) {
        return parseClaims(token).get("point", Integer.class);
    }

    public Long getOwnerCharIdFromToken(String token) {
        return parseClaims(token).get("ownerCharId", Long.class);
    }

    public String getOwnerCharNameFromToken(String token) {
        return parseClaims(token).get("ownerCharName", String.class);
    }
}
