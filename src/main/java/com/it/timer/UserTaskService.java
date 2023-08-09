package com.it.timer;

import com.it.controller.UserController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 定时任务,每5秒自动执行一次查询用户队列并执行
 * @date: 2023/8/6 21:52
 **/
@Service
public class UserTaskService {
    private final UserController userController;
    private volatile boolean isExecuting = false;

    public UserTaskService(UserController userController) {
        this.userController = userController;
        scheduleTask();
    }

    @Scheduled(fixedDelay = 5000)
    public void executeUserTask() {
        if (isExecuting) {
            return;
        }
        isExecuting = true;
        String result = userController.executeUser();
        isExecuting = false;
        System.out.println(result);
    }

    private void scheduleTask() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::executeUserTask, 0, 5, TimeUnit.SECONDS);
    }
}