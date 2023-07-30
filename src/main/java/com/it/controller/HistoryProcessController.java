package com.it.controller;


import com.it.domain.HistoryProcess;
import com.it.dto.SelectHistoryProcessDto;
import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
import com.it.service.HistoryProcessService;
import com.it.util.Constant;
import com.it.util.PageUtils;
import com.it.vo.AjaxResponse;
import com.it.vo.SaveHistoricalProcessesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SpringBoot-stencil
 * @description: 保存历史流程
 * @create: 2023-07-20 15:52
 **/
@RestController
@RequestMapping("/historicalProcesses")
@Api(tags = "历史流程操作模块")
public class HistoryProcessController {
    @Resource
    private HistoryProcessService historyProcessService;

    /**
     * @description: 流程的效验
     * @date: 2023/7/20 16:19
     * @param: id 流程编号
     * @return: AjaxResponse
     **/
    @ApiOperation("流程效验接口")
    @GetMapping("/processValidation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史流程编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "nodeId",value = "节点编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "type",value = "节点状态(activity || gateway)",readOnly = true,paramType = "path")
    })
    public AjaxResponse processValidation(String historyProcessId,String nodeId,String type){
        String message = historyProcessService.processValidation(historyProcessId,nodeId,type);
        return AjaxResponse.success(message);
    }

    /**
     * @description: 判断历史流程表中是否已经有了该条流程
     * @date: 2023/7/21 14:20
     * @param:
     * @return: com.it.vo.AjaxResponse
     **/
    @ApiOperation("判断历史流程是否保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "历史流程编号",readOnly = true,paramType = "path")
    })
    @GetMapping("/selectHistoricalProcessesById")
    public AjaxResponse selectHistoricalProcessesById(String id){
        HistoryProcess historyProcess = historyProcessService.getBaseMapper().selectById(id);
        return historyProcess == null ? AjaxResponse.success("该流程没有被保存") :
                AjaxResponse.error(new CustomError(CustomErrorType.DATABASE_OP_ERROR,"该流程已经被保存"));
    }

    /**
     * @description: 保存&另存为历史流程接口
     * @date: 2023/7/20 16:17
     * @param: activity , gateway
     * @return: com.it.vo.AjaxResponse
     **/
    @ApiOperation("保存&另存为历史流程接口")
    @PostMapping("/saveHistoricalProcesses")
    public AjaxResponse saveHistoricalProcesses(@RequestBody SaveHistoricalProcessesVO saveHistoricalProcessesVO){
        String flag = historyProcessService.saveHistoricalProcesses(saveHistoricalProcessesVO);
        return "success".equals(flag) ? AjaxResponse.success() :
                AjaxResponse.error(new CustomError(CustomErrorType.OPERATE_ERROR),flag);
    }

    /**
     * @description: 查询历史流程列表(分页)
     * @author: 胡浩
     * @date: 2023/7/21 21:30
     * @param: String page, String pageSize
     * @return: AjaxResponse
     **/
    @ApiOperation("查询历史流程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "pageSize",value = "页数",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "userName",value = "用户账户",readOnly = true,paramType = "path")
    })
    @GetMapping("/selectHistoryProcessList")
    public AjaxResponse selectHistoryProcessList(String page, String pageSize,String userName){
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.PAGE_SIZE,pageSize );
        map.put(Constant.PAGE, page);
        map.put("userName",userName);
        PageUtils pageUtils = historyProcessService.selectHistoryProcessList(map);
        return pageUtils.getList().size() > 0 ? AjaxResponse.success(pageUtils) :
                AjaxResponse.error(new CustomError(CustomErrorType.DATABASE_OP_ERROR,"查询结果为空"));
    }

    /**
     * @description: 查询历史流程
     * @author: 胡浩
     * @date: 2023/7/21 21:59
     * @param: id:历史流程ID
     * @return: AjaxResponse
     **/
    @ApiOperation("查询历史流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path")
    })
    @GetMapping("/selectHistoryProcess")
    public AjaxResponse selectHistoryProcess(Integer historyProcessId){
         SelectHistoryProcessDto selectHistoryProcessDto = historyProcessService.selectHistoryProcess(historyProcessId);
        return AjaxResponse.success(selectHistoryProcessDto);
    }

    
}
