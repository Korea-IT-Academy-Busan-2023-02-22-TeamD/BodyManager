package com.groupd.bodymanager.dto.response.menu;

import java.util.ArrayList;

import java.util.List;

import com.groupd.bodymanager.dto.response.ResponseDto;
import com.groupd.bodymanager.entity.resultSet.MenuListResultSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetMenuDetailListResponseDto extends ResponseDto {
    private List<MenuDetail> menuDetailList;

    public GetMenuDetailListResponseDto(List<MenuListResultSet> resultSet){
        super("SU","Success");

        List<MenuDetail> menuDetailList = new ArrayList<>();
        
        for(MenuListResultSet result : resultSet) {
            MenuDetail menuDetail = new MenuDetail(result);
            menuDetailList.add(menuDetail);
        }
        this.menuDetailList = menuDetailList;
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MenuDetail {
    
    private String menuCode;
    private String time;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
  
    public MenuDetail(MenuListResultSet resultSet){
        this.menuCode = resultSet.getMenuCode();
        this.time = resultSet.getTime();
        this.monday = resultSet.getMonday();
        this.tuesday = resultSet.getTuesday();
        this.wednesday = resultSet.getWednesday();
        this.thursday = resultSet.getThursady();
        this.friday = resultSet.getFriday();
        this.saturday = resultSet.getSaturday();
        this.sunday = resultSet.getSunday();
    }

}
