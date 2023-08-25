package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_history_process
 */
@TableName(value ="sys_history_process")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryProcess implements Serializable {

    @TableId(type = IdType.AUTO)
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
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户账户
     */
    private String userName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", startEmulationTime=").append(startEmulationTime);
        sb.append(", endEmulationTime=").append(endEmulationTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", userName=").append(userName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}