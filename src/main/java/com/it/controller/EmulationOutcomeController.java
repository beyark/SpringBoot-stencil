package com.it.controller;

import com.it.dto.MyResponseDto;
import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
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
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "endEmulationTime",value = "结束仿真时间",readOnly = true,paramType = "path")
    })
    @GetMapping("/generateXML")
    public AjaxResponse generateXML(Integer historyProcessId,String startEmulationTime,String endEmulationTime){
        String url = emulationOutcomeService.generateXML(historyProcessId, startEmulationTime,endEmulationTime);
        return url != null ? AjaxResponse.success(url) : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR));
    }

    @ApiOperation("加载XML")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url",value = "xml地址",readOnly = true,paramType = "path"),
    })
    @GetMapping("/efficacyXML")
    public AjaxResponse efficacyXML(String url){
        String flag = emulationOutcomeService.efficacyXML(url);
        return AjaxResponse.success(flag);
    }

    @ApiOperation("获取引擎执行中状态信息")
    @GetMapping("/fetchEngineState")
    public AjaxResponse fetchEngineState(){
        MyResponseDto myResponseDto = emulationOutcomeService.fetchEngineState();
        return AjaxResponse.success(myResponseDto);
    }

    @ApiOperation("第一次开启仿真")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "endEmulationTime",value = "结束仿真时间",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path")
    })
    @GetMapping("/startSimulation")
    public AjaxResponse startSimulation(Integer historyProcessId,String startEmulationTime,String endEmulationTime,String userName){
        boolean flag = emulationOutcomeService.startSimulation(historyProcessId,startEmulationTime,endEmulationTime,userName);
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR));
    }

    @ApiOperation("持续仿真")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "velocityValue",value = "速度值",readOnly = true,paramType = "path")
    })
    @GetMapping("/sustainEmulation")
    public AjaxResponse sustainEmulation(Integer velocityValue){
        MyResponseDto myResponseDto = emulationOutcomeService.sustainEmulation(velocityValue);
        return AjaxResponse.success(myResponseDto);
    }

    @ApiOperation("中止仿真")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path")
    })
    @GetMapping("/discontinueEmulation")
    public AjaxResponse discontinueEmulation(String userName,String historyProcessId){
        boolean flag = emulationOutcomeService.discontinueEmulation(userName, historyProcessId);
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.OTHER_ERROR),"仿真终止失败");
    }

    @ApiOperation("重启引擎")
    @GetMapping("/rebootEmulationEngine")
    public AjaxResponse rebootEmulationEngine(){
        Boolean flag = emulationOutcomeService.rebootEmulationEngine();
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"重启引擎失败"));
    }

    @ApiOperation("获取引擎状态")
    @GetMapping("/getEngineState")
    public AjaxResponse getEngineState(){
        String flag = emulationOutcomeService.getEngineState();
        return AjaxResponse.success(flag);
    }

}
