package com.cdh.server.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdh.server.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 分页查询所有员工
     * @param page
     * @param employee
     * @param dateScope
     * @return
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee,
                                      @Param("dateScope") LocalDate[] dateScope);

    /**
     * 获取员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 查询所有员工账套
     * @param page
     * @return
     */
    IPage<Employee> listEmployeeWithSal(@Param("page") Page<Employee> page);
}
