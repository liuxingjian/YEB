package com.cdh.server.controller;


import com.cdh.server.pojo.Position;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IPositionService;
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
@Api(tags = "职位管理")
@RestController
@RequestMapping("system/basic/pos")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @ApiOperation("查询所有职位信息")
    @GetMapping("listAll")
    public List<Position> queryAllPositions(){
        return positionService.list();
    }

    @ApiOperation("添加职位信息")
    @PostMapping("add")
    public Result addPosition(@RequestBody Position position){
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @ApiOperation("更新职位信息")
    @PostMapping("update")
    public Result updatePosition(@RequestBody Position position){
        if (positionService.updateById(position)){
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @ApiOperation("删除职位信息")
    @DeleteMapping("delete/{id}")
    public Result deletePosition(@PathVariable Integer id){
        if (positionService.removeById(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @ApiOperation("批量删除职位信息")
    @DeleteMapping("deleteBatch")
    public Result deletePositionByBatch(Integer[] ids){
        if (positionService.removeByIds(Arrays.asList(ids))){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
