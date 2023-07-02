package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.domain.User;
import com.it.service.UserService;
import com.it.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 胡浩
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-06-26 12:28:20
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUser(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean deleteUserById(int userId) {
        int i = userMapper.deleteById(userId);
        return i == 1 ? true : false;
    }
}




