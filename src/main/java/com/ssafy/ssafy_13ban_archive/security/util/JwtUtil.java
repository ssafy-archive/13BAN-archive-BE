package com.ssafy.ssafy_13ban_archive.security.util;

import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import io.jsonwebtoken.Claims;
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
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("userId", user.getUserId())
                .claim("loginId", user.getLoginId())
                .claim("name", user.getName())
                .claim("ssafyNumber", user.getSsafyNumber())
                .claim("role", user.getUserRole().getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpirationTime * 1000L))
                .signWith(key)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(Integer userId) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 1000L))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 JwtUserInfo 추출
     */
    public JwtUserInfo getUserInfoFromToken(String token) {
        Claims claims = getJwtParser()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtUserInfo(
                claims.get("userId", Integer.class),        // PK
                claims.get("loginId", String.class),        // 로그인 ID
                claims.get("name", String.class),           // 이름
                claims.get("ssafyNumber", String.class),    // 싸피번호
                claims.get("role", String.class)            // 권한
        );
    }

    /**
     * 토큰에서 userId 추출
     */
    public int getUserId(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Integer.class);
    }

    /**
     * 토큰에서 loginId 추출
     */
    public String getLoginId(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("loginId", String.class);
    }

    /**
     * 토큰에서 name 추출
     */
    public String getName(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }

    /**
     * 토큰에서 ssafyNumber 추출
     */
    public String getSsafyNumber(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("ssafyNumber", String.class);
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
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
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
