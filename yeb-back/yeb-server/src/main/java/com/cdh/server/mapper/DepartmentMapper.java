package com.cdh.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdh.server.pojo.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-24
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查询所有部门
     * @param parentId
     * @return
     */
    List<Department> getAllDepartments(Integer parentId);

    /**
     * 添加部门
     * @param dept
     * @return
     */
    void addDept(Department dept);

    /**
     * 删除部门
     * @param dept
     */
    void deleteDept(Department dept);
}
