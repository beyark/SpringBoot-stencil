package com.it.service;

import com.it.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.dto.UserQueueMessageDto;

/**
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2023-08-06 20:45:22
*/
public interface UserService extends IService<User> {

    //新增用户
    Boolean addUser(String userName,Integer historyProcessId, String startEmulationTime, String endEmulationTime);

    //删除用户
    Boolean removeUser(String name);

    //执行任务
    String executeUser();

    //查询用户队列排名信息
    UserQueueMessageDto getUserQueue(String userName);
}
