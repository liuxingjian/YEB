package com.cdh.server.controller.login;

import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.pojo.vo.AdminLoginVo;
import com.cdh.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author by cdh
 * @description:
 * @Date: Created in 18:12 on 2021/4/18
 */
@Api(tags = "登录管理")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登陆之后返回token")
    @PostMapping("login")
    public Result login(@RequestBody AdminLoginVo adminLoginVo, HttpServletRequest request){
        return adminService.login(adminLoginVo,request);
    }

    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("admin/info")
    public Admin getAdminInfo(Principal principal){
        if (null == principal){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("logout")
    public Result logout(){
        return Result.success("注销成功");
    }
}
