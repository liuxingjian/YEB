package com.cdh.server.config.handler;

import com.cdh.server.pojo.rest.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author by cdh
 * @description:当未登陆或者token失效时访问接口自定义返回的结果
 * @Date: Created in 19:08 on 2021/4/18
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Result error = Result.error("您还没有登录，请先登录！");
        error.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }
}
