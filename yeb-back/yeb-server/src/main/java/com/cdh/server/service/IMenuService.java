package com.cdh.server.service;

import com.cdh.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户ID查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusByRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> queryAllMenus();
}
