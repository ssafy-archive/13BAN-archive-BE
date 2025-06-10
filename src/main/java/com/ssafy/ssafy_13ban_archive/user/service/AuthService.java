package com.ssafy.ssafy_13ban_archive.user.service;

import com.ssafy.ssafy_13ban_archive.user.model.request.LoginRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LogoutRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.ActionResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public LoginResponseDTO login(LoginRequestDTO request) {
        return null;
    }

    public ActionResponseDTO logout(LogoutRequestDTO request) {
        return new ActionResponseDTO(true);
    }

}
