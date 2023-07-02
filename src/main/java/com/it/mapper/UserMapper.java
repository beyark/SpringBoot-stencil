package com.it.mapper;

import com.it.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 胡浩
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-06-26 12:28:20
* @Entity com.it.domain.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




