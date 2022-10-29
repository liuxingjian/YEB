package com.cdh.server.service.impl;

import com.cdh.server.pojo.Department;
import com.cdh.server.mapper.DepartmentMapper;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cdh
 * @since 2021-04-24
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartments() {
        return baseMapper.getAllDepartments(-1);
    }

    /**
     * 添加部门
     * @param dept
     * @return
     */
    @Override
    public Result addDept(Department dept) {
        dept.setEnabled(true);
        baseMapper.addDept(dept);
        if (1 == dept.getResult()){
            return Result.success("添加成功!",dept);
        }
        return Result.error("添加失败!");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public Result deleteDept(Integer id) {
        Department dept = new Department();
        dept.setId(id);
        baseMapper.deleteDept(dept);
        if (-2 == dept.getResult()){
            return Result.error("该部门下还有子部门，删除失败!");
        }
        if (-1 == dept.getResult()){
            return Result.error("该部门下还有员工，删除失败!");
        }
        if (1 == dept.getResult()){
            return Result.success("删除成功!");
        }
        return Result.error("删除失败!");
    }
}
