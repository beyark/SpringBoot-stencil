package com.it.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.domain.*;
import com.it.dto.MyResponseDto;
import com.it.dto.ResponseDto;
import com.it.service.*;
import okhttp3.Headers;
import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: SpringBoot-stencil
 * @description: 仿真执行、结果模块
 * @create: 2023-07-25 13:56
 **/
@Service
public class EmulationOutcomeServiceImpl implements EmulationOutcomeService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityActivityService activityActivityService;
    @Resource
    private GatewayService gatewayService;
    @Resource
    private ActivityGatewayService activityGatewayService;
    @Resource
    private GatewaySideService gatewaySideService;
    @Resource
    private UserService userService;
    @Resource
    private EmulationOutcomeService emulationOutcomeService;
    @Resource
    private EmulationTimeOutcomeService emulationTimeOutcomeService;
    @Resource
    private ActivityOutcomeService activityOutcomeService;
    @Resource
    private XmlUrlService xmlUrlService;

    //生成XML文件
    @Override
    public String generateXML(Integer historyProcessId, String startEmulationTime,String endEmulationTime) {
        try {
            // 定义文件名和路径
            String fileName = historyProcessId + "-" + System.currentTimeMillis() + ".xml";
//            String filePath = "C:\\test\\" + fileName;
            String filePath = "/LCFZ/java/xml/" + fileName;

            // 创建 PrintWriter 对象，用于输出 XML 内容到文件
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //设置头部（endEmulationTime）
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat.parse(endEmulationTime);
            Date date2 = dateFormat.parse(startEmulationTime);
            long differenceInMilliseconds = (date1.getTime() - date2.getTime()) * 2;
            long seconds = differenceInMilliseconds / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            String timeString = String.format("%02d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60);

            // 输出 XML 头信息和根元素
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            writer.println("<!DOCTYPE Model SYSTEM \"https://a-herzog.github.io/Warteschlangensimulator/Simulator.dtd\">");
            writer.println("<Model xmlns=\"https://a-herzog.github.io\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"https://a-herzog.github.io https://a-herzog.github.io/Warteschlangensimulator/Simulator.xsd\">");

            // 输出模型版本、作者、客户数、热身阶段和终止时间等元素
            writer.println("  <ModelVersion>5.3.0</ModelVersion>");
            writer.println("  <ModelAuthor>msewi</ModelAuthor>");
            writer.println("  <ModelClients Active=\"0\">10000000</ModelClients>");
            writer.println("  <ModelWarmUpPhase>0.01</ModelWarmUpPhase>");
            writer.println("  <ModelTerminationTime Active=\"1\">"+ timeString +"</ModelTerminationTime>");
            writer.println("  <ModelElements>");

            Map<String, String> map = new HashMap<>(); // 用于存储分发器
            //编写活动XML
            List<Activity> activityList = activityService.getBaseMapper().selectList(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId));
            //终点边的唯一性变量
            int zyz = 1;
            //终点边的集合
            List<String> zdList = new ArrayList<>();
            for (Activity activity : activityList) {
                if (activity.getType() == 1) {
                    // activity.getType() == 1 说明为开始活动
                    //设置事件发生源（Source）以及定义边和起点绑定
                    writer.println("    <ModelElementSource id=\"" + "12" + activity.getActivityId() + "\">");
                    writer.println("      <ModelElementName>Clients</ModelElementName>");
                    writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                    writer.println("      <ModelElementConnection Element=\"" + "11" + activity.getActivityId() + "\" Type=\"Out\"/>");

                    // 设置活动执行频率
                    Integer executionFrequency = activity.getExecutionFrequency(); //executionFrequency: 执行频率
                    Integer distinctValue = activity.getDistinctValue();
                    if (activity.getTimeType() == 0 && activity.getEventType() == 0) {
                        // 秒(固定值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Seconds\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入国定值
                        writer.println("      <ModelElementBatchData Size=\"" + distinctValue + "\"/>");
                    } else if (activity.getTimeType() == 0 && activity.getEventType() == 1) {
                        // 秒(随机值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Seconds\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入随机值
                        String result = "";
                        for (int i = 0; i < executionFrequency; i++) {
                            if (i < activity.getRandom1()) {
                                result += "0;";
                            } else if (i < activity.getRandom2()) {
                                result += "1;";
                            } else {
                                result += "0;";
                            }
                        }
                        // 移除最后一个分号
                        result = result.substring(0, result.length() - 1);
                        writer.println("      <ModelElementBatchData Size=\"" + result + "\"/>");
                    }
                    if (activity.getTimeType() == 1 && activity.getEventType() == 0) {
                        // 分(固定值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Minutes\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入国定值
                        writer.println("      <ModelElementBatchData Size=\"" + distinctValue + "\"/>");
                    } else if (activity.getTimeType() == 1 && activity.getEventType() == 1) {
                        // 分(随机值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Minutes\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入随机值
                        String result = "";
                        for (int i = 0; i < executionFrequency; i++) {
                            if (i < activity.getRandom1()) {
                                result += "0;";
                            } else if (i < activity.getRandom2()) {
                                result += "1;";
                            } else {
                                result += "0;";
                            }
                        }
                        // 移除最后一个逗号
                        result = result.substring(0, result.length() - 1);
                        writer.println("      <ModelElementBatchData Size=\"" + result + "\"/>");
                    }
                    if (activity.getTimeType() == 2 && activity.getEventType() == 0) {
                        // 时(固定值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Hours\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入国定值
                        writer.println("      <ModelElementBatchData Size=\"" + distinctValue + "\"/>");
                    } else if (activity.getTimeType() == 2 && activity.getEventType() == 1) {
                        // 时(随机值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Hours\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入随机值
                        String result = "";
                        for (int i = 0; i < executionFrequency; i++) {
                            if (i < activity.getRandom1()) {
                                result += "0;";
                            } else if (i < activity.getRandom2()) {
                                result += "1;";
                            } else {
                                result += "0;";
                            }
                        }
                        // 移除最后一个逗号
                        result = result.substring(0, result.length() - 1);
                        writer.println("      <ModelElementBatchData Size=\"" + result + "\"/>");
                    }
                    if (activity.getTimeType() == 3 && activity.getEventType() == 0) {
                        // 天(固定值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Days\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入国定值
                        writer.println("      <ModelElementBatchData Size=\"" + distinctValue + "\"/>");
                    } else if (activity.getTimeType() == 3 && activity.getEventType() == 1) {
                        // 天(随机值)
                        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Days\">" + executionFrequency + "</ModelElementExpression>");
                        // 设置活动事件输入随机值
                        String result = "";
                        for (int i = 0; i < executionFrequency; i++) {
                            if (i < activity.getRandom1()) {
                                result += "0;";
                            } else if (i < activity.getRandom2()) {
                                result += "1;";
                            } else {
                                result += "0;";
                            }
                        }
                        // 移除最后一个逗号
                        result = result.substring(0, result.length() - 1);
                        writer.println("      <ModelElementBatchData Size=\"" + result + "\"/>");
                    }

                    writer.println("    </ModelElementSource>");

                    writer.println("    <ModelElementEdge id=\"" + "11" +  activity.getActivityId() + "\">");
                    writer.println("      <ModelElementConnection Element1=\"" + "12" + activity.getActivityId() + "\" Element2=\"" + "1" + activity.getActivityId() + "\" Type=\"Edge\"/>");
                    writer.println("    </ModelElementEdge>");

                    // 活动id
                    writer.println("    <ModelElementProcessStation id=\"" + "1" + activity.getActivityId() + "\">");
                    // 活动名称
                    writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                    // 活动的坐标
                    writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                    // 活动执行人
                    writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");

                    writer.println("      <ModelElementConnection Element=\"" + "11" + activity.getActivityId() + "\" Type=\"In\"/>");

                    // 设置开始活动的下一个节点
                    List<ActivityActivity> activityActivityList = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId)
                            .eq("activity_one", activity.getId()));
                    List<ActivityGateway> activityGatewayList = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId)
                            .eq("value", 0)
                            .eq("activity_id", activity.getId()));
                    if (activityActivityList != null && activityActivityList.size() == 1) {
                        writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivityList.get(0).getId() + "\" Type=\"Out\"/>");
                    } else {
                        if (activityGatewayList != null && activityGatewayList.size() > 0) {
                            writer.println("      <ModelElementConnection Element=\"" + "11" + activityGatewayList.get(0).getId() + "\" Type=\"Out\"/>");
                        }
                    }
                    // 活动处理时间、等待处理时间
                    extracted(writer, activity);

                    writer.println("    </ModelElementProcessStation>");

                    // 设置该活动的TextXML
                    extracted1(writer, activity);
                } else if (activity.getType() == 0) {
                    // activity.getType() == 0 说明为中间活动
                    List<ActivityGateway> activityGatewayList = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId)
                            .eq("activity_id", activity.getId())
                            .eq("value", 0));
                    List<ActivityActivity> activityActivities = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId)
                            .eq("activity_one", activity.getId()));
                    List<ActivityActivity> activityActivities2 = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId)
                            .eq("activity_two", activity.getId()));
                    List<ActivityGateway> activityGatewayList2 = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId)
                            .eq("activity_id", activity.getId())
                            .eq("value", 1));
                    List<String> numList = new ArrayList<>();
