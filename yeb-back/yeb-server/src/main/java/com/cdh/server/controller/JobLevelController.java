package com.cdh.server.controller;


import com.cdh.server.pojo.Joblevel;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IJoblevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "职称管理")
@RestController
@RequestMapping("system/basic/joblevel")
public class JobLevelController {

    @Autowired
    private IJoblevelService jobLevelService;

    @ApiOperation("查询所有职称信息")
    @GetMapping("listAll")
    public List<Joblevel> queryAllJobLevels(){
        return jobLevelService.list();
    }

    @ApiOperation("添加职称信息")
    @PostMapping("add")
    public Result addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if (jobLevelService.save(joblevel)){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @ApiOperation("更新职称信息")
    @PostMapping("update")
    public Result updateJobLevel(@RequestBody Joblevel joblevel){
        if (jobLevelService.updateById(joblevel)){
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @ApiOperation("删除职称信息")
    @DeleteMapping("delete/{id}")
    public Result deleteJobLevel(@PathVariable Integer id){
        if (jobLevelService.removeById(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @ApiOperation("批量删除职称信息")
    @DeleteMapping("deleteBatch")
    public Result deleteJobLevelByBatch(Integer[] ids){
        if (jobLevelService.removeByIds(Arrays.asList(ids))){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
