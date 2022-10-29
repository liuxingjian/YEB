package com.cdh.server.exception;

import com.cdh.server.pojo.rest.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author by cdh
 * @description:全局异常处理
 * @Date: Created in 16:31 on 2021/4/21
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public Result mysqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return Result.error("该数据有关联数据，操作失败!");
        }

        return Result.error("数据库异常，操作失败!");
    }
}
