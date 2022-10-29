package com.cdh.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cdh.server.pojo.Employee;
import com.cdh.server.pojo.MailConstant;
import com.cdh.server.pojo.MailLog;
import com.cdh.server.service.IEmployeeService;
import com.cdh.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //@Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){

        QueryWrapper<MailLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0)
                .lt("tryTime", LocalDateTime.now());
        List<MailLog> mailLogList = mailLogService.list(queryWrapper);

        mailLogList.forEach(mailLog -> {
            //如果重试次数超过3次，更新投递状态为投递失败，不再重试
            if (3 <= mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>()
                .set("status",2).eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstant.MSG_TIMEOUT))
                    .set("msgId",mailLog.getMsgId()));
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend(
                    MailConstant.MAIL_EXCHANGE_NAME,
                    MailConstant.MAIL_ROUTING_KEY_NAME,
                    employee,
                    new CorrelationData(mailLog.getMsgId()));
        });

    }

}
