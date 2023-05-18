package com.groupd.bodymanager.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.groupd.bodymanager.dto.response.ResponseDto;


public class CustomResponse {
    public static ResponseEntity<ResponseDto> successs() {
        ResponseDto errorBody = new ResponseDto("SU", "Sucess");
        return ResponseEntity.status(HttpStatus.OK).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto errorBody = new ResponseDto("DE", "Database Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> validationFaild() {
        ResponseDto errorBody = new ResponseDto("VF", "Request Parameter Validation Failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> notExistUserCode() {
        ResponseDto errorBody = new ResponseDto("NC", "Non-Existent User Code");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> notExistBoardNumber() {
        ResponseDto errorBody = new ResponseDto("NB", "None-existent Board Number");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> notExistRoutineNumber() {
        ResponseDto errorBody = new ResponseDto("NR", "Non-Existent Routine Number");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }


    public static ResponseEntity<ResponseDto> notExistMenuCode() {
        ResponseDto errorBody = new ResponseDto("NMC", "Non-Existent Menu Code");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> existUserEmail() {

        ResponseDto errorBody = new ResponseDto("EU", "Existent User Email");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> existUserNickname() {

        ResponseDto errorBody = new ResponseDto("EN", "Existent User Nickname");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> existUserPhoneNumber() {

        ResponseDto errorBody = new ResponseDto("EP", "Existent User Phone Number");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> signInFailed() {

        ResponseDto errorBody = new ResponseDto("SF", "Sign In Failed");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }
  
    public static ResponseEntity<ResponseDto> noneMatchedPassword(){
        ResponseDto errorBody = new ResponseDto("NM", "None Matched New Password with New PasswordCheck");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> notExistUserEmail() {
        ResponseDto errorBody = new ResponseDto("NU", "Non-Existent User Email");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> equalMenuCode() {
        ResponseDto errorBody = new ResponseDto("EM", "Adjusted MenuCode Equal Current MenuCode");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> noPermission() {

        ResponseDto errorBody = new ResponseDto("NP", "No-permission");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

}