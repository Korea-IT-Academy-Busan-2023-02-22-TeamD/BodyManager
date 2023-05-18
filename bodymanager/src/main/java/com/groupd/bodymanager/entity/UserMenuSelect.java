package com.groupd.bodymanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.groupd.bodymanager.entity.primaryKey.SelectPK;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserMenuSelect")
@Entity(name = "UserMenuSelect")
@IdClass(SelectPK.class)
public class UserMenuSelect {
    @Id
    private String menuCode;
    @Id
    private int userCode;
    public UserMenuSelect(UserEntity userEntity, MenuEntity menuEntity){
        this.menuCode = menuEntity.getMenuCode();
        this.userCode = userEntity.getUserCode();
    }   
}
