package com.cdh.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdh.server.mapper.MenuMapper;
import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.Menu;
import com.cdh.server.service.IMenuService;
import com.cdh.server.utils.AdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 通过用户ID查询菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        Admin admin = AdminUtil.getCurrentAdmin();
        Integer adminId = admin.getId();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 从redis获取菜单数据
        List<Menu> menus = ((List<Menu>) ops.get("menu_" + adminId));
        if (CollectionUtils.isEmpty(menus)){
            // 如果为空，去数据库获取菜单数据
            menus = baseMapper.getMenusByAdminId(adminId);
            ops.set("menu_"+ adminId, menus);
        }
        return menus;
    }

    /**
     * 根据角色获取菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusByRole() {
        return baseMapper.getMenusByRole();
    }

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> queryAllMenus() {
        return baseMapper.queryAllMenus();
    }
}
