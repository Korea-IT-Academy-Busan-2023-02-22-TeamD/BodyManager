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
import com.groupd.bodymanager.dto.request.user.DeleteUserRequestDto;
import com.groupd.bodymanager.dto.request.user.PatchUserRequestDto;
import com.groupd.bodymanager.dto.request.user.PostManagerRequestDto;
import com.groupd.bodymanager.dto.request.user.SignInRequestDto;
import com.groupd.bodymanager.dto.request.user.SignUpRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.user.GetAuthResponseDto;
import com.groupd.bodymanager.dto.response.user.GetUserResponseDto;
import com.groupd.bodymanager.entity.BodyInfoEntity;
import com.groupd.bodymanager.entity.ManagerEntity;
import com.groupd.bodymanager.entity.MileageEntity;
import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.provider.JwtProvider;
import com.groupd.bodymanager.repository.BodyInfoRepository;
import com.groupd.bodymanager.repository.ManagerRepository;
import com.groupd.bodymanager.repository.MileageRepository;
import com.groupd.bodymanager.repository.UserRepository;
import com.groupd.bodymanager.service.UserService;



@Service
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private ManagerRepository managerRepository;
    private MileageRepository mileageRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private BodyInfoRepository bodyInfoRepository;
    @Autowired
    public UserServiceImplement(
            UserRepository userRepository,
            JwtProvider jwtProvider, ManagerRepository managerRepository, MileageRepository mileageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtProvider = jwtProvider;
        this.managerRepository = managerRepository;
        this.mileageRepository = mileageRepository;
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

        try { 
            // TODO 존재하는 유저 이메일
            boolean existedUserEmail = userRepository.existsByUserEmail(userEmail);
            if (existedUserEmail)
                return CustomResponse.existUserEmail();

            // TODO 존재하는 유저 닉네임
            boolean existedUserNickname = userRepository.existsByUserNickname(userNickname);
            if (existedUserNickname)
                return CustomResponse.existUserNickname();

            // TODO 존재하는 유저 휴대전화 번호
            boolean existedUserPhoneNumber = userRepository.existsByUserPhoneNumber(userPhoneNumber);
            if (existedUserPhoneNumber)
                return CustomResponse.existUserPhoneNumber();

            String encodedPassword = passwordEncoder.encode(userPassword); // 유저 계정 생성 및 암호화 작업
            dto.setUserPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);
            System.out.println(userEntity.toString());
            int userCode = userEntity.getUserCode();

            MileageEntity mileageEntity = new MileageEntity();
            mileageEntity.setUserCode(userCode);
            mileageEntity.setAttendanceToday(false);
            mileageEntity.setAttendanceDate(null);
            mileageEntity.setAttendanceMileage(0);
            mileageRepository.save(mileageEntity);


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
            UserEntity userEntity = userRepository.findByUserEmail(userEmail);
            if (userEmail == null)
                return CustomResponse.signInFailed();

            // TODO 로그인 실패 (패스워드 x)
            String encordedPassword = userEntity.getUserPassword();
            boolean equaledPassword = passwordEncoder.matches(userPassword, encordedPassword);
            ;
            if (!equaledPassword)
                return CustomResponse.signInFailed();

            String jwt = jwtProvider.create(userEmail);
            int userCode = userEntity.getUserCode();
            body = new GetAuthResponseDto(jwt, userCode);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public ResponseEntity<? super GetUserResponseDto> addManager(PostManagerRequestDto dto) {
        GetUserResponseDto body = null;
        String addEmail = dto.getManagerEmail();
        int addCode;

        try {
            // TODO 이메일 일치 확인 - 유저이메일에서 확인하는거고...
            boolean isExistEmail = userRepository.existsByUserEmail(addEmail);
            if (!isExistEmail) {
                return CustomResponse.notExistUserEmail();
            } // 오류 반환 <이메일 없숨!>
              // TODO 이메일 중복 확인 - 매니저이메일 리스트 안에서 확인하는것..
            boolean isAlreadyAdded = managerRepository.existsByManagerEmail(addEmail);
            if (isAlreadyAdded) {
                return CustomResponse.existUserEmail();// 오류반환 <이메일 중복>
            }
            UserEntity userEntity = userRepository.findByUserEmail(addEmail);
            
            addCode = userEntity.getUserCode();
            ManagerEntity managerEntity = new ManagerEntity(addEmail);
            managerRepository.save(managerEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.successs();
    }

    // 사용자 조회
    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(Integer userCode) {
        GetUserResponseDto body = null;

        try {

            if (userCode == null) {
                return CustomResponse.validationFaild();
            }
            // todo 존재하지 않는 회원코드
            // 유저코드 조회
            UserEntity userEntity = userRepository.findByUserCode(userCode);
            // 존재하지 않는 유저 코드 조회시
            if (userEntity == null) {
                return CustomResponse.notExistUserCode();
            }

            body = new GetUserResponseDto(userEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public ResponseEntity<ResponseDto> patchUser(PatchUserRequestDto dto) {

        ResponseDto responseBody = null;
        UserEntity userEntity = userRepository.findByUserEmail(dto.getUserEmail());
        String userPassword = userEntity.getUserPassword();
        String userNewPassword = dto.getUserNewPassword();
        String userNewPasswordCheck = dto.getUserNewPasswordCheck();
        String userNickname = dto.getUserNickname();
        String userPhoneNumber = dto.getUserPhoneNumber();
        String userAddress = dto.getUserAddress();
        String userGender = dto.getUserGender();
        int userAge = dto.getUserAge();

        try {
            boolean isRightPassword = passwordEncoder.matches(dto.getUserPassword(), userPassword);
            if (!isRightPassword) { // TODO 기존 비밀번호 불일치
                return CustomResponse.noPermission();
            }
            boolean isMatchedPassword = userNewPassword.equals(userNewPasswordCheck);
            if (!isMatchedPassword) { // TODO 새로운 비밀번호와 비밀번호 확인간의 불일치
                return CustomResponse.noneMatchedPassword();
            }
            boolean isExistNickname = userRepository.existsByUserNickname(dto.getUserNickname());
            if (isExistNickname) { // TODO 존재하는 유저 닉네임
                return CustomResponse.existUserNickname();
            }
            boolean isExistPhoneNumber = userRepository.existsByUserPhoneNumber(dto.getUserPhoneNumber());
            if (isExistPhoneNumber) { // TODO 존재하는 유저 휴대전화 번호
                return CustomResponse.existUserPhoneNumber();
            }
            boolean changePassword = userNewPassword.isBlank();
            if (!changePassword) {
                userPassword = passwordEncoder.encode(userNewPassword);
                userEntity.setUserPassword(userPassword); // 비밀번호 변경
            }
            boolean changeNickname = userNickname.isBlank();
            if (!changeNickname) {
                userEntity.setUserNickname(userNickname); // 닉네임 변경
            }
            boolean changePhoneNumber = userPhoneNumber.isBlank();
            if (!changePhoneNumber) {
                userEntity.setUserPhoneNumber(userPhoneNumber); // 휴대전화번호 변경
            }
            // 나머지는 변경된 사항 저장
            userEntity.setUserAddress(userAddress);
            userEntity.setUserGender(userGender);
            userEntity.setUserAge(userAge);

            userRepository.save(userEntity); // 변경된 유저 정보 저장

        } catch (Exception exception) {
            exception.printStackTrace();
            responseBody = new ResponseDto("DE", "DatabaseError");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(responseBody);
        }

        return CustomResponse.successs();
    }

    @Override
    public ResponseEntity<ResponseDto> deleteUser(String userEmail, DeleteUserRequestDto dto) {

        GetAuthResponseDto body = null;

        String userEmailCheck = dto.getUserEmailCheck();
        String userPassword = dto.getUserPassword();

        try {
            // todo 로그인 상태에서 로그인된 이메일을 어떻게 가져오는지
            boolean matchId = userEmail.equals(userEmailCheck);
            if (!matchId)
                return CustomResponse.signInFailed();

            // 로그인 실패 (패스워드 x)
            UserEntity userEntity = userRepository.findByUserEmail(userEmailCheck);
            String encordedPassword = userEntity.getUserPassword();
            boolean equaledPassword = passwordEncoder.matches(userPassword, encordedPassword);
            
            if (!equaledPassword)
                return CustomResponse.signInFailed();

            Integer userCode = userEntity.getUserCode();
            MileageEntity mileageEntity = mileageRepository.findByUserCode(userCode);
            BodyInfoEntity bodyInfoEntity = bodyInfoRepository.findByUserCode(userCode);
            // String jwt = jwtProvider.create(userEmail);

            // body = new GetAuthResponseDto(jwt, userCode);
            bodyInfoRepository.delete(bodyInfoEntity);
            mileageRepository.delete(mileageEntity);
            userRepository.delete(userEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.successs();
    }
}
