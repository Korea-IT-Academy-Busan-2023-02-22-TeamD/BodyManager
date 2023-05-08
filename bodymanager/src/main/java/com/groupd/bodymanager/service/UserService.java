package com.groupd.bodymanager.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupd.bodymanager.dto.request.user.PostUserRequestDto;
import com.groupd.bodymanager.dto.request.user.SignInRequestDto;
import com.groupd.bodymanager.dto.request.user.SignUpRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.user.GetAuthResponseDto;

public interface UserService {
    
    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto);
    public ResponseEntity<? super GetAuthResponseDto> signIn(SignUpRequestDto dto);
}
