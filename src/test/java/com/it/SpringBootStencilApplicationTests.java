package com.it;

import com.it.util.BeanTools;
import com.it.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SpringBootStencilApplicationTests {

    @BeforeEach
    public void before() {
        System.out.println(1);
    }

    @Test
    void contextLoads() {

    }

}
