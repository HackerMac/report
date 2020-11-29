package com.report.dao;

import com.report.bean.RoleUserInfo;
import org.springframework.data.repository.query.Param;


public interface RoleUserDao {
    int addRoleUser(@Param("roleUsers")RoleUserInfo roleUser, @Param("id") Integer uid);

}
