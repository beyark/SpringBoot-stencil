package com.it.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayVo implements Serializable {

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
     * 参数效验
     */
    private Integer parameterValidation;

    /**
     * 网关的边
     */
    private List<Data> dataList;

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
        sb.append(", dataList=").append(dataList);
        sb.append("]");
        return sb.toString();
    }

    //网关对应的边
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        //边的id
        public String id;
        //边的概率值
        public String probabilityValue;
    }
}