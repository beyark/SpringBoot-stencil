package com.it.service;

import com.it.dto.MyResponseDto;

/**
 * @program: SpringBoot-stencil
 * @description: 仿真执行、结果模块
 * @create: 2023-07-23 15:16
 **/
public interface EmulationOutcomeService{

    //生成XML文件
    String generateXML(Integer historyProcessId,String startEmulationTime);

    //加载XML文件
    boolean efficacyXML(String url);

    //获取引擎执行中返回的状态信息
    MyResponseDto fetchEngineState();

    //第一次开启仿真
    boolean startSimulation(Integer historyProcessId,String startEmulationTime,String endEmulationTime,String userName);

    //持续仿真流程
    MyResponseDto sustainEmulation(Integer velocityValue);

    //中止仿真
    boolean discontinueEmulation(String userName,String historyProcessId);

    //重启仿真引擎
    Boolean rebootEmulationEngine();

    //获取引擎状态
    String getEngineState();

}
