package com.groupd.bodymanager.service.implement;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupd.bodymanager.common.CustomResponse;
import com.groupd.bodymanager.dto.request.mileage.PostMileageRequestDto;
import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.dto.response.mileage.GetMileageResponseDto;
import com.groupd.bodymanager.entity.MileageEntity;
import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.repository.MileageRepository;
import com.groupd.bodymanager.repository.UserRepository;
import com.groupd.bodymanager.service.MileageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MileageServiceImplement implements MileageService {
    private  final UserRepository userRepository;
    private  final MileageRepository mileageRepository;

    @Override
    public ResponseEntity<ResponseDto> postMileage(String userEmail, PostMileageRequestDto dto) {

        ResponseDto body = null;
        Date now = new Date();
        SimpleDateFormat simpleDateFormat =
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String attendanceDate = simpleDateFormat.format(now);
        
        
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);
        int userCode = userEntity.getUserCode();
        try {
            // 존재하지 않는 유저코드 반환
            UserEntity existeduserCode = userRepository.findByUserCode(userCode);
            if(existeduserCode == null) {
                ResponseDto errorBody = new ResponseDto("NC", "Non-Existent User Code");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
            }
            
            MileageEntity mileageEntity = mileageRepository.findByUserCode(userCode);
            // 이미 출석했는지 확인
            // boolean attendanceResult = dto.isAttendanceResult();
            MileageEntity attendanceStatus = mileageRepository.findByUserCodeAndAttendanceToday(userCode, true);
            boolean attendanceToday = attendanceStatus != null && attendanceStatus.isAttendanceToday();
            // boolean isAlreadyAttend = attendanceResult == !attendanceToday; // 둘다 T 면 T다. 
            // 출석을 하면 T로 된다. F가 됨, = 같음 -> T가됨. ==>>> 출석한 상태
            // F가되면 출석을 하지 않은 상태.
            if(attendanceToday) {
                ResponseDto errorBody = new ResponseDto("AT", "Already Attended Today");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
            } 
                        
            // 출석 및 마일리지 +10
            mileageEntity.setAttendanceToday(true);
            mileageEntity.setAttendanceDate(attendanceDate);
            mileageEntity.setAttendanceMileage(mileageEntity.getAttendanceMileage() + 10);
            mileageRepository.save(mileageEntity);

            body = new ResponseDto("SU", "Success");
            
        } catch (Exception exception) {
            // 데이터베이스 오류 반환 //
            exception.printStackTrace();
            ResponseDto errorBody = new ResponseDto("DE", "Database Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }
        
        // 성공반환
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public ResponseEntity<? super GetMileageResponseDto> getMileage(Integer userCode) {
        
        GetMileageResponseDto body = null;
        
        try {

            if(userCode == null) return CustomResponse.validationFaild();
            
            // 존재하지 않는 유저코드 반환
            UserEntity existeduserCode = userRepository.findByUserCode(userCode);
            if(existeduserCode == null) {
            ResponseDto errorBody = new ResponseDto("NC", "Non-Existent User Code");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
            }       
            
            MileageEntity mileageEntity = mileageRepository.findByUserCode(userCode);
            
            Integer totalMileage = mileageEntity.getAttendanceMileage();
            body = new GetMileageResponseDto(userCode, totalMileage);
        } catch (Exception exception) {
            // 데이터베이스 오류 반환 //
            exception.printStackTrace();
            ResponseDto errorBody = new ResponseDto("DE", "Database Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }




}
