package com.cdh.server.mapper;

import com.cdh.server.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新管理员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer updateAdminRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
