package com.it;

import com.it.util.BeanTools;
import com.it.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class SpringBootStencilApplicationTests {

    @BeforeEach
    public void before() {
        System.out.println(1);
    }

    @Test
    void contextLoads() {
        String startEmulationTime = "2023-07-27 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(startEmulationTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long timestamp = date.getTime();
        System.out.println(timestamp);


    }

    @Test
    void test1(){
//        long timestamp = 1690423200000l;
        long timestamp = 1690423200l;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date(timestamp));
        System.out.println(dateStr);
    }

    @Test
    void test2(){
        String numberStr = "0.9";
        double number = Double.parseDouble(numberStr);
        DecimalFormat df = new DecimalFormat("0%");
        String percentage = df.format(number);
        System.out.println(percentage);
    }

}
