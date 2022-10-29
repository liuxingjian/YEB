package com.cdh.server.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cdh.server.pojo.Employee;
import com.cdh.server.pojo.rest.PageBean;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IEmployeeService;
import com.cdh.server.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "员工账套设置")
@RestController
@RequestMapping("/salary/sobcfg")
public class SalaryCfgController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "查询所有员工账套")
    @PostMapping("listAll")
    public PageBean listEmployeeWithSal(@RequestParam(defaultValue = "1") Integer currentPage,
                                        @RequestParam(defaultValue = "10") Integer size){
        return employeeService.listEmployeeWithSal(currentPage,size);
    }


    @ApiOperation(value = "更新员工账套")
    @PostMapping("update/{eid}/{sid}")
    public Result updateEmp(@PathVariable("eid") Integer eId,@PathVariable("sid") Integer sId){
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("salaryId",sId).eq("id",eId);
        if (employeeService.update(updateWrapper)){
            return Result.success("员工账套更新成功");
        }
        return Result.error("员工账套更新失败");
    }
}
