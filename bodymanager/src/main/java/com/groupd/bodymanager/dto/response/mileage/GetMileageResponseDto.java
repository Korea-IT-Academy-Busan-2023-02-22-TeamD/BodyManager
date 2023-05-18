package com.groupd.bodymanager.dto.response.mileage;


import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.entity.MileageEntity;
import com.groupd.bodymanager.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class GetMileageResponseDto extends ResponseDto{
    private Integer userCode;
    private Integer attendanceMileage;


    public GetMileageResponseDto(Integer userCode, Integer totalAttendanceMileage){
        super("SU", "Success");
        this.userCode = userCode;
        this.attendanceMileage = totalAttendanceMileage;
        
    
    }

}

