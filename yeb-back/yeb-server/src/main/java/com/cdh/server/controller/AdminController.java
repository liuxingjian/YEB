package com.cdh.server.controller;


import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IAdminService;
import com.cdh.server.utils.FastDfsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "管理员管理")
@RestController
@RequestMapping("system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation("获取所有管理员")
    @GetMapping("listAll")
    public List<Admin> getAllAdmin(String keywords){
        return adminService.getAllAdmin(keywords);
    }

    @ApiOperation("更新当前登录用户信息")
    @PostMapping("info")
    public Result updateAdminIfo(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)){
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(authentication,
                    null, authentication.getAuthorities()));
            return Result.success("更新成功!");
        }
        return Result.error("更新失败!");
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("userFace")
    public Result updateAdminUserFace(MultipartFile file,Integer adminId,Authentication authentication){
        String[] filePath = FastDfsUtil.upload(file);
        String url = FastDfsUtil.getTrackerUrl() + filePath[0] + "/" + filePath[1];
        return adminService.updateAdminUserFace(url,adminId,authentication);
    }

    @ApiOperation("更新用户密码")
    @PostMapping("pass")
    public Result updateAdminPass(@RequestBody Map<String,Object> info){
        String oldPass = (String)info.get("oldPass");
        String pass = (String)info.get("pass");
        Integer adminId = (Integer)info.get("adminId");
        return adminService.updateAdminPassWord(oldPass,pass,adminId);
    }

    @ApiOperation("更新管理员")
    @PostMapping("update")
    public Result updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return Result.success("更新成功!");
        }
        return Result.error("更新失败!");
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("delete/{id}")
    public Result deleteAdmin(@PathVariable("id") String id){
        if (adminService.removeById(id)){
            return Result.success("删除成功!");
        }
        return Result.error("删除失败!");
    }

    @ApiOperation("更新管理员角色")
    @PutMapping("updateAdminRole")
    public Result updateAdminRole(Integer adminId,Integer[] rids){
        return adminService.updateAdminRole(adminId,rids);
    }
}
