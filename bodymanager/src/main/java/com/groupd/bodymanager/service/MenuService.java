package com.groupd.bodymanager.service;

import org.springframework.http.ResponseEntity;

import com.groupd.bodymanager.dto.request.menu.MenuRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuDetailListResponseDto;

public interface MenuService {

    public ResponseEntity<ResponseDto> postMenuCodeAndUserCode(MenuRequestDto dto);
    public ResponseEntity<? super GetMenuDetailListResponseDto> getMenuDetailList(Integer userCode);
    public ResponseEntity<ResponseDto> patchMenuCode(MenuRequestDto dto);

}
