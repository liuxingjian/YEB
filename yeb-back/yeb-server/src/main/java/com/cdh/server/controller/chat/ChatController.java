package com.cdh.server.controller.chat;

import com.cdh.server.pojo.Admin;
import com.cdh.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation("获取所有管理员")
    @GetMapping("listAll")
    public List<Admin> getAllAdmin(String keywords){
        return adminService.getAllAdmin(keywords);
    }

}
