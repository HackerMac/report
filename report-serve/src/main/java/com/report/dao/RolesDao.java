package com.report.dao;

import com.report.bean.RoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RolesDao {
    int addRoles(@Param("roles") String[] roles, @Param("uid") Integer uid);

    List<RoleInfo> getRolesByUid(Integer uid);
}
