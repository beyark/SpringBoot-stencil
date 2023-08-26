package com.it.controller;

import com.it.domain.User;
import com.it.dto.UserQueueMessageDto;
import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
import com.it.service.UserService;
import com.it.vo.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Resource
    private UserService userService;

    private volatile boolean isExecuting = false;
    private volatile String lastResult;

    @ApiOperation("第一次开启仿真/新增用户到队列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "historyProcessId",value = "历史记录编号",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "startEmulationTime",value = "开始仿真时间",readOnly = true,paramType = "path"),
            @ApiImplicitParam(name = "endEmulationTime",value = "结束仿真时间",readOnly = true,paramType = "path")
    })
    @GetMapping("/add")
    public AjaxResponse addUser(@RequestParam("userName") String userName,
                                @RequestParam("historyProcessId") Integer historyProcessId,
                                @RequestParam("startEmulationTime") String startEmulationTime,
                                @RequestParam("endEmulationTime") String endEmulationTime) {
        // URL 解码
        String startTime = null;
        String endTime = null;
        try {
            startTime = URLDecoder.decode(startEmulationTime, "UTF-8");
            endTime = URLDecoder.decode(endEmulationTime, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        User user = userService.getUserByUserName(userName);
        if (user != null){
            userService.removeById(user.getId());
        }
        boolean flag = userService.addUser(userName,historyProcessId,startTime,endTime);
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.DATABASE_OP_ERROR),"新增用户失败");
    }

    @ApiOperation("移除用户出队列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "UserName",value = "用户名称",readOnly = true,paramType = "path"),
    })
    @GetMapping("/remove")
    public AjaxResponse removeUser(@RequestParam("userName") String userName) {
        Boolean flag = userService.removeUser(userName);
        return flag ? AjaxResponse.success() : AjaxResponse.error(new CustomError(CustomErrorType.DATABASE_OP_ERROR),"移除用户失败");
    }

    @ApiOperation("执行队列")
    @GetMapping("/execute")
    public synchronized String executeUser() {
        if (isExecuting) {
            return "任务已在执行...";
        }
        isExecuting = true;
        lastResult = userService.executeUser();
        isExecuting = false;
        return lastResult;
    }

    @ApiOperation("查询用户队列排名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path"),
    })
    @GetMapping("/getUserQueue")
    public AjaxResponse getUserQueue(@RequestParam("userName") String userName) {
        UserQueueMessageDto userQueue = userService.getUserQueue(userName);
        return userQueue != null ? AjaxResponse.success(userQueue) : AjaxResponse.error(new CustomError(CustomErrorType.OTHER_ERROR),"该用户没有在队列中");
    }
    @ApiOperation("判断该用户是否仿真结束")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path"),
    })
    @GetMapping("/userFZ")
    public AjaxResponse userFZ(@RequestParam("userName") String userName) {
        User user = userService.getUserByUserName(userName);
        return user != null ? AjaxResponse.success(user) : AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR));
    }
}
