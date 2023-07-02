package com.it.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
@EnableOpenApi //swagger3启动注释
public class SwaggerConfig {
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi(){
        Docket docket = new Docket(DocumentationType.OAS_30).groupName("xxx项目");
        docket.apiInfo(apiInfo())
                .select()
                //扫描注解的位置
                .apis(RequestHandlerSelectors.basePackage("com.it.controller"))
                .build();
        return docket;
    }

    private ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("hu_hao","","");
        return new ApiInfo(
                swaggerProperties.getApplicationName() + "APi",
                swaggerProperties.getApplicationDescription(),
                swaggerProperties.getApplicationVersion(),
                //服务Url
                "urn:tos",
                contact,
                "xxx项目 1.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
