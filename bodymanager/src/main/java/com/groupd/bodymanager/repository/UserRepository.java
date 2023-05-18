package com.groupd.bodymanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupd.bodymanager.entity.UserEntity;
import com.groupd.bodymanager.entity.UserMenuSelect;
import com.groupd.bodymanager.entity.primaryKey.UserPK;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

    public boolean existsByUserEmail(String email);
    public boolean existsByUserNickname(String nickName);
    public boolean existsByUserPhoneNumber(String phoneNumber);

    public UserEntity findByUserEmail(String email);
    public UserEntity findByUserCode(int userCode);
}
