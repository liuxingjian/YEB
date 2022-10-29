package com.cdh.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdh.server.mapper.EmployeeMapper;
import com.cdh.server.mapper.MailLogMapper;
import com.cdh.server.pojo.Employee;
import com.cdh.server.pojo.MailConstant;
import com.cdh.server.pojo.MailLog;
import com.cdh.server.pojo.rest.PageBean;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.service.IEmployeeService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MailLogMapper mailLogMapper;

    /**
     * 分页查询所有员工
     * @param currentPage
     * @param size
     * @param employee
     * @param dateScope
     * @return
     */
    @Override
    public PageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] dateScope) {
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> data = baseMapper.getEmployeeByPage(page,employee,dateScope);
        PageBean pageBean = new PageBean();
        pageBean.setTotal(data.getTotal());
        pageBean.setData(data.getRecords());
        return pageBean;
    }

    /**
     * 查询最大工号
     * @return
     */
    @Override
    public Result getMaxId() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MAX(workId)");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        String maxWorkId = String.format("%08d", Integer.parseInt(mapList.get(0).get("MAX(workId)").toString()) + 1);
        return Result.success(null,maxWorkId);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    public Result addEmp(Employee employee) {
        // 计算合同期限，保留两位小数
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long day = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(day / 365.00)));
        if (1 == baseMapper.insert(employee)){
            MailLog mailLog = new MailLog();
            Employee emp = baseMapper.getEmployee(employee.getId()).get(0);
            //数据库记录发送的消息
            String msgId = UUID.randomUUID().toString();
            mailLog.setMsgId(msgId);
            mailLog.setEid(emp.getId());
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailConstant.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstant.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstant.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLogMapper.insert(mailLog);

            rabbitTemplate.convertAndSend(
                    MailConstant.MAIL_EXCHANGE_NAME,
                    MailConstant.MAIL_ROUTING_KEY_NAME,
                    emp,
                    new CorrelationData(msgId));
            return Result.success("员工添加成功!");
        }
        return Result.error("员工添加失败!");
    }

    /**
     * 获取员工
     * @param id
     * @return
     */
    @Override
    public List<Employee> getEmployee(Integer id) {
        return baseMapper.getEmployee(id);
    }

    /**
     * 查询所有员工账套
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public PageBean listEmployeeWithSal(Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> data = baseMapper.listEmployeeWithSal(page);
        PageBean pageBean = new PageBean();
        pageBean.setTotal(data.getTotal());
        pageBean.setData(data.getRecords());
        return pageBean;
    }
}
