package com.cdh.server.service.impl;

import com.cdh.server.pojo.Position;
import com.cdh.server.mapper.PositionMapper;
import com.cdh.server.service.IPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
