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
 * @TableName sys_emulation_time_outcome
 */
@TableName(value ="sys_emulation_time_outcome")
@Data
public class EmulationTimeOutcome implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 处理时间最大值
     */
    private Date disposeTimeMax;

    /**
     * 处理时间最小值
     */
    private Date disposeTimeMin;

    /**
     * 处理时间平均值
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
     * 完成时间最大值
     */
    private Date finishTimeMax;

    /**
     * 完成时间最小值
     */
    private Date finishTimeMin;

    /**
     * 完成时间平均值
     */
    private Date finishTimeAvg;

    /**
     * 仿真次数
     */
    private Integer count;

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