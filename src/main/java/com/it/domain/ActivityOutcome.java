package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName sys_activity_outcome
 */
@TableName(value ="sys_activity_outcome")
@Data
public class ActivityOutcome implements Serializable {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 执行次数
     */
    private Integer executeNum;

    /**
     * 平均处理时间
     */
    private String disposeTimeAvg;

    /**
     * 等待时间最大值
     */
    private String awaitTimeMax;

    /**
     * 等待时间最小值
     */
    private String awaitTimeMin;

    /**
     * 等待时间平均值
     */
    private String awaitTimeAvg;

    /**
     * 最大等待数量
     */
    private Integer awaitTimeMaxNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 历史流程编号
     */
    private Integer historyProcessId;

    /**
     * 用户名称
     */
    private String userName;

    @TableField(exist = false)
    private String userNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}