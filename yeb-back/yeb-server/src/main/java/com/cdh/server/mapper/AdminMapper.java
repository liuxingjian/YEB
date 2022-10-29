package com.cdh.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdh.server.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有管理员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmin(@Param("id") Integer id, @Param("keywords") String keywords);
}
