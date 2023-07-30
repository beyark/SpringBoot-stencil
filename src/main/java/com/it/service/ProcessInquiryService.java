package com.it.service;


//import com.it.dto.ResultDto;
import com.it.dto.ResultDto2;
import com.it.dto.SelectProcessListDto;
import com.it.vo.ProcessInquiryVo;

import java.util.List;

public interface ProcessInquiryService {

    //查询流程列表
    SelectProcessListDto selectProcessList(ProcessInquiryVo processInquiryVo);

    //查询流程详细信息
    ResultDto2 selectProcessDetails(ProcessInquiryVo processInquiryVo);
}
