package com.groupd.bodymanager.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupd.bodymanager.common.CustomResponse;
import com.groupd.bodymanager.dto.request.menu.MenuRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuDetailListResponseDto;
import com.groupd.bodymanager.entity.MenuEntity;
import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.entity.UserMenuSelect;
import com.groupd.bodymanager.entity.resultSet.MenuListResultSet;
import com.groupd.bodymanager.repository.MenuDetailRepository;
import com.groupd.bodymanager.repository.MenuRepository;
import com.groupd.bodymanager.repository.UserMenuSelectRepository;
import com.groupd.bodymanager.repository.UserRepository;
import com.groupd.bodymanager.service.MenuService;

@Service
public class MenuServiceImplement implements MenuService {
    private UserRepository userRepository;
    private MenuRepository menuRepository;
    private MenuDetailRepository menuDetailRepository;
    private UserMenuSelectRepository userMenuSelectRepository;

    @Autowired
    MenuServiceImplement(
        MenuRepository menuRepository, UserRepository userRepository, MenuDetailRepository menuDetailRepository,UserMenuSelectRepository userMenuSelectRepository){
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.menuDetailRepository = menuDetailRepository;
        this.userMenuSelectRepository = userMenuSelectRepository;

    }

    //*유저코드와 메뉴코드를 등록 */
    @Override
    public ResponseEntity<ResponseDto> postMenuCodeAndUserCode(MenuRequestDto dto) {
        String menuCode = dto.getMenuCode();
        int userCode = dto.getUserCode();
        try {
            // 필수 값 입력
            if (menuCode == null)  return CustomResponse.validationFaild();
            UserEntity userEntity = userRepository.findByUserCode(userCode);
            if (userEntity == null) return CustomResponse.notExistUserCode();

            // *존재하지 않는 메뉴코드 반환 */
            boolean existedByMenuCode = menuRepository.existsByMenuCode(menuCode);
            if (!existedByMenuCode) return CustomResponse.notExistMenuCode();

            MenuEntity menuEntity = menuRepository.findByMenuCode(menuCode);
            
            // *Response 데이터를 레포지토리에 저장 */
            UserMenuSelect userMenuSelect = new UserMenuSelect(userEntity, menuEntity);
            userMenuSelectRepository.save(userMenuSelect);
            
            
        } catch (Exception exceptione) {
            exceptione.printStackTrace();
            // *데이터베이스 오류 */
            return CustomResponse.databaseError();
        }
        // *성공 반환 */
        return CustomResponse.successs();
    }

    
    @Override //식단 조회
    public ResponseEntity<? super GetMenuDetailListResponseDto> getMenuDetailList(Integer userCode) {
        GetMenuDetailListResponseDto body = null;
        try {
            //*매개변수 에러 */
            if(userCode == null) return CustomResponse.validationFaild();

            UserMenuSelect userMenuSelect = userMenuSelectRepository.findByUserCode(userCode);

            //*존재하지 않는 유저 */
            if(userMenuSelect == null) return CustomResponse.notExistUserCode();
            String menuCode = userMenuSelect.getMenuCode();
            MenuEntity menuEntity = menuRepository.findByMenuCode(menuCode);
            
            List<MenuListResultSet> resultSet = menuDetailRepository.getMenuDetailList();
            body = new GetMenuDetailListResponseDto(resultSet,menuEntity,userMenuSelect);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    




    @Override // 메뉴 코드를 수정
    public ResponseEntity<ResponseDto> patchMenuCode(MenuRequestDto dto) {
        int userCode = dto.getUserCode();
        String menuCode = dto.getMenuCode();

        try {
            UserMenuSelect userMenuSelect = userMenuSelectRepository.findByUserCode(userCode);

            if(userMenuSelect == null) return CustomResponse.notExistUserCode();

            //* 수정된 메뉴코드와 현재 메뉴코드가 같을 시 반환 */
            if(userMenuSelect.getMenuCode() == menuCode) return CustomResponse.equalMenuCode();
            userMenuSelect.setMenuCode(menuCode);
            
            userMenuSelectRepository.save(userMenuSelect);
        } catch (Exception exception) {
            exception.printStackTrace();
            CustomResponse.databaseError();
        }
            return CustomResponse.successs();
    }


}
