package com.cdh.server.service.impl;

import com.cdh.server.pojo.Nation;
import com.cdh.server.mapper.NationMapper;
import com.cdh.server.service.INationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