//                if (activityGatewayList.size() > 0 && activityActivities.size() > 0) {
                    if ((activityGatewayList.size() + activityActivities.size()) > 1) {
                        // 产生了分发器，存入到分发器集合
                        writer.println("    <ModelElementEdge id=\"" + "11" + activity.getActivityId() + "\">");
                        writer.println("      <ModelElementConnection Element1=\"" + "1" + activity.getActivityId() + "\" Element2=\"" + "10" + activity.getActivityId() + "\" Type=\"Edge\"/>");
                        writer.println("    </ModelElementEdge>");

                        // 说明 这个活动对应多个输出节点，新增一个分发器
                        writer.println("    <ModelElementDuplicate id=\"" + "10" + activity.getActivityId() + "\">");
                        writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"150\" y=\"170\"/>");
                        writer.println("      <ModelElementConnection Element=\"" + "11" + activity.getActivityId() + "\" Type=\"In\"/>");
                        // 拿下一个节点(下一个节点为活动的情况)
                        if (activityActivities != null) {
                            for (int i = 0; i < activityActivities.size(); i++) {
                                map.put("1" + activityActivities.get(i).getActivityTwo(), ("11" + numList.size()) + activity.getActivityId());
                                writer.println("      <ModelElementConnection Element=\"" + ("11" + numList.size()) + activity.getActivityId() + "\" Type=\"Out\"/>");
                                numList.add(activityActivities.get(i).getActivityTwo());
                            }
                        }
                        // 拿下一个节点(下一个节点为网关的情况)
                        if (activityGatewayList != null) {
                            for (ActivityGateway activityGateway : activityGatewayList) {
                                map.put("2" + activityGateway.getGatewayId(), ("11" + numList.size()) + activity.getActivityId());
                                writer.println("      <ModelElementConnection Element=\"" + ("11" + numList.size()) + activity.getActivityId() + "\" Type=\"Out\"/>");
                                numList.add(activityGateway.getGatewayId());
                            }
                        }
                        writer.println("    </ModelElementDuplicate>");

                        for (int i = 0; i < numList.size(); i++) {
                            Activity activity1 = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                                    .eq("id", numList.get(i)));
                            Gateway gateway = gatewayService.getBaseMapper().selectOne(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId)
                                    .eq("id", numList.get(i)));
                            writer.println("    <ModelElementEdge id=\"" + ("11" + i) + activity.getActivityId() + "\">");
                            if (activity1 != null) {
                                writer.println("      <ModelElementConnection Element1=\"" + "10" + activity.getActivityId() + "\" Element2=\"" + "1" + activity1.getActivityId() + "\" Type=\"Edge\"/>");
                            }
                            if (gateway != null) {
                                writer.println("      <ModelElementConnection Element1=\"" + "10" + activity.getActivityId() + "\" Element2=\"" + "2" + gateway.getGatewayId() + "\" Type=\"Edge\"/>");
                            }
                            writer.println("    </ModelElementEdge>");
                        }

                        // 活动id
                        writer.println("    <ModelElementProcessStation id=\"" + "1" + activity.getActivityId() + "\">");
                        // 活动名称
                        writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                        // 活动的坐标
                        writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                        // 类型
                        writer.println("      <ModelElementPriority ClientType=\"Clients\">w</ModelElementPriority>");
                        // 优先度
                        writer.println("      <ModelElementOperatorsPriority>1</ModelElementOperatorsPriority>");
                        // 活动执行人
                        writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");
                        //设置输出位 分发器
                        writer.println("      <ModelElementConnection Element=\"" + "11" + activity.getActivityId() + "\" Type=\"Out\"/>");
                        // 拿上一个节点(上一个节点为活动的情况)
                        if (activityActivities2 != null) {
                            for (ActivityActivity activityActivity : activityActivities2) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivity.getId() + "\" Type=\"In\"/>");
                            }
                        }
                        // 拿上一个节点(上一个节点为网关的情况)
                        if (activityGatewayList2 != null) {
                            for (ActivityGateway activityGateway : activityGatewayList2) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"In\"/>");
                            }
                        }
                        // 活动处理时间、等待处理时间
                        extracted(writer, activity);
                        writer.println("    </ModelElementProcessStation>");
                    } else {
                        // 活动id
                        writer.println("    <ModelElementProcessStation id=\"" + "1" + activity.getActivityId() + "\">");
                        // 活动名称
                        writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                        // 活动的坐标
                        writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                        // 类型
                        writer.println("      <ModelElementPriority ClientType=\"Clients\">w</ModelElementPriority>");
                        // 优先度
                        writer.println("      <ModelElementOperatorsPriority>1</ModelElementOperatorsPriority>");
                        // 活动执行人
                        writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");
                        // 设置中间活动的上和下的节点
                        // 拿下一个节点(下一个节点为活动的情况)
                        if (activityActivities != null) {
                            for (ActivityActivity activityActivity : activityActivities) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivity.getId() + "\" Type=\"Out\"/>");
                            }
                        }
                        // 拿下一个节点(下一个节点为网关的情况)
                        if (activityGatewayList != null) {
                            for (ActivityGateway activityGateway : activityGatewayList) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"Out\"/>");
                            }
                        }
                        // 拿上一个节点(上一个节点为活动的情况)
                        Set<String> keys = map.keySet();
                        if (activityActivities2 != null) {
                            for (ActivityActivity activityActivity : activityActivities2) {
                                int num = 0;
                                for (String key : keys) {
                                    if (("1" + activityActivity.getActivityTwo()).equals(key)) {
                                        writer.println("      <ModelElementConnection Element=\"" + map.get(key) + "\" Type=\"In\"/>");
                                        num++;
                                    }
                                }
                                if (num == 0) {
                                    writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivity.getId() + "\" Type=\"In\"/>");
                                }
                            }
                        }
                        // 拿上一个节点(上一个节点为网关的情况)
                        if (activityGatewayList2 != null) {
                            for (ActivityGateway activityGateway : activityGatewayList2) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"In\"/>");

                            }
                        }
                        // 活动处理时间、等待处理时间
                        extracted(writer, activity);
                        writer.println("    </ModelElementProcessStation>");
                    }
                    // 设置该活动的TextXML
                    extracted1(writer, activity);
                } else if (activity.getType() == 2) {
                    // activity.getType() == 2 说明为结束活动
                    //设置终点边 绑定终点
                    writer.println("    <ModelElementEdge id=\"" + "11" +  activity.getActivityId() + zyz + "\">");
                    writer.println("      <ModelElementConnection Element1=\"" + "1" + activity.getActivityId() + "\" Element2=\"" + "13888" +  "\" Type=\"Edge\"/>");
                    writer.println("    </ModelElementEdge>");
                    zdList.add("11" +  activity.getActivityId() + zyz);

                    // 活动id
                    writer.println("    <ModelElementProcessStation id=\"" + "1" + activity.getActivityId() + "\">");
                    // 活动名称
                    writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                    // 活动的坐标
                    writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                    // 活动执行人
                    writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");

                    writer.println("      <ModelElementConnection Element=\"" + "11" +  activity.getActivityId() + zyz + "\" Type=\"Out\"/>");

                    // 设置中间活动的上和下的节点
                    List<ActivityActivity> activityActivityList = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId)
                            .eq("activity_two", activity.getId()));
                    List<ActivityGateway> activityGatewayList = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId)
                            .eq("activity_id", activity.getId()));
                    if (activityActivityList != null && activityActivityList.size() > 0) {
                        for (ActivityActivity activityActivity : activityActivityList) {
                            writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivity.getId() + "\" Type=\"In\"/>");
                        }
                    }
                    if (activityGatewayList != null && activityGatewayList.size() > 0) {
                        for (ActivityGateway activityGateway : activityGatewayList) {
                            writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"In\"/>");
                        }
                    }
                    // 活动处理时间、等待处理时间
                    extracted(writer, activity);
                    writer.println("    </ModelElementProcessStation>");

                    // 设置该活动的TextXML
                    extracted1(writer, activity);
                }
            }

            // 编写网关XML
            List<Gateway> gatewayList = gatewayService.getBaseMapper().selectList(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId));
            for (Gateway gateway : gatewayList) {
                writer.println("    <ModelElementDecide id=\"" + "2" + gateway.getGatewayId() + "\">");
                writer.println("      <ModelElementName>" + gateway.getName() + "</ModelElementName>");
                writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"425\" y=\"215\"/>");
                writer.println("      <ModelElementDecideMode>Random</ModelElementDecideMode>");
                List<ActivityGateway> activityGatewayList = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId)
                        .eq("gateway_id", gateway.getId()));
                for (ActivityGateway activityGateway : activityGatewayList) {
                    if (activityGateway.getValue() == 0) {
                        // 等于0 为 活动指向网关，对应输入（一般单输入）
                        Activity activity = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                                .eq("id", activityGateway.getActivityId()));
                        int num = 0;
                        for (String key : map.keySet()) {
                            if (("2" + activityGateway.getGatewayId()).equals(key)) {
                                writer.println("      <ModelElementConnection Element=\"" + map.get(key) + "\" Type=\"In\"/>");
                                num++;
                            }
                        }
                        if (num == 0) {
                            writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"In\"/>");
                        }

                    } else {
                        // 否则 为 网关指向活动，对应输出（多输出）
                        GatewaySide gatewaySide = gatewaySideService.getBaseMapper().selectOne(new QueryWrapper<GatewaySide>().eq("history_process_id", historyProcessId)
                                .eq("side_id", activityGateway.getSideId()));
                        writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Rate=\"" + (int) (Double.parseDouble(gatewaySide.getProbabilityValue()) * 10) + "\"  Type=\"Out\"/>");
                    }
                }
                writer.println("    </ModelElementDecide>");
            }

            // 编写边XML
            List<ActivityActivity> activityActivityList = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId));
            List<ActivityGateway> activityGatewayList = activityGatewayService.getBaseMapper().selectList(new QueryWrapper<ActivityGateway>().eq("history_process_id", historyProcessId));
            // 活动与活动的边
            for (ActivityActivity activityActivity : activityActivityList) {
                Activity activityOne = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                        .eq("id", activityActivity.getActivityOne()));
                Activity activityTwo = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                        .eq("id", activityActivity.getActivityTwo()));
                Set<String> mapKeys = map.keySet();
                int num = 0;
                for (String mapKey : mapKeys) {
                    if (("1" + activityActivity.getActivityTwo()).equals(mapKey)) {
                        num = 1;
                    }
                }
                if (num == 0) {
                    writer.println("    <ModelElementEdge id=\"" + "11" + activityActivity.getId() + "\">");
                    writer.println("      <ModelElementConnection Element1=\"" + "1" + activityOne.getActivityId() + "\" Element2=\"" + "1" + activityTwo.getActivityId() + "\" Type=\"Edge\"/>");
                    writer.println("    </ModelElementEdge>");
                }
            }
            // 活动与网关的边
            for (ActivityGateway activityGateway : activityGatewayList) {
                // 获取到该边相关的节点信息（下面设置边的时候，设定的是节点ID指向节点ID）
                Activity activity = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                        .eq("id", activityGateway.getActivityId()));
                Gateway gateway = gatewayService.getBaseMapper().selectOne(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId)
                        .eq("id", activityGateway.getGatewayId()));
                if (activityGateway.getValue() == 1) {
                    // value == 1 说明是网关指向活动，需要设置概率值
                    GatewaySide gatewaySide = gatewaySideService.getBaseMapper().selectOne(new QueryWrapper<GatewaySide>().eq("history_process_id", historyProcessId)
                            .eq("side_id", activityGateway.getSideId())); // 获取概率值
                    // 将数据库里面的概率值转换为百分比（如：0.9==>90%）
                    double number = Double.parseDouble(gatewaySide.getProbabilityValue());
                    DecimalFormat df = new DecimalFormat("0%");
                    String percentage = df.format(number);
                    writer.println("    <ModelElementEdge id=\"" + "11" + activityGateway.getId() + "\">");
                    writer.println("      <ModelElementName>Rate " + (int) (Double.parseDouble(gatewaySide.getProbabilityValue()) * 10) + " (" + percentage + ")</ModelElementName>");
                    writer.println("      <ModelElementConnection Element1=\"" + "2" + gateway.getGatewayId() + "\" Element2=\"" + "1" + activity.getActivityId() + "\" Type=\"Edge\"/>");
                    writer.println("    </ModelElementEdge>");
                } else {
                    Set<String> mapKeys = map.keySet();
                    int num = 0;
                    for (String mapKey : mapKeys) {
                        if (("2" + activityGateway.getGatewayId()).equals(mapKey)) {
                            num = 1;
                        }
                    }
                    if (num == 0) {
                        // 否则 说明是活动指向网关，不需要考虑概率值
                        writer.println("    <ModelElementEdge id=\"" + "11" + activityGateway.getId() + "\">");
                        writer.println("      <ModelElementConnection Element1=\"" + "1" + activity.getActivityId() + "\" Element2=\"" + "2" + gateway.getGatewayId() + "\" Type=\"Edge\"/>");
                        writer.println("    </ModelElementEdge>");
                    }
                }
            }

            //设置终点（Dispose）
            writer.println("   <ModelElementDispose id=\"" + "13888"  + "\">");
            writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"930\" y=\"215\"/>");
            for (String zd : zdList) {
                writer.println("      <ModelElementConnection Element=\"" + zd + "\" Type=\"In\"/>");
            }
            writer.println("   </ModelElementDispose>");

            // 仿真整体平均处理时间
            writer.println("    <ModelElementAnimationText id=\"1\">");
            writer.println("      <ModelElementName>仿真整体平均处理时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1160\" y=\"170\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Time value\">ProcessTime_avg()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体平均等待时间
            writer.println("    <ModelElementAnimationText id=\"2\">");
            writer.println("      <ModelElementName>仿真整体平均等待时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1310\" y=\"175\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Time value\">WaitingTime_avg()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体平均完成时间
            writer.println("    <ModelElementAnimationText id=\"3\">");
            writer.println("      <ModelElementName>仿真整体平均完成时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1460\" y=\"175\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">ProcessTime_avg()+WaitingTime_avg()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最大处理时间
            writer.println("    <ModelElementAnimationText id=\"4\">");
            writer.println("      <ModelElementName>仿真整体最大处理时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1160\" y=\"280\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">ProcessTime_max()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最大等待时间
            writer.println("    <ModelElementAnimationText id=\"5\">");
            writer.println("      <ModelElementName>仿真整体最大等待时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1305\" y=\"280\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_max()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最大完成时间
            writer.println("    <ModelElementAnimationText id=\"6\">");
            writer.println("      <ModelElementName>仿真整体最大完成时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1460\" y=\"275\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_max()+ProcessTime_max()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最小处理时间
            writer.println("    <ModelElementAnimationText id=\"7\">");
            writer.println("      <ModelElementName>仿真整体最小处理时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1165\" y=\"385\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">ProcessTime_min()</ModelElementAnimationMode>");
            writer.println("      <ModelElementBackgroundColor>255,255,255</ModelElementBackgroundColor>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最小等待时间
            writer.println("    <ModelElementAnimationText id=\"8\">");
            writer.println("      <ModelElementName>仿真整体最小等待时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1305\" y=\"385\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_min()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");
            // 仿真整体最小完成时间
            writer.println("    <ModelElementAnimationText id=\"9\">");
            writer.println("      <ModelElementName>仿真整体最小完成时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1455\" y=\"390\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_min()+ProcessTime_min()</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");

            //将开始仿真时间转时间搓
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(startEmulationTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            long timestamp = date.getTime(); // 将毫秒转换为秒

            // 当前仿真时间
            writer.println("    <ModelElementAnimationText id=\"10\">");
            writer.println("      <ModelElementName>当前仿真时间</ModelElementName>");
            writer.println("      <ModelElementSize h=\"30\" w=\"53\" x=\"195\" y=\"420\"/>");
            writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
            writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
            writer.println("      <ModelElementAnimationMode Type=\"Date\">" + timestamp + "</ModelElementAnimationMode>");
            writer.println("    </ModelElementAnimationText>");

            writer.println("  </ModelElements>");
            // 编写活动执行人定义XML
            writer.println("     <Resources SecondaryPriority=\"Random\">");
            for (Activity activity : activityList) {
                writer.println("      <Resource Name=\"" + "1" + activity.getActivityId() + "\" Type=\"Number\" Value=\"1\"/>");
            }
            writer.println("    </Resources>");
            // 输出根元素的结尾标记
            writer.println("</Model>");
            // 关闭 PrintWriter 对象
            writer.close();
            System.out.println("XML文件已生成：" + filePath);
            return filePath;
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println("生成XML失败...");
            return "error";
        }
    }

    private static void extracted1(PrintWriter writer, Activity activity) {
        // 设置该活动平均处理时间
        writer.println("    <ModelElementAnimationText id=\"" + "3" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "平均处理时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"76\" x=\"431\" y=\"623\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Time value\">ProcessTime_avg(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动平均等待时间
        writer.println("    <ModelElementAnimationText id=\"" + "4" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "平均等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"405\" y=\"415\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_avg(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动当前等待用户数
        writer.println("    <ModelElementAnimationText id=\"" + "5" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "当前用户数</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"92\" x=\"410\" y=\"475\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Number\">WIP(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动最大等待时间
        writer.println("    <ModelElementAnimationText id=\"" + "6" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "最大等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"415\" y=\"610\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_max(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动最小等待时间
        writer.println("    <ModelElementAnimationText id=\"" + "7" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "最小等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"415\" y=\"680\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Time value\">WaitingTime_min(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动最大等待数量
        writer.println("    <ModelElementAnimationText id=\"" + "8" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "最大等待数量</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"410\" y=\"755\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WIP_max(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动执行次数
        writer.println("    <ModelElementAnimationText id=\"" + "9" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "执行次数</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"50\" x=\"410\" y=\"840\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">NumberOut(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
    }

    private static void extracted(PrintWriter writer, Activity activity) {
        // 活动处理时间
        Integer disposeTime = activity.getDisposeTime(); //disposeTime: 处理时间
        if (activity.getDisposeTimeType() == 0) {
            //秒
            writer.println("      <ModelElementExpression  Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\"> " + disposeTime + "</ModelElementExpression>");
        } else if (activity.getDisposeTimeType() == 1) {
            //分
            writer.println("      <ModelElementExpression  Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">" + disposeTime * 60 + "</ModelElementExpression>");
        } else if (activity.getDisposeTimeType() == 2) {
            //时
            writer.println("      <ModelElementExpression  Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">" + disposeTime * 60 * 60 + "</ModelElementExpression>");
        } else if (activity.getDisposeTimeType() == 3) {
            //天
            writer.println("      <ModelElementExpression  Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">" + disposeTime * 60 * 60 * 24 + "</ModelElementExpression>");
        }

        // 活动等待时间
        Integer awaitTime = activity.getAwaitTime();//awaitTimeType: 等待时间
        if (activity.getAwaitTimeType() == 0) {
            //秒
            writer.println("      <ModelElementExpression Type=\"PostProcessingTime\">" + awaitTime + "</ModelElementExpression>");
        } else if (activity.getAwaitTimeType() == 1) {
            //分
            writer.println("      <ModelElementExpression Type=\"PostProcessingTime\">" + awaitTime * 60 + "</ModelElementExpression>");
        } else if (activity.getAwaitTimeType() == 2) {
            //时
            writer.println("      <ModelElementExpression Type=\"PostProcessingTime\">" + awaitTime * 60 * 60 + "</ModelElementExpression>");
        } else if (activity.getAwaitTimeType() == 3) {
            //天
            writer.println("      <ModelElementExpression Type=\"PostProcessingTime\">" + awaitTime * 60 * 60 * 24 + "</ModelElementExpression>");
        }
    }

    //加载XML文件
    @Override
    public String efficacyXML(String url) {
        try {
            // 创建 OkHttpClient 对象
            OkHttpClient client = new OkHttpClient();

            // 创建请求体
            MediaType mediaType = MediaType.parse("multipart/form-data");
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("model", "filename", RequestBody.create(MediaType.parse("application/octet-stream"), new File(url)))
                    .build();

            // 创建请求对象
            Request request = new Request.Builder()
                    .url("http://10.230.3.11:80/upload")
                    .post(body)
                    .addHeader("Content-Type", "multipart/form-data")
                    .addHeader("version", "v2")
                    .build();

            // 发送请求并获取响应
            Response response = client.newCall(request).execute();
            String values = response.body().string();

            if ("WebServer.Upload.Success,model.Check.Ok".equals(values)) {
                System.out.println("XML加载成功...");
                return values;
            } else {
                System.out.println("XML文件加载失败(" + values + ")");
                String result = values.replace("WebServer.Upload.Success,model.Check.Error!", "");
                return result;
            }
        } catch (Exception e) {
            System.out.println("XML文件加载失败...");
            e.printStackTrace();
            return "WebServer.Upload.Success,model.Check.Error!";
        }
    }

    //获取引擎执行中返回的状态信息(返回值当中出现了乱码,所以用 OkHttpClient 远程调用)
    @Override
    public MyResponseDto fetchEngineState() {
        OkHttpClient client = new OkHttpClient();
        Headers headers = new Headers.Builder()
                .add("version","v2")
                .build();
        Request request = new Request.Builder()
                .url("http://10.230.3.11:80/animation?command=status")
                .addHeader("Accept-Charset", "UTF-8")
                .headers(headers)
                .build();
        MyResponseDto myResponseDto = null;
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            myResponseDto = new ObjectMapper().readValue(responseBody, MyResponseDto.class);
            //源value数据
            Map<String, Object> oldmap = myResponseDto.getValue();
            //新value数据
            Map<String, Object> newMap = new HashMap<>();
            //获取所有key
            Set<String> keys = oldmap.keySet();
            //存储活动编号
            List<String> list = new ArrayList();
            for (String key : keys) {
                //将key为1-10的数据，保留
                int num = Integer.parseInt(key);
                if (num >= 1 && num <= 10) {
                    newMap.put(key, oldmap.get(key));
                }else {
                    //获取到活动编号
                    String activityId = "1" + key.substring(1);
                    //如果list集合当中不存在这个活动编号,就存储记录
                    if (!list.contains(activityId)) {
                        list.add(activityId);
                    }
                }
            }
            for (String str : list) {
                ResponseDto responseDto = new ResponseDto();
                for (String key : keys) {
                    //获取到活动编号
                    String activityId = "1" + key.substring(1);
                    //如果集合当中该条活动编号 与 该循环中获取到的活动编号一致，说明是一个活动，给设置具体参数
                    if (activityId.equals(str)) {
                        //第一个字符
                        char at = key.charAt(0);
                        //char 转 int
                        switch (Character.getNumericValue(at)) {
                            case 3:
                                responseDto.setDisposeTimeAvg((String) oldmap.get(key));
                                break;
                            case 4:
                                responseDto.setAwaitTimeAvg((String) oldmap.get(key));
                                break;
                            case 5:
                                responseDto.setUserNum((String) oldmap.get(key));
                                break;
                            case 6:
                                responseDto.setAwaitTimeMax((String) oldmap.get(key));
                                break;
                            case 7:
                                responseDto.setAwaitTimeMin((String) oldmap.get(key));
                                break;
                            case 8:
                                responseDto.setAwaitTimeMaxNum((String) oldmap.get(key));
                                break;
                            case 9:
                                responseDto.setExecuteNum((String) oldmap.get(key));
                                break;
                        }
                    }
                }
                Activity activity = activityService.getBaseMapper().selectById(str.substring(1));
                responseDto.setActivityName(activity.getName());
                newMap.put(str, responseDto);
            }
            myResponseDto.setValue(newMap);
        } catch (IOException e) {
            System.out.println("获取引擎执行中返回的状态信息失败...");
        }
        return myResponseDto;
    }

    //第一次开启仿真
    @Override
    @Transactional
    public boolean startSimulation(Integer historyProcessId, String startEmulationTime, String endEmulationTime,String userName) {
        //1、重启仿真引擎
        Boolean bool = this.rebootEmulationEngine();
        if (bool) {
            System.out.println("重启仿真引擎成功！");
            //2、生成XML文件 返回文件存储地址
            String url = this.generateXML(historyProcessId, startEmulationTime,endEmulationTime);
            // 保存该用户的xml路径(先删除，在新增，保证唯一)
            List<XmlUrl> xmlUrls = xmlUrlService.getBaseMapper().selectList(new LambdaQueryWrapper<XmlUrl>().eq(XmlUrl::getUserName, userName));
            if (xmlUrls.size() > 0){
                List<Integer> collect = xmlUrls.stream()
                        .map(XmlUrl::getId)
                        .collect(Collectors.toList());
                xmlUrlService.getBaseMapper().deleteBatchIds(collect);
            }
            xmlUrlService.getBaseMapper().insert(new XmlUrl(null, userName, url));
            if (!"error".equals(url)){
                //3、加载XML文件
                String flag = this.efficacyXML(url);
                if ("WebServer.Upload.Success,model.Check.Ok".equals(flag)){
                    //4、开始仿真(仿真之前判断XML是否加载成功，引擎是否正常)
                    String state = this.getEngineState();
                    if ("Model editor".equals(state)) {
                        this.sustainEmulation(50);
                        return true;
                    }
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
        return false;
    }

    //持续仿真
    @Override
    public MyResponseDto sustainEmulation(Integer velocityValue) {
        try {
            String url = "http://10.230.3.11:80/animation?command=step&interupt=" + velocityValue;

            //创建HttpHeaders对象，并设置请求头信息
            HttpHeaders headers = new HttpHeaders();
            headers.set("version","v2");

            //创建HttpEntity对象，将headers设置到请求实体中
            HttpEntity<String> entity = new HttpEntity<>(headers);

            //定义url地址，发送post请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            //获取仿真状态
            MyResponseDto myResponseDto = this.fetchEngineState();
            return myResponseDto;
        }catch (Exception e){
            return null;
        }
    }

    //中止仿真
    @Override
    @Transactional
    public boolean discontinueEmulation(String userName,String historyProcessId) {
        try {
            boolean user = userService.userFZ(userName);
            if (user){
                System.out.println("进入");
                //1、查询结果表中是否存在该流程的仿真结果了，如果存在先删除在存储一份，保证一个流程对应一个结果。
                List<ActivityOutcome> activityOutcomeList = activityOutcomeService.getBaseMapper().selectList(new QueryWrapper<ActivityOutcome>().eq("user_name", userName)
                        .eq("history_process_id", historyProcessId));
                List<EmulationTimeOutcome> emulationTimeOutcomeList = emulationTimeOutcomeService.getBaseMapper().selectList(new QueryWrapper<EmulationTimeOutcome>().eq("user_name", userName)
                        .eq("history_process_id", historyProcessId));
                if (activityOutcomeList.size() > 0 && emulationTimeOutcomeList.size() > 0){
                    //2、删除活动 & 整体流程结果数据
                    // 设置批量删除条件(活动)
                    LambdaQueryWrapper<ActivityOutcome> activityOutcomeLambdaQueryWrapper = Wrappers.lambdaQuery();
                    activityOutcomeLambdaQueryWrapper.eq(ActivityOutcome::getUserName, userName)
                            .eq(ActivityOutcome::getHistoryProcessId, historyProcessId);
                    // 执行删除操作(活动)
                    activityOutcomeService.getBaseMapper().delete(activityOutcomeLambdaQueryWrapper);
                    // 设置批量删除条件(整体流程)
                    LambdaQueryWrapper<EmulationTimeOutcome> emulationTimeOutcomeLambdaQueryWrapper = Wrappers.lambdaQuery();
                    emulationTimeOutcomeLambdaQueryWrapper.eq(EmulationTimeOutcome::getUserName, userName)
                            .eq(EmulationTimeOutcome::getHistoryProcessId, historyProcessId);
                    // 执行删除操作(整体流程)
                    emulationTimeOutcomeService.getBaseMapper().delete(emulationTimeOutcomeLambdaQueryWrapper);
                }

                //获取Value数据
                MyResponseDto myResponse = emulationOutcomeService.fetchEngineState();
//                System.out.println("Value:"+myResponse.getValue());
                Map<String, Object> map = myResponse.getValue();
                //存储整体仿真数据
                EmulationTimeOutcome emulationTimeOutcome = new EmulationTimeOutcome();
                emulationTimeOutcome.setDisposeTimeMax((String) map.get("4"));
                emulationTimeOutcome.setDisposeTimeMin((String) map.get("7"));
                emulationTimeOutcome.setDisposeTimeAvg((String) map.get("1"));
                emulationTimeOutcome.setAwaitTimeMax((String) map.get("5"));
                emulationTimeOutcome.setAwaitTimeMin((String) map.get("8"));
                emulationTimeOutcome.setAwaitTimeAvg((String) map.get("2"));
                emulationTimeOutcome.setFinishTimeMax((String) map.get("6"));
                emulationTimeOutcome.setFinishTimeMin((String) map.get("9"));
                emulationTimeOutcome.setFinishTimeAvg((String) map.get("3"));
                emulationTimeOutcome.setCreateTime(new Date());
                emulationTimeOutcome.setHistoryProcessId(Integer.valueOf(historyProcessId));
                emulationTimeOutcome.setUserName(userName);
                emulationTimeOutcomeService.getBaseMapper().insert(emulationTimeOutcome);
                //存储各个活动数据
                Set<String> keys = map.keySet();
                for (String key : keys) {
                    //将key为1-10的数据，保留
                    int num = Integer.parseInt(key);
                    if (num >= 1 && num <= 10) {
                        continue;
                    }else {
                        ResponseDto responseDto = (ResponseDto) map.get(key);
                        ActivityOutcome activityOutcome = new ActivityOutcome();
                        BeanUtils.copyProperties(responseDto,activityOutcome);
                        activityOutcome.setCreateTime(new Date());
                        activityOutcome.setHistoryProcessId(Integer.valueOf(historyProcessId));
                        activityOutcome.setUserName(userName);
                        activityOutcomeService.getBaseMapper().insert(activityOutcome);
                    }
                }

                System.out.println("准备执行终止仿真接口");
                //删除当前仿真用户
                userService.removeUser(userName);
                System.out.println(userName + "删除成功(仿真被终止)...");
                //终止仿真
                String url = "http://10.230.3.11:80/animation?command=quit";

                //创建HttpHeaders对象，并设置请求头信息
                HttpHeaders headers = new HttpHeaders();
                headers.set("version","v2");

                //创建HttpEntity对象，将headers设置到请求实体中
                HttpEntity<String> entity = new HttpEntity<>(headers);

                //定义url地址，发送post请求
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                System.out.println("执行终止仿真接口完毕");
            }
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return true;
        }
    }

    //重启仿真引擎
    @Override
    public Boolean rebootEmulationEngine() {
       try {
           String url = "http://10.230.3.11:80/open-pse-file";

           //创建HttpHeaders对象，并设置请求头信息
           HttpHeaders headers = new HttpHeaders();
           headers.set("version","v1");

           //创建HttpEntity对象，将headers设置到请求实体中
           HttpEntity<String> entity = new HttpEntity<>(headers);

           ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
           String flag = response.getBody();
           if (!flag.isEmpty() && "start".equals(flag)) {
               //重启成功
               return true;
           }
       }catch (Exception e){
           System.out.println("重启仿真引擎失败");
           return false;
       }
       return false;
    }

    //获取引擎状态
    @Override
    public String getEngineState() {
        try {
            String url = "http://10.230.3.11:80/status";

            //创建HttpHeaders对象，并设置请求头信息
            HttpHeaders headers = new HttpHeaders();
            headers.set("version","v2");

            //创建HttpEntity对象，将headers设置到请求实体中
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            String mode = (String) jsonObject.get("mode");
            System.out.println("引擎状态为：" + mode);
            return mode;
        }catch (Exception e){
            System.out.println("获取引擎状态失败...");
            return "error";
        }
    }
}
