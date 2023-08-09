package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: SpringBoot-stencil
 * @description: 查询流程列表 的DTO
 * @create: 2023-07-22 10:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectProcessListDto {
    private Integer count;
    private List data;
}
