package com.report.controller.admin;

import com.report.bean.RespInfo;
import com.report.bean.RoleInfo;
import com.report.bean.UserInfo;
import com.report.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mingku on 2020/11/20.
 */
@RestController
@RequestMapping("/admin")
public class UserManaController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserInfo> getUserByNickname(String nickname) {
        return userService.getUserByNickname(nickname);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public UserInfo getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<RoleInfo> getAllRole() {
        return userService.getAllRole();
    }

    @RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
    public RespInfo updateUserEnabled(Boolean enabled, Integer uid) {
        if (userService.updateUserEnabled(enabled, uid) == 1) {
            return new RespInfo("success", "更新成功!");
        } else {
            return new RespInfo("error", "更新失败!");
        }
    }

    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    public RespInfo deleteUserById(@PathVariable Integer uid) {
        if (userService.deleteUserById(uid) == 1) {
            return new RespInfo("success", "删除成功!");
        } else {
            return new RespInfo("error", "删除失败!");
        }
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    public RespInfo updateUserRoles(Integer[] rids, Integer id) {
        if (userService.updateUserRoles(rids, id) == rids.length) {
            return new RespInfo("success", "更新成功!");
        } else {
            return new RespInfo("error", "更新失败!");
        }
    }
}
