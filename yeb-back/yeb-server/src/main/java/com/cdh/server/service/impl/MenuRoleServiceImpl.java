package com.cdh.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdh.server.mapper.MenuRoleMapper;
import com.cdh.server.pojo.MenuRole;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IMenuRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {


    /**
     * 更新角色菜单
     * @param rid
     * @param mid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateMenuRole(Integer rid, Integer[] mid) {
        // 先清空该角色下的所有菜单
        baseMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));

        Integer count = baseMapper.insetBatch(rid, mid);
        if (count > 0){
            return Result.success("更新成功");
        }
        return Result.success("更新失败");
    }
}
