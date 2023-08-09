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
    User getNextUser();

    //查询用户队列排名信息
    @Select("SELECT \n" +
            "    (SELECT COUNT(*) FROM sys_user) AS sum,\n" +
            "    ranking\n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT \n" +
            "            ROW_NUMBER() OVER (ORDER BY id) AS ranking,\n" +
            "            user_name\n" +
            "        FROM \n" +
            "            sys_user\n" +
            "    ) AS subquery\n" +
            "WHERE \n" +
            "    user_name = #{userName};")
    UserQueueMessageDto getUserQueue(@Param("userName") String userName);
}




