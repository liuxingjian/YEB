package com.cdh.server.mapper;

import com.cdh.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过用户ID查询菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

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
