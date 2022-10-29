package com.cdh.server.controller;


import com.cdh.server.pojo.Department;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-24
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("system/basic/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation("查询所有部门")
    @GetMapping("listAll")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation("添加部门")
    @PostMapping("add")
    public Result addDept(@RequestBody Department dept){
        return departmentService.addDept(dept);
    }

    @ApiOperation("删除部门")
    @DeleteMapping("delete/{id}")
    public Result deleteDept(@PathVariable("id") Integer id){
        return departmentService.deleteDept(id);
    }
}
