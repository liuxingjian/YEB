package com.cdh.server.mapper;

import com.cdh.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Mapper
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 批量更新角色菜单
     * @param rid
     * @param mid
     * @return
     */
    Integer insetBatch(@Param("rid") Integer rid,@Param("mids") Integer[] mid);
}
