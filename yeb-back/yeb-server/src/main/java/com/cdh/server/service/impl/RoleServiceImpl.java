package com.cdh.server.service.impl;

import com.cdh.server.pojo.Role;
import com.cdh.server.mapper.RoleMapper;
import com.cdh.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
