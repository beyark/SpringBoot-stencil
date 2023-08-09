package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_activity_gateway
 */
@TableName(value ="sys_activity_gateway")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGateway implements Serializable {
    /**
     * 活动与网关关系表编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动编号
     */
    private String activityId;

    /**
     * 网关编号
     */
    private String gatewayId;

    /**
     * 是否有概率值（默认:0 ，有:1）(只有网关指向活动才有概率值）
     */
    private Integer value;

    /**
     * 概率值(只有网关指向活动才有概率值)
     */
    private String probabilityValue;

    /**
     * 边编号
     */
    private String sideId;

    /**
     * 历史流程编号
     */
    private Integer historyProcessId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", activityId=").append(activityId);
        sb.append(", gatewayId=").append(gatewayId);
        sb.append(", value=").append(value);
        sb.append(", probabilityValue=").append(probabilityValue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", sideId=").append(sideId);
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append("]");
        return sb.toString();
    }
}