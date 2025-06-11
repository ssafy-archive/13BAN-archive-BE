package com.ssafy.ssafy_13ban_archive.user.service;

import com.ssafy.ssafy_13ban_archive.security.dto.CustomUserDetails;
import com.ssafy.ssafy_13ban_archive.security.util.JwtTokenProvider;
import com.ssafy.ssafy_13ban_archive.user.model.request.LoginRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LogoutRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.ActionResponseDTO;
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
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 로그인을 처리하고 jwt 토큰 발급
     * @param request 로그인 요청 정보(로그인 id, password)
     * @return 로그인 응답 정보(accessToken, refreshToken 포함)
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication auth = authenticator.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().toString();
        String accessToken = "Bearer" + jwtTokenProvider.generateAccessToken(username, role);
        String refreshToken = "Bearer" + jwtTokenProvider.generateRefreshToken(username);
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    /**
     * 사용자 로그아웃 처리
     * @param request 로그아웃 요청 정보
     * @return 로그아웃 처리 결과
     */
    public ActionResponseDTO logout(LogoutRequestDTO request) {
        return new ActionResponseDTO(true);
    }

}
