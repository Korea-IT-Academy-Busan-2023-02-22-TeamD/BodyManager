package com.groupd.bodymanager.entity.primaryKey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
public class UserPK implements Serializable{
    @Column(name ="user_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userCode;
    @Column(name ="user_email")
    private String userEmail;
}
