package com.it.timer;

import com.it.domain.User;
import com.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserService userService;

    private final UserTaskService userTaskService;

    public MyApplicationRunner(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    /**
     * @description: 重启项目，清空队列用户
     * @date: 2023/8/22 10:04
     **/
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<User> userList = userService.getBaseMapper().selectList(null);
        if (userList.size() > 0){
            userService.deleteUserAll();
        }
        //开启定时器
        userTaskService.startUserTask();
    }
}
