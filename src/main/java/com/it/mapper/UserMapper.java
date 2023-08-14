package com.it.mapper;

import com.it.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.dto.UserQueueMessageDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2023-08-06 20:45:22
* @Entity com.it.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    //获取到当前队列执行用户
    @Select("SELECT * FROM sys_user LIMIT 1 FOR UPDATE")
//    @Select("SELECT * FROM sys_user LIMIT 1")
    User getNextUser();

    //查询用户队列排名信息
    @Select("SELECT \n" +
            "    (SELECT COUNT(*) FROM sys_user) AS sum,\n" +
            "    tab.user_name,\n" +
            "    tab.ranking\n" +
            "FROM\n" +
            "    (\n" +
            "    SELECT\n" +
            "        user_name,\n" +
            "        (SELECT COUNT(*) + 1\n" +
            "         FROM sys_user AS u2\n" +
            "         WHERE u2.id < u1.id) AS ranking\n" +
            "\t\tFROM\n" +
            "        sys_user AS u1\n" +
            "\t\tWHERE\n" +
            "        user_name = #{userName}\n" +
            "    ) AS tab;")
    UserQueueMessageDto getUserQueue(@Param("userName") String userName);
}




