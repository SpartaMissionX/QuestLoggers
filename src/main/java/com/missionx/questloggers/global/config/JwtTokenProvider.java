package com.missionx.questloggers.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 유효시간 1시간

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 보안 강화를 위해 키 생성
    }

    // JWT 토큰 생성
    public String createToken(Long userId, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_TIME);

        return Jwts.builder()
                .setSubject(email) // 주체를 이메일로 설정
                .claim("userId", userId) // 사용자 ID도 넣어줌
                .setIssuedAt(now) // 토큰 발급 시간 설정
                .setExpiration(expiration) // 토큰 만료 시간 설정(1시간 제한)
                .signWith(key, SignatureAlgorithm.HS256) // 위조된게 아닌가 확인하는 디지털 서명
                .compact(); // 토큰을 문자열로 변환
    }

//    // 나중에 로그인 이후 요청에서 사용자 확인이 필요하거나
//    // 게시글/댓글 쓸때 작성자 확인용으로 보류
//    // 토큰에서 userId 추출
//    public Long extractUserId(String token) {
//        Claims claims = parseClaims(token);
//        return claims.get("userId", Long.class);
//    }
//
//    // 토큰에서 email 추출
//    public String extractEmail(String token) {
//        return parseClaims(token).getSubject();
//    }

//    // 나중에 필터나 인가 처리할때
//    // 토큰 파싱 및 유효성 검증
//    public Claims parseClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
}
