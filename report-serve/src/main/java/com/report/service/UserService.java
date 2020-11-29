package com.report.service;

import com.report.bean.RoleInfo;
import com.report.bean.UserInfo;
import com.report.dao.RolesDao;
import com.report.dao.UserDao;
import com.report.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Created by mingku on 2020/11/20.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Autowired
    RolesDao rolesDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserInfo user = userDao.loadUserByUsername(s);
        if (user == null) {
            //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            return new UserInfo();
        }
        //查询用户的角色信息，并返回存入user中
        List<RoleInfo> roles = rolesDao.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(UserInfo user) {
        UserInfo loadUserByUsername = userDao.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        //配置用户的角色，默认都是普通用户
        String[] roles = new String[]{"2"};
        int i = rolesDao.addRoles(roles, user.getId());
        boolean b = i == roles.length;
        //System.out.println(b);
        if (!b) {
            return 2;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);//用户可用
        userDao.reg(user);
        long result = userDao.addUser(user);
        return result == 1 ? 1 : 0;
    }

    public int updateUserEmail(String email) {
        return userDao.updateUserEmail(email, Util.getCurrentUser().getId());
    }

    public List<UserInfo> getUserByNickname(String nickname) {
        return userDao.getUserByNickname(nickname);
    }

    public List<RoleInfo> getAllRole() {
        return userDao.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Integer uid) {
        return userDao.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Integer uid) {
        return userDao.deleteUserById(uid);
    }

    public int updateUserRoles(Integer[] rids, Integer id) {
        int i = userDao.deleteUserRolesByUid(id);
        return userDao.setUserRoles(rids, id);
    }

    public UserInfo getUserById(Integer id) {
        return userDao.getUserById(id);
    }
}
