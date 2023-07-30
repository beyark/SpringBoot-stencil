package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.domain.ActivityActivity;

/**
 * @program: SpringBoot-stencil
 * @description: 仿真执行、结果模块
 * @create: 2023-07-23 15:16
 **/
public interface EmulationOutcomeService{

    //生成XML文件
    void generateXML(Integer historyProcessId,String startEmulationTime);
}
