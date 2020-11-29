package com.report.utils;

import com.report.bean.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by mingku on 2020/11/20.
 */
public class Util {
    public static UserInfo getCurrentUser() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //System.out.println(user);
        return user;
    }
}
