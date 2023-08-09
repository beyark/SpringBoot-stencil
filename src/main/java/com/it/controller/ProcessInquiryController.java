package com.it.controller;

import com.it.dto.ResultDto2;
import com.it.dto.SelectProcessListDto;
import com.it.service.ProcessInquiryService;
import com.it.vo.AjaxResponse;
import com.it.vo.ProcessInquiryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/processInquiry")
@Api(tags = "流程查询模块")
public class ProcessInquiryController {
    @Resource
    private ProcessInquiryService processInquiryService;

    @ApiOperation("查询流程列表")
    @GetMapping("/selectProcessList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization",value = "token",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "environment",value = "环境",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "page",value = "页码",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "pageSize",value = "条数",readOnly = true,paramType = "path")
    })
    public AjaxResponse selectProcessList(ProcessInquiryVo processInquiryVo){
        SelectProcessListDto selectProcessListDto = processInquiryService.selectProcessList(processInquiryVo);
        return AjaxResponse.success(selectProcessListDto);
    }

    @ApiOperation("查询流程详细信息")
    @GetMapping("/selectProcessDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization",value = "token",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "environment",value = "环境",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "licheng1_id",value = "传入的id为查询流程列表接口返回结果中的licheng1_id",readOnly = true,paramType = "path"),
    })
    public AjaxResponse selectProcessDetails(ProcessInquiryVo processInquiryVo){
        ResultDto2 resultDto = processInquiryService.selectProcessDetails(processInquiryVo);
        return AjaxResponse.success(resultDto);
    }
}
