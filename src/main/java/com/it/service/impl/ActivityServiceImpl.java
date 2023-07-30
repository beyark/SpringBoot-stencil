package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.domain.Activity;
import com.it.service.ActivityService;
import com.it.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author 胡浩
* @description 针对表【sys_activity】的数据库操作Service实现
* @createDate 2023-07-20 16:11:40
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}




