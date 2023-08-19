package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.domain.ActivityOutcome;
import com.it.domain.EmulationTimeOutcome;
import com.it.domain.User;
import com.it.dto.MyResponseDto;
import com.it.dto.ResponseDto;
import com.it.dto.UserQueueMessageDto;
import com.it.service.ActivityOutcomeService;
import com.it.service.EmulationOutcomeService;
import com.it.service.EmulationTimeOutcomeService;
import com.it.service.UserService;
import com.it.mapper.UserMapper;
import com.it.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2023-08-06 20:45:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private EmulationOutcomeService emulationOutcomeService;
    @Resource
    private ActivityOutcomeService activityOutcomeService;
    @Resource
    private EmulationTimeOutcomeService emulationTimeOutcomeService;
    @Resource
    private UserService userService;

    //新增用户
    @Override
    public Boolean addUser(String userName, Integer historyProcessId, String startEmulationTime, String endEmulationTime) {
        User user = new User();
        user.setUserName(userName);
        user.setHistoryProcessId(historyProcessId);
        try {
            Date startTime = DateUtil.forDate(startEmulationTime, "yyyy-MM-dd HH:mm:ss");
            Date endTime = DateUtil.forDate(endEmulationTime, "yyyy-MM-dd HH:mm:ss");
            user.setStartEmulationTime(startTime);
            user.setEndEmulationTime(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int num = this.getBaseMapper().insert(user);
        return num == 1 ? true : false;
    }

    //删除用户
    @Override
    public Boolean removeUser(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", name);
        int num = this.getBaseMapper().delete(wrapper);
        return num == 1 ? true : false;
    }

    //执行任务
    @Override
    public String executeUser() {
        User user = this.getBaseMapper().getNextUser();
        if (user != null) {
            System.out.println(user.getUserName() + "用户执行...");
            //第一次开启仿真
            boolean flag = emulationOutcomeService.startSimulation(user.getHistoryProcessId(),
                    DateUtil.forString(user.getStartEmulationTime(), "yyyy-MM-dd HH:mm:ss"),
                    DateUtil.forString(user.getEndEmulationTime(), "yyyy-MM-dd HH:mm:ss"),
                    user.getUserName());
            if (flag) {
                try {
                    System.out.println("引擎就位！！！");
                    boolean bool = true;
                    while (bool) {
                        //获取当前用户，如果当前用户没了，说明点击了终止仿真。
                        User userVerify = userService.getBaseMapper().selectOne(new QueryWrapper<User>().eq("user_name", user.getUserName()));
                        if (userVerify != null){
                            //调用执行引擎的状态信息接口（获取引擎返回的时间戳）
                            MyResponseDto myResponseDto = emulationOutcomeService.fetchEngineState();
                            long time = Long.parseLong(myResponseDto.getTime());
//                        System.out.println("引擎返回的时间戳：" + time);
                            //获取用户输入的开始时间
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date startDate = null;
                            try {
                                startDate = sdf.parse(DateUtil.forString(user.getStartEmulationTime(), "yyyy-MM-dd HH:mm:ss"));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            long startTime = startDate.getTime();
//                        System.out.println("用户输入的开始时间：" + startTime);

                            //获取用户输入的结束时间
                            Date endDate = null;
                            try {
                                endDate = sdf.parse(DateUtil.forString(user.getEndEmulationTime(), "yyyy-MM-dd HH:mm:ss"));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            long endTime = endDate.getTime();
//                        System.out.println("用户输入的结束时间：" + endTime);
                            //如果引擎返回的时间 + 用户输入的当前时间 大于等于 用户输入结束时间，说明仿真流程结束
                            if ((time + startTime) >= endTime) {
                                bool = false;
                                //1、查询结果表中是否存在该流程的仿真结果了，如果存在先删除在存储一份，保证一个流程对应一个结果。
                                List<ActivityOutcome> activityOutcomeList = activityOutcomeService.getBaseMapper().selectList(new QueryWrapper<ActivityOutcome>().eq("user_name", userVerify.getUserName())
                                        .eq("history_process_id", userVerify.getHistoryProcessId()));
                                List<EmulationTimeOutcome> emulationTimeOutcomeList = emulationTimeOutcomeService.getBaseMapper().selectList(new QueryWrapper<EmulationTimeOutcome>().eq("user_name", userVerify.getUserName())
                                        .eq("history_process_id", userVerify.getHistoryProcessId()));
                                if (activityOutcomeList.size() > 0 && emulationTimeOutcomeList.size() > 0){
                                    //2、删除活动 & 整体流程结果数据
                                    // 设置批量删除条件(活动)
                                    LambdaQueryWrapper<ActivityOutcome> activityOutcomeLambdaQueryWrapper = Wrappers.lambdaQuery();
                                    activityOutcomeLambdaQueryWrapper.eq(ActivityOutcome::getUserName, userVerify.getUserName())
                                            .eq(ActivityOutcome::getHistoryProcessId, userVerify.getHistoryProcessId());
                                    // 执行删除操作(活动)
                                    activityOutcomeService.getBaseMapper().delete(activityOutcomeLambdaQueryWrapper);
                                    // 设置批量删除条件(整体流程)
                                    LambdaQueryWrapper<EmulationTimeOutcome> emulationTimeOutcomeLambdaQueryWrapper = Wrappers.lambdaQuery();
                                    emulationTimeOutcomeLambdaQueryWrapper.eq(EmulationTimeOutcome::getUserName, userVerify.getUserName())
                                            .eq(EmulationTimeOutcome::getHistoryProcessId, userVerify.getHistoryProcessId());
                                    // 执行删除操作(整体流程)
                                    emulationTimeOutcomeService.getBaseMapper().delete(emulationTimeOutcomeLambdaQueryWrapper);
                                }
                                //获取Value数据
                                MyResponseDto myResponse = emulationOutcomeService.fetchEngineState();
                                System.out.println("Value:"+myResponse.getValue());
                                Map<String, Object> map = myResponse.getValue();
                                //存储整体仿真数据
                                EmulationTimeOutcome emulationTimeOutcome = new EmulationTimeOutcome();
                                emulationTimeOutcome.setDisposeTimeMax((String) map.get("4"));
                                emulationTimeOutcome.setDisposeTimeMin((String) map.get("7"));
                                emulationTimeOutcome.setDisposeTimeAvg((String) map.get("1"));
                                emulationTimeOutcome.setAwaitTimeMax((String) map.get("5"));
                                emulationTimeOutcome.setAwaitTimeMin((String) map.get("8"));
                                emulationTimeOutcome.setAwaitTimeAvg((String) map.get("2"));
                                emulationTimeOutcome.setFinishTimeMax((String) map.get("6"));
                                emulationTimeOutcome.setFinishTimeMin((String) map.get("9"));
                                emulationTimeOutcome.setFinishTimeAvg((String) map.get("3"));
                                emulationTimeOutcome.setCreateTime(new Date());
                                emulationTimeOutcome.setHistoryProcessId(user.getHistoryProcessId());
                                emulationTimeOutcome.setUserName(user.getUserName());
                                emulationTimeOutcomeService.getBaseMapper().insert(emulationTimeOutcome);
                                //存储各个活动数据
                                Set<String> keys = map.keySet();
                                for (String key : keys) {
                                    //将key为1-10的数据，保留
                                    int num = Integer.parseInt(key);
                                    if (num >= 1 && num <= 10) {
                                        continue;
                                    }else {
                                        ResponseDto responseDto = (ResponseDto) map.get(key);
                                        ActivityOutcome activityOutcome = new ActivityOutcome();
                                        BeanUtils.copyProperties(responseDto,activityOutcome);
                                        activityOutcome.setCreateTime(new Date());
                                        activityOutcome.setHistoryProcessId(user.getHistoryProcessId());
                                        activityOutcome.setUserName(user.getUserName());
                                        activityOutcomeService.getBaseMapper().insert(activityOutcome);
                                    }
                                }
                                //删除用户
                                this.removeUser(user.getUserName());
                                System.out.println(user.getUserName() + "删除成功(仿真流程结束)...");
                            }
                        }else {
                            bool = false;
                        }
                    }
                }catch (Exception e){
                    System.out.println("");
                }
            }
            //删除用户
            this.removeUser(user.getUserName());
            System.out.println(user.getUserName() + "删除成功(仿真流程结束)...");
            return "用户执行完毕...";
        }
        return "队列中没有用户...";
    }

    //查询用户队列排名信息
    @Override
    public UserQueueMessageDto getUserQueue(String userName) {
        UserQueueMessageDto userQueue = this.getBaseMapper().getUserQueue(userName);
        return userQueue;
    }

    //获取到当前队列执行用户
    @Override
    public User getNextUser() {
        return this.getBaseMapper().getNextUser();
    }

    //判断该用户是否仿真结束
    @Override
    public boolean userFZ(String userName) {
        User user = this.getBaseMapper().selectOne(new QueryWrapper<User>().eq("user_name", userName));
        return user != null ? true : false;
    }

    //根据用户名称获取用户信息
    @Override
    public User getUserByUserName(String userName) {
        User user = this.getBaseMapper().selectOne(new QueryWrapper<User>().eq("user_name", userName));
        return user;
    }
}




