package com.ssafy.ssafy_13ban_archive.user.service;

import com.ssafy.ssafy_13ban_archive.user.exception.SignInFailureException;
import com.ssafy.ssafy_13ban_archive.user.exception.UserNotFoundException;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import com.ssafy.ssafy_13ban_archive.user.model.entity.UserRole;
import com.ssafy.ssafy_13ban_archive.user.model.request.SignInRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.SignInResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.UserResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 회원가입
     * @param request 회원가입 요청 정보(로그인 id, password, 이름, ssafyNumber)
     * @return 회원가입 응답 정보
     */
    @Transactional
    public SignInResponseDTO signIn(SignInRequestDTO request) {
        String loginId = request.getLoginId();
        String password = request.getPassword();
        String name = request.getName();
        String sssafyNumber = request.getSsafyNumber();

        // 로그인 id 중복 검증
        if(userRepository.existsByLoginId(loginId)){
            throw new SignInFailureException("이미 존재하는 로그인 ID 입니다.");
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(password);

        // 회원가입 정보를 통해 사용자 객체 생성
        User user = User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .ssafyNumber(sssafyNumber)
                .userRole(UserRole.USER)
                .build();

        // DB에 저장
        User savedUser = userRepository.save(user);

        return SignInResponseDTO.builder()
                .userId(savedUser.getUserId())
                .loginId(savedUser.getLoginId())
                .name(savedUser.getName())
                .ssafyNumber(savedUser.getSsafyNumber())
                .userRole(savedUser.getUserRole().getRole())
                .build();
    }

    /**
     * 특정 사용자 조회
     * @param userId 사용자의 userId(pk)
     * @return 사용자 정보(비밀번호 제외)
     */
    public UserResponseDTO getUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .ssafyNumber(user.getSsafyNumber())
                .userRole(user.getUserRole().getRole())
                .build();
    }

}
