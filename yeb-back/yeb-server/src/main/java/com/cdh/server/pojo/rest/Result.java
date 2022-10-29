package com.cdh.server.pojo.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by cdh
 * @description:公共返回对象
 * @Date: Created in 17:52 on 2021/4/18
 */
@Data
@ApiModel("统一返回结果")
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @ApiModelProperty(value = "结果状态码",required = true)
    private long code;
    @ApiModelProperty(value = "状态信息")
    private String message;
    @ApiModelProperty(value = "返回数据对象")
    private Object obj;

    public static Result success(String message){
        return new Result(200,message,null);
    }

    public static Result success(String message,Object obj){
        return new Result(200,message,obj);
    }

    public static Result error(String message){
        return new Result(500,message,null);
    }

    public static Result error(String message,Object obj){
        return new Result(500,message,obj);
    }

}
