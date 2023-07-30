package com.it.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot-stencil
 * @description:
 * @author: 胡浩
 * @create: 2023-06-26 14:21
 **/
@Component
public class Test {
    @Scheduled(cron = "*/5 * * * * *") // 5秒
    public void test(){
//        System.out.println("123");
    }
}
