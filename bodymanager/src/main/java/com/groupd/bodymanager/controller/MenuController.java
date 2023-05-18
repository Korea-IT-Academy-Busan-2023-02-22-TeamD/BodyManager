package com.groupd.bodymanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.groupd.bodymanager.dto.request.menu.MenuRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuDetailListResponseDto;
import com.groupd.bodymanager.service.MenuService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    
    
    //*1.유저코드와 메뉴코드를 등록 */
    @PostMapping("")
    public ResponseEntity<ResponseDto> postMenuCodeAndUserCode(
        @Valid @RequestBody MenuRequestDto requestBody
        ) {
            ResponseEntity<ResponseDto> response = menuService.postMenuCodeAndUserCode(requestBody);
            return response;
        }

    //*2.식단 리스트를 조회 */
    @GetMapping("/{userCode}")
    public ResponseEntity<? super GetMenuDetailListResponseDto> getMenuDetailList(
        @PathVariable("userCode") Integer userCode
    ) {
        ResponseEntity<? super GetMenuDetailListResponseDto> response = menuService.getMenuDetailList(userCode);
        return response;
    }

    //*3.유저의 메뉴코드 변경 */
    @PatchMapping("")
    public ResponseEntity<ResponseDto> patchMenuCode(
        @Valid @RequestBody MenuRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = menuService.patchMenuCode(requestBody);
        return response;
    }

    
}
