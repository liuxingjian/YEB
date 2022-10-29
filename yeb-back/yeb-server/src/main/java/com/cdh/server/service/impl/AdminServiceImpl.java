package com.cdh.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdh.server.config.security.JwtTokenUtil;
import com.cdh.server.mapper.AdminMapper;
import com.cdh.server.mapper.AdminRoleMapper;
import com.cdh.server.mapper.RoleMapper;
import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.AdminRole;
import com.cdh.server.pojo.Role;
import com.cdh.server.pojo.rest.Result;
import com.cdh.server.pojo.vo.AdminLoginVo;
import com.cdh.server.service.IAdminService;
import com.cdh.server.utils.AdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    /**
     * 登陆之后返回token
     * @param adminLoginVo
     * @param request
     * @return
     */
    @Override
    public Result login(AdminLoginVo adminLoginVo, HttpServletRequest request) {
        String username = adminLoginVo.getUsername();
        String password = adminLoginVo.getPassword();
        String code = adminLoginVo.getCode();
        String captcha = (String) request.getSession().getAttribute("captcha");

        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)){
            return Result.error("验证码输入错误，请重新输入！");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails ||
                !passwordEncoder.matches(password,userDetails.getPassword())){
            return Result.error("用户名或密码不正确");
        }
        if (!userDetails.isEnabled()){
            return Result.error("账号被禁用，请联系管理员");
        }

        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>(16);
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return Result.success("登录成功", tokenMap);
    }

    /**
     * 获取当前登录用户的信息
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return baseMapper.selectOne(wrapper);

    }

    @Autowired
    private RoleMapper roleMapper;
    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有管理员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmin(String keywords) {
        return baseMapper.getAllAdmin(AdminUtil.getCurrentAdmin().getId(),keywords);
    }

    @Autowired
    private AdminRoleMapper adminRoleMapper;
    /**
     * 更新管理员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));

        Integer result = adminRoleMapper.updateAdminRole(adminId, rids);
        if (Objects.equals(result, rids.length)){
            return Result.success("更新成功!");
        }
        return Result.error("更新失败!");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public Result updateAdminPassWord(String oldPass, String pass, Integer adminId) {
        Admin admin = baseMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int update = baseMapper.updateById(admin);
            if (1 == update){
                return Result.success("更新成功");
            }
        }
        return Result.error("更新失败!");
    }

    /**
     * 更新用户头像
     * @param url
     * @param adminId
     * @param authentication
     * @return
     */
    @Override
    public Result updateAdminUserFace(String url, Integer adminId, Authentication authentication) {
        Admin admin = baseMapper.selectById(adminId);
        admin.setUserFace(url);
        int update = baseMapper.updateById(admin);
        if (1 == update){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return Result.success("更新成功",url);
        }
        return Result.error("更新失败");
    }
}
