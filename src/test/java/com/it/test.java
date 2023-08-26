package com.it;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @program: SpringBoot-stencil
 * @description:
 * @author: 胡浩
 * @create: 2023-08-26 11:58
 **/
public class test {
    public static void main(String[] args) {
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
}
