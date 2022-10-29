package com.cdh.server.service;

import com.cdh.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdh.server.pojo.rest.PageBean;
import com.cdh.server.pojo.rest.Result;

import java.time.LocalDate;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页查询所有员工
     * @param currentPage
     * @param size
     * @param employee
     * @param dateScope
     * @return
     */
    PageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] dateScope);

    /**
     * 查询最大工号
     * @return
     */
    Result getMaxId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    Result addEmp(Employee employee);

    /**
     * 获取员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 查询所有员工账套
     * @param currentPage
     * @param size
     * @return
     */
    PageBean listEmployeeWithSal(Integer currentPage, Integer size);
}
