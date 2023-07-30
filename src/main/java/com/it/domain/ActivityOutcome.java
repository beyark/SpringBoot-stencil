package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 
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
    private Date disposeTimeAvg;

    /**
     * 等待时间最大值
     */
    private Date awaitTimeMax;

    /**
     * 等待时间最小值
     */
    private Date awaitTimeMin;

    /**
     * 等待时间平均值
     */
    private Date awaitTimeAvg;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}