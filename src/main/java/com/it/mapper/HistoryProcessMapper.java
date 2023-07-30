package com.it.mapper;

import com.it.domain.HistoryProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
* @author 胡浩
* @description 针对表【sys_history_process】的数据库操作Mapper
* @createDate 2023-07-20 16:11:40
* @Entity com.it.domain.HistoryProcess
*/
@Repository
public interface HistoryProcessMapper extends BaseMapper<HistoryProcess> {

    void inserts(HistoryProcess historyProcess);
}




