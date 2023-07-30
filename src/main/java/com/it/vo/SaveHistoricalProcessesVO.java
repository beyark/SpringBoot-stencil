package com.it.vo;

import com.it.domain.Activity;
import com.it.domain.Gateway;
import com.it.domain.HistoryProcess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: SpringBoot-stencil
 * @description:
 * @author: 胡浩
 * @create: 2023-07-20 17:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveHistoricalProcessesVO {
    private String authorization;
    private String environment;
    private String userName;

    //按钮类型（save:保存 saveAs：另存为）
    private String saveType;

    private HistoryProcess historyProcess;
    private List<Activity> activity;
    private List<GatewayVo> gateway;

}
