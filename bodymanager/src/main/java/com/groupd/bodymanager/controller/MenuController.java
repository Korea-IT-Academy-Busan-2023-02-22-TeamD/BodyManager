package com.groupd.bodymanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.groupd.bodymanager.dto.request.menu.MenuRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuDetailListResponseDto;
import com.groupd.bodymanager.service.MenuService;



@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        menuService = this.menuService;
    }
    
    
    // 1. 유저코드와 메뉴코드를 등록
    @PostMapping("")
    public ResponseEntity<ResponseDto> postMenuCodeAndUserCode(
        @Valid @RequestBody MenuRequestDto requestBody
        ) {
            ResponseEntity<ResponseDto> response = menuService.postMenuCodeAndUserCode(requestBody);
            return response;
        }


    // 2. 식단 정보를 조회
    @GetMapping("/{userCode}")
    public ResponseEntity<? super GetMenuResponseDto> getMenu(
        @Valid @RequestBody MenuRequestDto requestBody
    ) {
        ResponseEntity<? super GetMenuResponseDto> response =
        menuService.getMenu(requestBody);
        return response;
    }

    //*3.식단 리스트를 조회 */
    @GetMapping("/list")
    public ResponseEntity<? super GetMenuDetailListResponseDto> getMenuDetailList() {
        ResponseEntity<? super GetMenuDetailListResponseDto> response = menuService.getMenuDetailList();
        return response;
    }

    //*4.유저의 메뉴코드 변경 */
    @PatchMapping("/{userCode}")
    public ResponseEntity<ResponseDto> patchMenuCode(
        @Valid @RequestBody MenuRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = menuService.patchMenuCode(requestBody);
        return response;
    }

    
}
