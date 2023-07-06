package com.it.controller;

import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
import com.it.service.UserService;
import com.it.vo.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SpringBoot-stencil
 * @description:
 * @author: 胡浩
 * @create: 2023-06-26 12:31
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation("根据用户ID查询用户信息123")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户编号",readOnly = true,paramType = "path")
    })
    @GetMapping("/getUser/{userId}")
    public AjaxResponse getUser(@PathVariable("userId") int userId){
        return AjaxResponse.success(userService.getUser(userId));
    }

    @ApiOperation("测试逻辑删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",readOnly = true,paramType = "path")
    })
    @DeleteMapping("/deleteUserById/{userId}")
    public AjaxResponse deleteUserById(@PathVariable("userId") int userId){
        boolean bool = userService.deleteUserById(userId);
        return bool == true ? AjaxResponse.success("删除成功！") :
                AjaxResponse.error(new CustomError(CustomErrorType.DATABASE_OP_ERROR));
    }
}
