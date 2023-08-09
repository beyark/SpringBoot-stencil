package com.it.service;

import com.it.domain.HistoryProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.dto.SelectHistoryProcessDto;
import com.it.util.PageUtils;
import com.it.vo.SaveHistoricalProcessesVO;
import org.apache.ibatis.annotations.Options;

import java.util.Map;

/**
* @description 针对表【sys_history_process】的数据库操作Service
* @createDate 2023-07-20 16:11:40
*/
public interface HistoryProcessService extends IService<HistoryProcess> {
    //流程效验接口
    String processValidation(String historyProcessId, String nodeId, String type);

    //保存&另存为历史流程接口
    String saveHistoricalProcesses(SaveHistoricalProcessesVO saveHistoricalProcessesVO);

    //查询历史流程列表(分页)
    PageUtils selectHistoryProcessList(Map<String, Object> map);

    //查询历史流程
    SelectHistoryProcessDto selectHistoryProcess(Integer historyProcessId);


}
