package com.report.dao.impl;

import cn.hutool.core.date.DateTime;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.report.bean.ArticleInfo;
import com.report.bean.RoleInfo;
import com.report.bean.RoleUserInfo;
import com.report.bean.UserInfo;
import com.report.dao.UserDao;
import com.report.utils.Util;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private MongoTemplate template;

    public UserDaoImpl() {
        super();
    }

    @Override
    public UserInfo loadUserByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return template.findOne(query, UserInfo.class);
    }

    @Override
    public long reg(UserInfo user) {
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("emial", user.getEmail());
        UpdateResult updateResult = template.updateFirst(query, update, UserInfo.class);
        if (updateResult.getMatchedCount() == 1)
            return 1;
        return 0;
    }


    @Override
    public int updateUserEmail(String email, Integer id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("emial", email);
        UpdateResult updateResult = template.updateFirst(query, update, UserInfo.class);
        if (updateResult.getMatchedCount() == 1)
            return 1;
        return 0;
    }

    @Override
    public List<UserInfo> getUserByNickname(String nickname) {
        Query pageQuery = new Query().limit(20);
        List<UserInfo> all = template.find(pageQuery, UserInfo.class);
        System.out.println(all.size());
        List<UserInfo> all1 = new ArrayList<>();
        //for (UserInfo a : all) {
        //    // 如果是当前用户，则跳过
        //    if (a.getNickname() == nickname) {
        //        continue;
        //    }
        //    Query query = new Query(Criteria.where("uid").is(a.getId()));
        //    List<RoleUserInfo> roleUserInfos = template.find(query, RoleUserInfo.class);
        //    List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        //    for (RoleUserInfo roleUserInfo : roleUserInfos) {
        //        Query q = new Query(Criteria.where("id").is(roleUserInfo.getRid()));;
        //        roleInfos.add(template.findOne(q, RoleInfo.class));
        //    }
        //    List<RoleInfo> roles = new ArrayList<>();
        //    for (RoleInfo roleInfo : roleInfos) {
        //        roles.add(roleInfo);
        //    }
        //    all1.add(a);
        //}
        return all;
    }

    @Override
    public List<RoleInfo> getAllRole() {
        List<RoleInfo> reuslt = template.findAll(RoleInfo.class);
        return reuslt;
    }

    @Override
    public int updateUserEnabled(Boolean enabled, Integer uid) {
        Query query = new Query(Criteria.where("id").is(uid));
        Update update = new Update();
        update.set("enabled", enabled);
        UpdateResult updateResult = template.updateFirst(query, update, UserInfo.class);
        if (updateResult.getMatchedCount() == 1) {
            return 1;
        }
        return 0;
    }


    @Override
    public int deleteUserById(Integer uid) {
        DeleteResult result = template.remove(Query.query(Criteria.where("id").is(uid)), UserInfo.class);
        return (int)result.getDeletedCount();
    }

    @Override
    public int deleteUserRolesByUid(Integer id) {
        DeleteResult result = template.remove(Query.query(Criteria.where("uid").is(id)), RoleUserInfo.class);
        return (int)result.getDeletedCount();
    }

    @Override
    public int setUserRoles(Integer[] rids, Integer id) {
        RoleUserInfo roleUser = new RoleUserInfo();
        roleUser.setUid(id);
        int count = 0;
        for (Integer rid : rids) {
            template.insert(roleUser);
            count += 1;
        }
        return count;
    }

    @Override
    public UserInfo getUserById(Integer id) {
        return template.findById(id, UserInfo.class);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int addUser(UserInfo user) {
        List<UserInfo> all = template.findAll(UserInfo.class);
        Collections.sort(all,new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo a, UserInfo b) {
                return a.getId() - b.getId();
            }
        });
        Integer count = 1;
        Boolean flg = false;
        // 防止中途id离散分布
        for (UserInfo a : all) {
            if (a.getId() != count) {
                user.setId(count);
                flg = true;
                break;
            }
            count += 1;
        }
        if (!flg) user.setId(count);
        user.setRegTime(new DateTime(System.currentTimeMillis()));
        UserInfo userInfo = template.insert(user, "t_user");
        if (userInfo == null) {
            return 1;
        }
        return 0;
    }
}
