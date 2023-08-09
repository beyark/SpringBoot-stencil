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


    @ApiOperation("生成XML文件(测试)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path")
    })
    @GetMapping("/generateXML")
    public AjaxResponse generateXML(Integer historyProcessId,String startEmulationTime){
        String url = emulationOutcomeService.generateXML(historyProcessId, startEmulationTime);
        return url != null ? AjaxResponse.success(url) : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR));
    }

    @ApiOperation("加载XML(测试)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url",value = "xml地址",readOnly = true,paramType = "path"),
    })
    @GetMapping("/efficacyXML")
    public AjaxResponse efficacyXML(String url){
        boolean flag = emulationOutcomeService.efficacyXML(url);
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR));
    }

    @ApiOperation("获取引擎执行中状态信息(测试)")
    @GetMapping("/fetchEngineState")
    public AjaxResponse fetchEngineState(){
        MyResponseDto myResponseDto = emulationOutcomeService.fetchEngineState();
        return AjaxResponse.success(myResponseDto);
    }

    @ApiOperation("第一次开启仿真(测试)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "endEmulationTime",value = "结束仿真时间",readOnly = true,paramType = "path")
    })
    @GetMapping("/startSimulation")
    public AjaxResponse startSimulation(Integer historyProcessId,String startEmulationTime,String endEmulationTime){
        boolean flag = emulationOutcomeService.startSimulation(historyProcessId,startEmulationTime,endEmulationTime);
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
    @GetMapping("/discontinueEmulation")
    public AjaxResponse discontinueEmulation(){
        emulationOutcomeService.discontinueEmulation();
        return AjaxResponse.success();
    }

    @ApiOperation("重启引擎(测试)")
    @GetMapping("/rebootEmulationEngine")
    public AjaxResponse rebootEmulationEngine(){
        Boolean flag = emulationOutcomeService.rebootEmulationEngine();
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"重启引擎失败"));
    }

    @ApiOperation("获取引擎状态(测试)")
    @GetMapping("/getEngineState")
    public AjaxResponse getEngineState(){
        String flag = emulationOutcomeService.getEngineState();
        return AjaxResponse.success(flag);
    }

}
