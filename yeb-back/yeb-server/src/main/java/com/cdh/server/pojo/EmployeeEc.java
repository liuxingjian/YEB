package com.cdh.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee_ec")
@ApiModel(value="EmployeeEc对象", description="")
public class EmployeeEc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工编号")
    private Integer eid;

    @ApiModelProperty(value = "奖罚日期")
    private LocalDate ecDate;

    @ApiModelProperty(value = "奖罚原因")
    private String ecReason;

    @ApiModelProperty(value = "奖罚分")
    private Integer ecPoint;

    @ApiModelProperty(value = "奖罚类别，0：奖，1：罚")
    private Integer ecType;

    @ApiModelProperty(value = "备注")
    private String remark;


}
