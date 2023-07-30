package com.it.controller;

import com.it.service.EmulationOutcomeService;
import com.it.vo.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: SpringBoot-stencil
 * @description: 仿真执行、结果模块
 * @create: 2023-07-23 15:16
 **/
@RestController
@RequestMapping("/emulationOutcome")
@Api(tags = "仿真执行、结果模块")
public class EmulationOutcomeController {
    @Resource
    private EmulationOutcomeService emulationOutcomeService;


    @ApiOperation("生成XML文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path")
    })
    @GetMapping("/generateXML")
    public AjaxResponse generateXML(Integer historyProcessId,String startEmulationTime){
        emulationOutcomeService.generateXML(historyProcessId,startEmulationTime);
        return AjaxResponse.success();
    }





}
