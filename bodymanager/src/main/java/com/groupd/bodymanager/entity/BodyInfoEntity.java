package com.groupd.bodymanager.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.groupd.bodymanager.dto.request.bodyInfo.PostBodyInfoRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BodyInfo")
@Entity(name = "BodyInfo")
public class BodyInfoEntity {

    @Id
    private int userCode;
    @NotNull
    private double height;
    @NotNull
    private double weight;
    @NotNull
    private double muscleMass;
    @NotNull
    private double fatRate;
    @NotBlank
    private String recordDate;
    @NotNull
    private double bmiIndex;
    @NotBlank
    private String bmiResult;

    public BodyInfoEntity(PostBodyInfoRequestDto dto) {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = 
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String recordDate = simpleDateFormat.format(now);

        this.userCode = dto.getUserCode();
        this.height = dto.getHeight();
        this.weight = dto.getWeight();
        this.muscleMass = dto.getMuscleMass();
        this.fatRate = dto.getFatRate();
        this.recordDate = recordDate;

    }

    
}
