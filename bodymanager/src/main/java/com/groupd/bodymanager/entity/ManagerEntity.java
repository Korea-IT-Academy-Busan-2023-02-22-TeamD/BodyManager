package com.groupd.bodymanager.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Email;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Manager")
@Table(name = "Manager")
@Data
@NoArgsConstructor
// @IdClass(managerPK.class)
public class ManagerEntity {

    @Id
    @Email
    String managerEmail;


    public ManagerEntity(String email) {
        
        this.managerEmail = email;
    }

}
