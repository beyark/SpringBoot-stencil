package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: SpringBoot-stencil
 * @create: 2023-08-24 12:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {
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

    /**
     *  信息
     */
    private String message;
}
