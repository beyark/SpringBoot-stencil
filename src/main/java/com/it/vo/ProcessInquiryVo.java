package com.it.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @program: SpringBoot-stencil
 * @description:
 * @author: 胡浩
 * @create: 2023-07-19 09:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInquiryVo {
    private String authorization;
    private String environment;
    private String licheng1_id;
    private Integer page;
    private Integer pageSize;
}
