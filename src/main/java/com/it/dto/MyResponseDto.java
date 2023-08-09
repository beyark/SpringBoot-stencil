package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
/**
 * @description: 引擎执行状态整体数据接收
 * @date: 2023/8/2 12:54
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponseDto {
    private String time;
    private Map<String, Object> movingImages;
    private Map<String, Object> value;
    private Map<String, Object> staticImages;
    private String logs;
}