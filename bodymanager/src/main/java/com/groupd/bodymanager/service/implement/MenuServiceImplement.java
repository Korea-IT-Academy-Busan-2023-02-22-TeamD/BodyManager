package com.groupd.bodymanager.service.implement;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupd.bodymanager.common.CustomResponse;
import com.groupd.bodymanager.dto.request.board.PatchBoardRequestDto;
import com.groupd.bodymanager.dto.request.menu.MenuRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetMenuDetailListResponseDto;
import com.groupd.bodymanager.dto.response.menu.PatchMenuResponseDto;
import com.groupd.bodymanager.dto.response.menu.GetUserMenuResponseDto;
import com.groupd.bodymanager.entity.MenuDetailEntity;
import com.groupd.bodymanager.entity.MenuEntity;
import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.entity.UserMenuSelect;
import com.groupd.bodymanager.entity.primaryKey.SelectPK;
import com.groupd.bodymanager.entity.primaryKey.UserPK;
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
        MenuRepository menuRepository, UserRepository userRepository, 
        MenuDetailRepository menuDetailRepository,UserMenuSelectRepository userMenuSelectRepository){
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.menuDetailRepository = menuDetailRepository;
        this.userMenuSelectRepository = userMenuSelectRepository;

    }

    //*1.유저코드와 메뉴코드를 등록 */
    @Override
    public ResponseEntity<ResponseDto> postMenuCodeAndUserCode(String email, MenuRequestDto dto) {
        String menuCode = dto.getMenuCode();
        String correctMenuCode = menuCode.toUpperCase();
        UserEntity userEntity = userRepository.findByUserEmail(email);
        Integer userCode = userEntity.getUserCode();
        menuCode = menuCode.toUpperCase();
        try {
            //*필수 값을 입력 */
            if (menuCode == null)  return CustomResponse.validationFaild();
            // *존재하지 않는 메뉴코드 반환 */
            boolean existedByMenuCode = menuRepository.existsByMenuCode(correctMenuCode);
            if (!existedByMenuCode) return CustomResponse.notExistMenuCode();
            //* 이미 등록한 유저 경우 수정*/
            boolean existsByUserCode = userMenuSelectRepository.existsByUserCode(userCode);
            
            if (existsByUserCode) {
                patchMenuCode(email, dto);
            }else{
            // *Response 데이터를 레포지토리에 저장 */
            
            UserMenuSelect userMenuSelect = new UserMenuSelect(userCode, correctMenuCode);
            userMenuSelectRepository.save(userMenuSelect);
        }
        } catch (Exception exceptione) {
            exceptione.printStackTrace();
            // *데이터베이스 오류 */
            return CustomResponse.databaseError();
        }
        // *성공 반환 */
        return CustomResponse.successs();
    }


    @Override //*모든 식단을 조회합니다. */
    public ResponseEntity<? super GetMenuDetailListResponseDto> getMenuList() {
        GetMenuDetailListResponseDto body = null;
        try {

            List<MenuListResultSet> resultSet = menuDetailRepository.getMenuDetailList();
            body = new GetMenuDetailListResponseDto(resultSet);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }



@Override //*회원이 선택한 식단을 조회합니다. */
public ResponseEntity<? super GetUserMenuResponseDto> getMenu(Integer userCode) {
    GetUserMenuResponseDto body = null;
    try {
        
        if (userCode == null)
        return CustomResponse.validationFaild();
    UserMenuSelect userMenuSelect = userMenuSelectRepository.findByUserCode(userCode);
    //*UserMenuSelect에 회원정보가 없을시 */
    if(userMenuSelect == null) return CustomResponse.notExistUserCode();
    String menuCode = userMenuSelect.getMenuCode();
    List<MenuDetailEntity> menuDetailEntities = menuDetailRepository.findByMenuCode(menuCode);

    body = new GetUserMenuResponseDto(userMenuSelect, menuDetailEntities);

    } catch (Exception exception) {
        exception.printStackTrace();
        return CustomResponse.databaseError();
    }

    return ResponseEntity.status(HttpStatus.OK).body(body);
}



    @Override // 메뉴 코드를 수정
    public ResponseEntity<ResponseDto> patchMenuCode(String email, MenuRequestDto dto) {
        PatchMenuResponseDto body = null;
        UserEntity userEntity = userRepository.findByUserEmail(email);
        Integer userCode = userEntity.getUserCode();
        String menuCode = dto.getMenuCode();
        String correctMenuCode = menuCode.toUpperCase();

        try {
            if((email == null) || (dto == null)) return CustomResponse.validationFaild();
            UserMenuSelect userMenuSelect = userMenuSelectRepository.findByUserCode(userCode);
            //* 수정된 메뉴코드와 현재 메뉴코드가 같을 시 반환 */
            boolean isSameMenuCode = userMenuSelect.getMenuCode().equals(correctMenuCode);
            if(isSameMenuCode) return CustomResponse.equalMenuCode();
            userMenuSelectRepository.patchMenuCode(correctMenuCode, userCode);
            body = new PatchMenuResponseDto(userCode,correctMenuCode);
                        
        } catch (Exception exception) {
            exception.printStackTrace();
            CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }



}
