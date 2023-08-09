package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SpringBoot-stencil
 * @description: 查询用户队列信息DTO
 * @create: 2023-08-07 12:17
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueueMessageDto {
    private String sum;
    private Integer ranking;
}
