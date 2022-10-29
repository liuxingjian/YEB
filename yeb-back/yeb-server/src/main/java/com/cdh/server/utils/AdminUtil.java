package com.cdh.server.utils;

import com.cdh.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author by cdh
 * @description:获取当前登录的管理员
 * @Date: Created in 21:23 on 2021/4/24
 */
public class AdminUtil {
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }
}
