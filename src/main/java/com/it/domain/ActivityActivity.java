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
 * @TableName sys_activity_activity
 */
@TableName(value ="sys_activity_activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityActivity implements Serializable {
    /**
     * 活动与活动关系表编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动1编号
     */
    private String activityOne;

    /**
     * 活动2编号
     */
    private String activityTwo;

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
        sb.append(", activityOne=").append(activityOne);
        sb.append(", activityTwo=").append(activityTwo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", historyProcessId=").append(historyProcessId);
        sb.append("]");
        return sb.toString();
    }
}