package com.it.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel("用户实体类")
public class User implements Serializable {
    /**
     * 用户编号
     */
    @TableId(type = IdType.AUTO)
//    @ApiModelProperty("用户编号")
    private Long userId;

    /**
     * 用户名123
     */
    private String userName;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * 状态
     */
    private String stuate;

    /**
     * 电话
     */
    private Integer tel;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userPwd=").append(userPwd);
        sb.append(", stuate=").append(stuate);
        sb.append(", tel=").append(tel);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}