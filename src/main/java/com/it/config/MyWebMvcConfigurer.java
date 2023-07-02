package com.it.config;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源映射路径 为springboot访问静态资源做准备
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  /java/sp/** 映射到哪里去,前端里面的路由      file://java/sp/ 表示需要访问linux系统文件所在的文件夹路径名称
        registry.addResourceHandler("/java/sp/**").addResourceLocations("file:/java/sp/");
    }
}