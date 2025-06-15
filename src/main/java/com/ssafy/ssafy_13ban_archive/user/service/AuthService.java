package com.ssafy.ssafy_13ban_archive.user.service;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.security.dto.CustomUserDetails;
import com.ssafy.ssafy_13ban_archive.security.util.JwtUtil;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import com.ssafy.ssafy_13ban_archive.user.model.request.LoginRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LogoutRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticator;
    private final JwtUtil jwtUtil;

    /**
     * 사용자 로그인을 처리하고 jwt 토큰 발급
     * @param request 로그인 요청 정보(로그인 id, password)
     * @return 로그인 응답 정보(accessToken, refreshToken 포함)
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication auth = authenticator.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        String accessToken = "Bearer " + jwtUtil.generateAccessToken(user);
        String refreshToken = "Bearer " + jwtUtil.generateRefreshToken(user.getUserId());
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    /**
     * 사용자 로그아웃 처리
     * @param request 로그아웃 요청 정보
     * @return 로그아웃 처리 결과
     */
    public SuccessResponseDTO logout(LogoutRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        // 토큰 형식 검증
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return new SuccessResponseDTO(false);
        }

        String token = jwtUtil.extractToken(refreshToken);
        jwtUtil.addToBlacklist(token);
        return new SuccessResponseDTO(true);
    }

}
