package com.cdh.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_sys_msg")
@ApiModel(value="SysMsg对象", description="")
public class SysMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "消息id")
    private Integer mid;

    @ApiModelProperty(value = "0表示群发消息")
    private Integer type;

    @ApiModelProperty(value = "这条消息是给谁的")
    private Integer adminid;

    @ApiModelProperty(value = "0 未读 1 已读")
    private Integer state;


}
