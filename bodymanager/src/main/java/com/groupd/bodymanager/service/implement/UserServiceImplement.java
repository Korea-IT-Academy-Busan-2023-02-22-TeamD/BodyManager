package com.groupd.bodymanager.service.implement;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groupd.bodymanager.common.CustomResponse;
import com.groupd.bodymanager.dto.request.user.PatchUserRequestDto;
import com.groupd.bodymanager.dto.request.user.SignInRequestDto;
import com.groupd.bodymanager.dto.request.user.SignUpRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.user.GetAuthResponseDto;
import com.groupd.bodymanager.dto.response.user.GetUserResponseDto;
import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.provider.JwtProvider;
import com.groupd.bodymanager.repository.UserRepository;
import com.groupd.bodymanager.service.UserService;

@Service
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userCode;

    @Autowired
    public UserServiceImplement(
            UserRepository userRepository,
            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtProvider = jwtProvider;
    }

    // 회원가입
    @Override
    public ResponseEntity<? super GetAuthResponseDto> signUp(SignUpRequestDto dto) {
        GetAuthResponseDto body = null;
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();
        String userNickname = dto.getUserNickname();
        String userPhoneNumber = dto.getUserPhoneNumber();
        String userAddress = dto.getUserAddress();
        String userGender = dto.getUserGender();
        int userAge = dto.getUserAge();

        try { // TODO 존재하는 유저 이메일
            boolean existedUserEmail = userRepository.existsByEmail(userEmail);
            if (existedUserEmail)
                return CustomResponse.existUserEmail();

            // TODO 존재하는 유저 닉네임
            boolean existedUserNickname = userRepository.existsByNickname(userNickname);
            if (existedUserNickname)
                return CustomResponse.existUserNickname();

            // TODO 존재하는 유저 휴대전화 번호
            boolean existedUserPhoneNumber = userRepository.existsByPhoneNumber(userPhoneNumber);
            if (existedUserPhoneNumber)
                return CustomResponse.existUserPhoneNumber();

            String encodedPassword = passwordEncoder.encode(userPassword); // 유저 계정 생성 및 암호화 작업
            dto.setUserPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto, userCode);
            userRepository.save(userEntity);

            body = new GetAuthResponseDto(userCode);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 로그인
    @Override
    public ResponseEntity<? super GetAuthResponseDto> signIn(SignInRequestDto dto) {
        GetAuthResponseDto body = null;
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();

        try {
            // TODO 로그인 실패 (이메일 x)
            UserEntity userEntity = userRepository.findByEmail(userEmail);
            if (userEmail == null)
                return CustomResponse.signInFailed();

            // TODO 로그인 실패 (패스워드 x)
            String encordedPassword = userEntity.getUserPassword();
            boolean equaledPassword = passwordEncoder.matches(userPassword, encordedPassword);
            ;
            if (!equaledPassword)
                return CustomResponse.signInFailed();
            String jwt = jwtProvider.create(userEmail);

            body = new GetAuthResponseDto(jwt, userEntity.getUserCode());

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(Integer userCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public ResponseEntity<ResponseDto> patchUser(PatchUserRequestDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patchUser'");
    }

    @Override
    public ResponseEntity<ResponseDto> deletdUser(String userEmail, Integer userCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletdUser'");
    }
}