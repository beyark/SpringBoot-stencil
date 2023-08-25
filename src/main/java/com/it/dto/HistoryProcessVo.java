package com.it.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: SpringBoot-stencil
 * @create: 2023-08-24 15:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryProcessVo {
    private int historyProcessId;

    /**
     * 流程编号(非自增，是打开流程当中的liucheng1_id，用于获取样式等信息)
     */
    private String id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始仿真时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startEmulationTime;

    /**
     * 结束仿真时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endEmulationTime;

    /**
     * 最新修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String  updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户账户
     */
    private String userName;
}
