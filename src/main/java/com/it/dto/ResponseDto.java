package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SpringBoot-stencil
 * @description: 引擎执行状态value数据接收
 * @create: 2023-08-08 13:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String activityName;
    //3（平均处理时间）
    private String disposeTimeAvg;
    //4（平均等待时间）
    private String awaitTimeAvg;
    //5（当前用户数）
    private String userNum;
    //6（最大等待时间）
    private String awaitTimeMax;
    //7（最小等待时间）
    private String awaitTimeMin;
    //8（最大等待数量）
    private String awaitTimeMaxNum;
    //9（执行次数）
    private String executeNum;
}
