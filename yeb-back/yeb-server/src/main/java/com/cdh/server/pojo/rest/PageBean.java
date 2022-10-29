package com.cdh.server.pojo.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author by cdh
 * @Date: Created in 16:53 on 2021/4/25
 */
@Data
@ApiModel("分页公共返回对象")
@AllArgsConstructor
@NoArgsConstructor
public class PageBean implements Serializable {
    @ApiModelProperty(value = "总条数",required = true)
    private Long total;
    @ApiModelProperty(value = "分页数据集合",required = true)
    private List<?> data;
}
