package com.it.dto;

import com.it.domain.Activity;
import com.it.domain.Gateway;
import com.it.domain.HistoryProcess;
import com.it.vo.GatewayVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: SpringBoot-stencil
 * @description: 查询历史流程返回的DTO
 * @create: 2023-07-22 14:02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectHistoryProcessDto {

    private HistoryProcess historyProcess;

    private List<Activity> activityList;

    private List<GatewayVo> gatewayList;

}
