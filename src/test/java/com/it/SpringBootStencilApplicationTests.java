package com.it;

import com.it.util.BeanTools;
import com.it.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        String encodedDate = "2023-08-26 11%3A39%3A53";

        // URL 解码
        String decodedDate = null;
        try {
            decodedDate = URLDecoder.decode(encodedDate, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(decodedDate);
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

    @Test
    void test4() {
        String startTime = "2023-07-27 10:00:00";
        String endTime = "2023-07-27 12:00:00";
        String currentTime = "2023-07-27 11:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Date currentDate = sdf.parse(currentTime);

            long totalTime = endDate.getTime() - startDate.getTime();
            long elapsedTime = currentDate.getTime() - startDate.getTime();

            double progress = (double) elapsedTime / totalTime;

            int progressBarWidth = 20; // 进度条的宽度（单位：字符）
            int filledWidth = (int) (progress * progressBarWidth);

            StringBuilder progressBar = new StringBuilder();
            progressBar.append("[");
            for (int i = 0; i < progressBarWidth; i++) {
                if (i < filledWidth) {
                    progressBar.append("#");
                } else {
                    progressBar.append(" ");
                }
            }
            progressBar.append("]");

            System.out.println("进度条：" + progressBar.toString());
            System.out.println("进度：" + (int) (progress * 100) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}