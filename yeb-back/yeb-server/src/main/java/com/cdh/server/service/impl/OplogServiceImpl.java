package com.cdh.server.service.impl;

import com.cdh.server.pojo.Oplog;
import com.cdh.server.mapper.OplogMapper;
import com.cdh.server.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
