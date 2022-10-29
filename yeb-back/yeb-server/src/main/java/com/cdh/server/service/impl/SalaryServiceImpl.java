package com.cdh.server.service.impl;

import com.cdh.server.pojo.Salary;
import com.cdh.server.mapper.SalaryMapper;
import com.cdh.server.service.ISalaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
