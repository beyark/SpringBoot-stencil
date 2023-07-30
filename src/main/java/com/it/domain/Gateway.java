package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_gateway
 */
@TableName(value ="sys_gateway")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gateway implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer gatewayId;

    /**
     * 网关编号（与前端网关编号对应）
     */
    private String id;

    /**
     * 网关名称
     */
    private String name;

    /**
     * 最新修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 历史流程编号
     */
    private Integer historyProcessId;

    /**
     * 参数效验(0：为设置参数，1：已设置参数)
     */
    private Integer parameterValidation;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append(", parameterValidation=").append(parameterValidation);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}