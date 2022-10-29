package com.cdh.server.controller;


import com.cdh.server.pojo.Salary;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "工资管理")
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation("查询工资账套")
    @GetMapping("listAll")
    public List<Salary> queryAllSalary(){
        return salaryService.list();
    }

    @ApiOperation("添加工资账套")
    @PostMapping("add")
    public Result addJobLevel(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @ApiOperation("更新工资账套")
    @PostMapping("update")
    public Result updateJobLevel(@RequestBody Salary salary){
        if (salaryService.updateById(salary)){
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @ApiOperation("删除工资账套")
    @DeleteMapping("delete/{id}")
    public Result deleteJobLevel(@PathVariable Integer id){
        if (salaryService.removeById(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

}
