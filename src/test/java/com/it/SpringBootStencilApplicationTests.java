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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class SpringBootStencilApplicationTests {

    @BeforeEach
    public void before() {

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
    void test1() {
        String str = "23456";
        if (str.length() > 0) {
            str = "1" + str.substring(1);
        }
        System.out.println(str);

        char firstChar = str.charAt(0);
        System.out.println(firstChar);
    }

    @Test
    void test2() {
        String dateString = "1970-01-01 12:00:00";
        String pattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(dateString);
            long timestamp = date.getTime() / 1000; // 将毫秒转换为秒
            System.out.println("Timestamp: " + timestamp);
        } catch (ParseException e) {
            System.out.println("Error occurred while parsing the date: " + e.getMessage());
        }
    }

    @Test
    void test3() {
        int num1 = 2;
        int num2 = 4;
        int executionFrequency = 5;
        String result = "";

        for (int i = 0; i < executionFrequency; i++) {
            if (i < num1) {
                result += "0,";
            } else if (i < num2) {
                result += "1,";
            } else {
                result += "0,";
            }
        }
        // 移除最后一个逗号
        result = result.substring(0, result.length() - 1);
        System.out.println(result);
    }
}