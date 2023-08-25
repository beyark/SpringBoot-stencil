package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.domain.Activity;
import com.it.domain.Gateway;
import com.it.domain.XmlUrl;
import com.it.dto.ActivityDto;
import com.it.dto.GatewayDto;
import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
import com.it.service.*;
import com.it.vo.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @program: SpringBoot-stencil
 * @create: 2023-08-22 16:28
 **/
@RestController
@RequestMapping("/xml")
@Api(tags = "xml模块")
public class XmlUrlController {
    @Resource
    private XmlUrlService xmlUrlService;
    @Resource
    private EmulationOutcomeService emulationOutcomeService;
    @Resource
    private ActivityService activityService;
    @Resource
    private GatewayService gatewayService;

    @ApiOperation("获取该用户的xml地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名称",readOnly = true,paramType = "path"),
    })
    @GetMapping("/getXmlUrl")
    public AjaxResponse getXmlUrl(@RequestParam("userName") String userName) {
        XmlUrl xmlUrl = xmlUrlService.getBaseMapper().selectOne(new LambdaQueryWrapper<XmlUrl>().eq(XmlUrl::getUserName, userName));
        String value = emulationOutcomeService.efficacyXML(xmlUrl.getXmlUrl());
        //正则将错误提示当中数字去掉第一位
        String output = value.replaceAll("\\b\\d", "");
        //正则截取出数字（就是节点的数据库id了）
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(output);
        Integer id = 0;
        if (matcher.find()) {
            id = Integer.valueOf(matcher.group());
        }
        Activity activity = activityService.getBaseMapper().selectOne(new LambdaQueryWrapper<Activity>().eq(Activity::getActivityId, id));
        if (activity != null){
            ActivityDto activityDto = new ActivityDto();
            BeanUtils.copyProperties(activity,activityDto);
            activityDto.setMessage(output);
            return AjaxResponse.success(activityDto);
        }else {
            Gateway gateway = gatewayService.getBaseMapper().selectOne(new LambdaQueryWrapper<Gateway>().eq(Gateway::getGatewayId, id));
            GatewayDto gatewayDto = new GatewayDto();
            BeanUtils.copyProperties(gateway,gatewayDto);
            gatewayDto.setMessage(output);
            return AjaxResponse.success(gatewayDto);
        }
    }
}
