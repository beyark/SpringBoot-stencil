package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
                    DateUtil.forString(user.getEndEmulationTime(), "yyyy-MM-dd HH:mm:ss"));
            if (flag) {
                boolean bool = true;
                while (bool) {
                    MyResponseDto myResponseDto = emulationOutcomeService.fetchEngineState();
                    long time = Long.parseLong(myResponseDto.getTime());
                    System.out.println("引擎返回的当前时间：" + time);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(DateUtil.forString(user.getEndEmulationTime(), "yyyy-MM-dd HH:mm:ss"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    long timestamp = date.getTime() / 1000; // 将毫秒转换为秒
                    System.out.println("用户输入的结束时间：" + timestamp);

                    if (time >= timestamp) {
                        bool = false;
                        MyResponseDto myResponse = emulationOutcomeService.fetchEngineState();
                        System.out.println("=============="+myResponse.getValue());
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
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.removeUser(user.getUserName());
            System.out.println(user.getUserName() + "删除成功...");
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
}




