package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: SpringBoot-stencil
 * @create: 2023-08-24 12:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayDto implements Serializable {
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
     * 参数效验(0：为设置参数，1：已设置参数)
     */
    private Integer parameterValidation;

    /**
     *  信息
     */
    private String message;
}
