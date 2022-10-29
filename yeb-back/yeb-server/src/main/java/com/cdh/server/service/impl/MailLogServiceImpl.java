package com.cdh.server.service.impl;

import com.cdh.server.pojo.MailLog;
import com.cdh.server.mapper.MailLogMapper;
import com.cdh.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
