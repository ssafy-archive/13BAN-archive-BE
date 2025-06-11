package com.ssafy.ssafy_13ban_archive.security.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final int accessExpirationTime;
    private final int refreshExpirationTime;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "token-blacklist:";

    public JwtUtil(
            @Value("${spring.jwt.secret}")String secret,
            @Value("${jwt.access-token.expiration}") int accessExpirationTime,
            @Value("${jwt.refresh-token.expiration}") int refreshExpirationTime,
            RedisTemplate<String, Object> redisTemplate) {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.redisTemplate = redisTemplate;
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(key)
                .build();
    }

    /**
     * Access Token 생성
     */
    public String generateAccessToken(String username, String role) {

        return Jwts.builder()
                .subject(username)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpirationTime * 1000L))
                .signWith(key)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String username) {

        return Jwts.builder()
                .subject(username)
                .claim("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 1000L))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 username 추출
     */
    public String getUsername(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    /**
     * 토큰에서 role 추출
     */
    public String getRole(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    /**
     * 토큰 만료 시간 확인
     */
    public Boolean isExpired(String token) {
        try {
            return getJwtParser()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 토큰에서 만료 일자 가져오기
     */
    public Date getExpirationDateFromToken(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            getJwtParser().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 토큰이 블랙리스트에 등록되어 있는지 확인
     */
    private boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

    /**
     * 토큰을 블랙리스트에 추가 (로그아웃 시 사용)
     */
    public void addToBlacklist(String token) {
        try {
            // 토큰의 남은 유효 시간 계산
            Date expiration = getExpirationDateFromToken(token);
            long ttl = expiration.getTime() - System.currentTimeMillis();

            // 만료 시간이 양수일 경우에만 Redis에 저장
            if (ttl > 0) {
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + token,
                        "blacklisted",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (JwtException e) {
            // 이미 만료된 토큰이거나 유효하지 않은 토큰인 경우 무시
        }
    }

    /**
     * Bearer 토큰에서 실제 JWT 추출
     */
    public String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

}
