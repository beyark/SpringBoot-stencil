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
 * @TableName sys_gateway_side
 */
@TableName(value ="sys_gateway_side")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewaySide implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 网关边编号
     */
    private String sideId;

    /**
     * 概率值
     */
    private String probabilityValue;

    /**
     * 网关编号
     */
    private String gatewayId;

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
        sb.append(", sideId=").append(sideId);
        sb.append(", probabilityValue=").append(probabilityValue);
        sb.append(", gatewayId=").append(gatewayId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append("]");
        return sb.toString();
    }
}