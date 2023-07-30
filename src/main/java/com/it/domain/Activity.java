package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_activity
 */
@TableName(value ="sys_activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer activityId;

    /**
     * 活动编号(与前端的活动编号需要对应)
     */
    private String id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 处理时间
     */
    private Integer disposeTime;

    /**
     * 处理时间单位(0：秒1:分2:时3:天)
     */
    private Integer disposeTimeType;

    /**
     * 等待时间
     */
    private Integer awaitTime;

    /**
     * 等待时间单位(0：秒1:分2:时3:天)
     */
    private Integer awaitTimeType;

    /**
     * 活动设置(默认:0开始活动:1结束活动:2)
     */
    private Integer type;

    /**
     * 时间发生间隔(0:按频率)
     */
    private Integer intervals;

    /**
     * 执行频率
     */
    private Integer executionFrequency;

    /**
     * 执行频率单位(0：秒1:分2:时3:天)
     */
    private Integer timeType;

    /**
     * 事件输入类型(0:固定值1:范围内随机)
     */
    private Integer eventType;

    /**
     * 具体值
     */
    private Integer distinctValue;

    /**
     * 修改时间
     */
    private Date updataTime;

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

    /**
     * 随机值1
     */
    private Integer random1;

    /**
     * 随机值2
     */
    private Integer random2;


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
        sb.append(", disposeTime=").append(disposeTime);
        sb.append(", disposeTimeType=").append(disposeTimeType);
        sb.append(", awaitTime=").append(awaitTime);
        sb.append(", awaitTimeType=").append(awaitTimeType);
        sb.append(", type=").append(type);
        sb.append(", interval=").append(intervals);
        sb.append(", executionFrequency=").append(executionFrequency);
        sb.append(", timeType=").append(timeType);
        sb.append(", eventType=").append(eventType);
        sb.append(", distinctValue=").append(distinctValue);
        sb.append(", updataTime=").append(updataTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append(", parameterValidation=").append(parameterValidation);
        sb.append(", random1=").append(random1);
        sb.append(", random2=").append(random2);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}