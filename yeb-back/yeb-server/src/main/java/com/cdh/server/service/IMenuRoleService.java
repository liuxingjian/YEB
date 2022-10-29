package com.cdh.server.service;

import com.cdh.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdh.server.pojo.rest.Result;

/**
 * @author cdh
 * @since 2021-04-16
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mid
     * @return
     */
    Result updateMenuRole(Integer rid, Integer[] mid);
}
