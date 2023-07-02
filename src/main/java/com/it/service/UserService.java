package com.it.service;

import com.it.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 胡浩
* @description 针对表【user】的数据库操作Service
* @createDate 2023-06-26 12:28:20
*/
public interface UserService extends IService<User> {
    User getUser(int id);

    boolean deleteUserById(int userId);

}
