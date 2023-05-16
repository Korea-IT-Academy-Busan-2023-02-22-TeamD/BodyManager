package com.groupd.bodymanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.groupd.bodymanager.dto.request.menu.PostMenuRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Menu")
@Entity(name = "Menu")
public class MenuEntity {
    @Id
    private String menuCode;
   


    public MenuEntity(PostMenuRequestDto dto) {
        this.menuCode = dto.getMenuCode();

    }
    
}
