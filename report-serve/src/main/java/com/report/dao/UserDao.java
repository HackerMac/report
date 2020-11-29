package com.report.dao;

import com.report.bean.RoleInfo;
import com.report.bean.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserDao {
    UserInfo loadUserByUsername(@Param("username") String username);

    long reg(UserInfo user);

    int addUser(@Param("user") UserInfo user);

    int updateUserEmail(@Param("email") String email, @Param("id") Integer id);

    List<UserInfo> getUserByNickname(@Param("nickname") String nickname);

    List<RoleInfo> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("uid") Integer uid);

    int deleteUserById(Integer uid);

    int deleteUserRolesByUid(Integer id);

    int setUserRoles(@Param("rids") Integer[] rids, @Param("id") Integer id);

    UserInfo getUserById(@Param("id") Integer id);
}
