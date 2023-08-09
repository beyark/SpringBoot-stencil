//package com.it.util;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
///**
// * @program: wanshu
// * @description: 公共工具类
// * @create: 2023-07-12 22:09
// **/
//public class WebUtils {
//
//    /**
//     * @description: 密码加盐
//     * @date: 2023/7/12 22:10
//     * @param: password
//     * @return: 加密后的password
//     **/
//    public static String passwordEncode(String password){
//        return new BCryptPasswordEncoder().encode(password);
//    }
//
//    /**
//     * @description: 获取当前登录用户
//     * @date: 2023/7/16 23:00
//     * @return: java.lang.String
//     **/
//    public static String loginUserName(){
//        //获取当前登录用户
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
//        return currentUser.getUsername();
//    }
//
//}
