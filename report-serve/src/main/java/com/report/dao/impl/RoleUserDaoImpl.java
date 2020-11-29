package com.report.dao.impl;

import com.report.bean.RoleUserInfo;
import com.report.dao.RoleUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class RoleUserDaoImpl implements RoleUserDao {
    @Autowired
    private MongoTemplate template;

    public RoleUserDaoImpl() {
        super();
    }

    @Override
    public int addRoleUser(RoleUserInfo roleUser, Integer uid) {

        return 0;
    }
}
