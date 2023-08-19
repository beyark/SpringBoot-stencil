package com.it.service.impl;

import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.dto.ResultDto2;
import com.it.dto.SelectProcessListDto;
import com.it.service.ProcessInquiryService;
import com.it.vo.ProcessInquiryVo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.List;

/**
 * @program: SpringBoot-stencil
 * @create: 2023-07-18 22:11
 **/
@Service
public class ProcessInquiryServiceImpl implements ProcessInquiryService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${thirdparty.api.url}")
    private String thirdPartyApiUrl;

    @Override
    public SelectProcessListDto selectProcessList(ProcessInquiryVo processInquiryVo) {
        System.out.println("==="+thirdPartyApiUrl);
        String url = "http://"+thirdPartyApiUrl+":10069/physical/searchByLabel";
        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", processInquiryVo.getAuthorization());
        headers.add("environment", processInquiryVo.getEnvironment());
        headers.setContentType(MediaType.APPLICATION_JSON);

        //放入body中的json参数
        JSONObject obj = new JSONObject();
        obj.put("label", "liucheng1");
        obj.put("page", processInquiryVo.getPage());
        obj.put("pageSize", processInquiryVo.getPageSize());
        obj.put("to_labels",new JSONArray());
        obj.put("where",new JSONObject());
        obj.put("filters",new JSONArray());
        obj.put("keyword","");
        obj.put("order", new JSONArray());

        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();
        // 将 JSON 字符串解析为 JSONObject 对象
        JSONObject jsonObject = JSONObject.parseObject(body);

        SelectProcessListDto selectProcessListDto = new SelectProcessListDto();
        if ("success".equals(jsonObject.get("msg"))){
            JSONObject data = jsonObject.getJSONObject("data");
            Integer count = (Integer) data.get("count");
            List list = (List) data.get("list");
            selectProcessListDto.setCount(count);
            selectProcessListDto.setData(list);
            return selectProcessListDto;
        }
        return selectProcessListDto;
    }

    @Override
    public ResultDto2 selectProcessDetails(ProcessInquiryVo processInquiryVo) {
        String url = "http://"+thirdPartyApiUrl+":18686/processView/findOneFlowChart";

        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", processInquiryVo.getAuthorization());
        headers.add("environment", processInquiryVo.getEnvironment());
        headers.setContentType(MediaType.APPLICATION_JSON);

        //放入body中的json参数
        JSONObject obj = new JSONObject();
        obj.put("processViewId", processInquiryVo.getLicheng1_id());

        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();

        // 将 JSON 字符串解析为 JSONObject 对象
        JSONObject resultJsonObject = JSONObject.parseObject(body);
        ResultDto2 resultDto = null;
        if ("success".equals(resultJsonObject.get("msg"))){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                resultDto = objectMapper.readValue(resultJsonObject.get("result").toString(), ResultDto2.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return resultDto;
        }
        return resultDto;
    }
}
