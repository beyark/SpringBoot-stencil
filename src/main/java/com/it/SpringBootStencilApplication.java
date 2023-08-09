package com.it;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.it.mapper")
@EnableScheduling   //开启定时任务
@EnableOpenApi      //swagger
public class SpringBootStencilApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStencilApplication.class, args);
    }
}
