package com.report.dao.impl;

import com.report.bean.RoleInfo;
import com.report.bean.RoleUserInfo;
import com.report.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class RolesDaoImpl implements RolesDao {
    @Autowired
    private MongoTemplate template;

    public RolesDaoImpl() {
        super();
    }

    @Override
    public int addRoles(String[] roles, Integer uid) {
        RoleUserInfo roleUser = new RoleUserInfo();
        List<RoleUserInfo> all = template.findAll(RoleUserInfo.class);
        Collections.sort(all, new Comparator<RoleUserInfo>() {
            @Override
            public int compare(RoleUserInfo o1, RoleUserInfo o2) {
                return o1.getId() - o2.getId();
            }
        });
        Integer count = 1;
        Boolean flg = false;
        for (RoleUserInfo a : all) {
            if (a.getId() != count) {
                roleUser.setId(count);
                flg = true;
                break;
            }
            count += 1;
        }
        if (!flg) roleUser.setId(count);
        roleUser.setUid(uid);
        //System.out.println(roleUser.getRid());
        count = 0;
        for (String role : roles) {
            roleUser.setRid(Integer.valueOf(role).intValue());
            RoleUserInfo t_role_user = template.insert(roleUser, "t_roles_user");
            if (t_role_user != null) {
                count += 1;
            }
        }
        return count;
    }

    @Override
    public List<RoleInfo> getRolesByUid(Integer uid) {
        List<RoleUserInfo> roleUserInfos = template.find(Query.query(Criteria.where("uid").is(uid)), RoleUserInfo.class);
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();

        for (RoleUserInfo roleUserInfo : roleUserInfos) {
            roleInfos.add(template.findOne(Query.query(Criteria.where("id").is(roleUserInfo.getRid())), RoleInfo.class));
        }
        return roleInfos;
    }
}
