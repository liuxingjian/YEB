package com.cdh.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdh.server.pojo.Menu;
import com.cdh.server.pojo.MenuRole;
import com.cdh.server.pojo.Role;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IMenuRoleService;
import com.cdh.server.service.IMenuService;
import com.cdh.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("system/basic/permiss")
public class RoleController {

    public static final String PREFIX_ROLE = "ROLE_";
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation("查询所有角色")
    @GetMapping("listAll")
    public List<Role> queryAllRoles(){
        return roleService.list();
    }

    @ApiOperation("添加角色")
    @PostMapping("add")
    public Result addRole(@RequestBody Role role){
        if (!role.getName().startsWith(PREFIX_ROLE)){
            role.setName(PREFIX_ROLE +role.getName());
        }
        if (roleService.save(role)){
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @ApiOperation("删除角色")
    @DeleteMapping("delete/{id}")
    public Result deleteRole(@PathVariable Integer id){
        if (roleService.removeById(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @ApiOperation("查询所有菜单")
    @GetMapping("menus")
    public List<Menu> queryAllMenus(){
        return menuService.queryAllMenus();
    }

    @ApiOperation("根据角色id查询菜单id")
    @GetMapping("mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid))
                .stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation("更新角色菜单")
    @PutMapping("updateMenuRole")
    public Result updateMenuRole(Integer rid,Integer[] mid){
        return menuRoleService.updateMenuRole(rid,mid);
    }
}
