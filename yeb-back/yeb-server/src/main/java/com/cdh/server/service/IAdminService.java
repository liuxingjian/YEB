package com.cdh.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.Role;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.pojo.vo.AdminLoginVo;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author cdh
 * @since 2021-04-16
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登陆之后返回token
     * @param adminLoginVo
     * @param request
     * @return
     */
    Result login(AdminLoginVo adminLoginVo, HttpServletRequest request);

    /**
     * 获取当前登录用户的信息
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有管理员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmin(String keywords);

    /**
     * 更新管理员角色
     * @param adminId
     * @param rids
     * @return
     */
    Result updateAdminRole(Integer adminId, Integer[] rids);

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    Result updateAdminPassWord(String oldPass, String pass, Integer adminId);

    /**
     * 更新用户头像
     * @param url
     * @param adminId
     * @param authentication
     * @return
     */
    Result updateAdminUserFace(String url, Integer adminId, Authentication authentication);
}
