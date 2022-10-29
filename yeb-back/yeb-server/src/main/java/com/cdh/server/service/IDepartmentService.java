package com.cdh.server.service;

import com.cdh.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdh.server.pojo.rest.Result;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-24
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 查询所有部门
     * @return
     */
    List<Department> getAllDepartments();

    /**
     * 添加部门
     * @param dept
     * @return
     */
    Result addDept(Department dept);

    /**
     * 删除部门
     * @param id
     * @return
     */
    Result deleteDept(Integer id);
}
