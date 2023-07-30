package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.domain.*;
import com.it.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: SpringBoot-stencil
 * @description: 仿真执行、结果模块
 * @create: 2023-07-25 13:56
 **/
@Service
public class EmulationOutcomeServiceImpl implements EmulationOutcomeService {

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

    //生成XML文件
    @Override
    public void generateXML(Integer historyProcessId, String startEmulationTime) {
        // 定义文件名和路径
        String fileName = "model.xml";
        String filePath = "./" + fileName;

        // 创建 PrintWriter 对象，用于输出 XML 内容到文件
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 输出 XML 头信息和根元素
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        writer.println("<!DOCTYPE Model SYSTEM \"https://a-herzog.github.io/Warteschlangensimulator/Simulator.dtd\">");
        writer.println("<Model xmlns=\"https://a-herzog.github.io\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"https://a-herzog.github.io https://a-herzog.github.io/Warteschlangensimulator/Simulator.xsd\">");

        // 输出模型版本、作者、客户数、热身阶段和终止时间等元素
        writer.println("  <ModelVersion>5.3.0</ModelVersion>");
        writer.println("  <ModelAuthor>msewi</ModelAuthor>");
        writer.println("  <ModelClients Active=\"1\">10000000</ModelClients>");
        writer.println("  <ModelWarmUpPhase>0.01</ModelWarmUpPhase>");
        writer.println("  <ModelTerminationTime Active=\"0\">10:00:00:00</ModelTerminationTime>");
        writer.println("  <ModelElements>");

        Map<String, String> map = new HashMap<>(); // 用于存储分发器
        //编写活动XML
        List<Activity> activityList = activityService.getBaseMapper().selectList(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId));
        for (Activity activity : activityList) {
            if (activity.getType() == 1) {
                // activity.getType() == 1 说明为开始活动
                // 活动id
                writer.println("    <ModelElementSource id=\"" + "1" + activity.getActivityId() + "\">");
                // 活动名称
                writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                // 活动的坐标
                writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                // 活动执行人
                writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");
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
                // 设置活动执行频率
                Integer executionFrequency = activity.getExecutionFrequency(); //executionFrequency: 执行频率
                if (activity.getTimeType() == 0 && activity.getEventType() == 0) {
                    // 秒(固定值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Seconds\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入国定值
                    writer.println("      <ModelElementBatchData Size=\"" + executionFrequency + "\"/>");
                } else if (activity.getTimeType() == 0 && activity.getEventType() == 1) {
                    // 秒(随机值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Seconds\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入随机值
                    String results = "";
                    for (int i = 0; i < executionFrequency; i++) {
                        if (activity.getRandom1() > activity.getRandom2()) {
                            results += "1,";
                        } else {
                            results += "0,";
                        }
                    }
                    // 移除最后一个逗号
                    results = results.substring(0, results.length() - 1);
                    writer.println("      <ModelElementBatchData Size=\"" + results + "\"/>");
                }
                if (activity.getTimeType() == 1 && activity.getEventType() == 0) {
                    // 分(固定值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Minutes\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入国定值
                    writer.println("      <ModelElementBatchData Size=\"" + executionFrequency + "\"/>");
                } else if (activity.getTimeType() == 1 && activity.getEventType() == 1) {
                    // 分(随机值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Minutes\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入随机值
                    String resultm = "";
                    for (int i = 0; i < executionFrequency; i++) {
                        if (activity.getRandom1() > activity.getRandom2()) {
                            resultm += "1,";
                        } else {
                            resultm += "0,";
                        }
                    }
                    // 移除最后一个逗号
                    resultm = resultm.substring(0, resultm.length() - 1);
                    writer.println("      <ModelElementBatchData Size=\"" + resultm + "\"/>");
                }
                if (activity.getTimeType() == 2 && activity.getEventType() == 0) {
                    // 时(固定值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Hours\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入国定值
                    writer.println("      <ModelElementBatchData Size=\"" + executionFrequency + "\"/>");
                } else if (activity.getTimeType() == 2 && activity.getEventType() == 1) {
                    // 时(随机值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Hours\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入随机值
                    String resulth = "";
                    for (int i = 0; i < executionFrequency; i++) {
                        if (activity.getRandom1() > activity.getRandom2()) {
                            resulth += "1,";
                        } else {
                            resulth += "0,";
                        }
                    }
                    // 移除最后一个逗号
                    resulth = resulth.substring(0, resulth.length() - 1);
                    writer.println("      <ModelElementBatchData Size=\"" + resulth + "\"/>");
                }
                if (activity.getTimeType() == 3 && activity.getEventType() == 0) {
                    // 天(固定值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Days\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入国定值
                    writer.println("      <ModelElementBatchData Size=\"" + executionFrequency + "\"/>");
                } else if (activity.getTimeType() == 3 && activity.getEventType() == 1) {
                    // 天(随机值)
                    writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Days\">" + executionFrequency + "</ModelElementExpression>");
                    // 设置活动事件输入随机值
                    String resultd = "";
                    for (int i = 0; i < executionFrequency; i++) {
                        if (activity.getRandom1() > activity.getRandom2()) {
                            resultd += "1,";
                        } else {
                            resultd += "0,";
                        }
                    }
                    // 移除最后一个逗号
                    resultd = resultd.substring(0, resultd.length() - 1);
                    writer.println("      <ModelElementBatchData Size=\"" + resultd + "\"/>");
                }
                writer.println("    </ModelElementSource>");

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
                            int num = 0;
                            for (String key : keys) {
                                if (("2" + activityGateway.getGatewayId()).equals(key)) {
                                    writer.println("      <ModelElementConnection Element=\"" + map.get(key) + "\" Type=\"In\"/>");
                                    num++;
                                }
                            }
                            if (num == 0) {
                                writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"In\"/>");
                            }
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
                // 活动id
                writer.println("    <ModelElementDispose id=\"" + "1" + activity.getActivityId() + "\">");
                // 活动名称
                writer.println("      <ModelElementName>" + activity.getName() + "</ModelElementName>");
                // 活动的坐标
                writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
                // 活动执行人
                writer.println("      <ModelElementOperators Alternative=\"1\" Count=\"1\" Group=\"" + "1" + activity.getActivityId() + "\"/>");
                // 设置中间活动的上和下的节点
                List<ActivityActivity> activityActivityList = activityActivityService.getBaseMapper().selectList(new QueryWrapper<ActivityActivity>().eq("history_process_id", historyProcessId)
                        .eq("activity_two", activity.getId()));
                for (ActivityActivity activityActivity : activityActivityList) {
                    writer.println("      <ModelElementConnection Element=\"" + "11" + activityActivity.getId() + "\" Type=\"In\"/>");
                }
                // 活动处理时间、等待处理时间
                extracted(writer, activity);
                writer.println("    </ModelElementDispose>");

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
                    writer.println("      <ModelElementConnection Element=\"" + "11" + activityGateway.getId() + "\" Type=\"Out\"/>");
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
                writer.println("      <ModelElementName>Rate 1 (" + percentage + ")</ModelElementName>");
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

        // 仿真整体平均处理时间
        writer.println("    <ModelElementAnimationText id=\"1\">");
        writer.println("      <ModelElementName>仿真整体平均处理时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1160\" y=\"170\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Number\">ProcessTime_avg()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体平均等待时间
        writer.println("    <ModelElementAnimationText id=\"2\">");
        writer.println("      <ModelElementName>仿真整体平均等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1310\" y=\"175\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Number\">WaitingTime_avg()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体平均完成时间
        writer.println("    <ModelElementAnimationText id=\"3\">");
        writer.println("      <ModelElementName>仿真整体平均完成时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1460\" y=\"175\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">ProcessTime_avg()+WaitingTime_avg()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最大处理时间
        writer.println("    <ModelElementAnimationText id=\"4\">");
        writer.println("      <ModelElementName>仿真整体最大处理时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1160\" y=\"280\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">ProcessTime_max()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最大等待时间
        writer.println("    <ModelElementAnimationText id=\"5\">");
        writer.println("      <ModelElementName>仿真整体最大等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1305\" y=\"280\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_max()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最大完成时间
        writer.println("    <ModelElementAnimationText id=\"6\">");
        writer.println("      <ModelElementName>仿真整体最大完成时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1460\" y=\"275\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_max()+ProcessTime_max()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最小处理时间
        writer.println("    <ModelElementAnimationText id=\"7\">");
        writer.println("      <ModelElementName>仿真整体最小处理时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1165\" y=\"385\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">ProcessTime_min()</ModelElementAnimationMode>");
        writer.println("      <ModelElementBackgroundColor>255,255,255</ModelElementBackgroundColor>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最小等待时间
        writer.println("    <ModelElementAnimationText id=\"8\">");
        writer.println("      <ModelElementName>仿真整体最小等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1305\" y=\"385\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_min()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 仿真整体最小完成时间
        writer.println("    <ModelElementAnimationText id=\"9\">");
        writer.println("      <ModelElementName>仿真整体最小完成时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"88\" x=\"1455\" y=\"390\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_min()+ProcessTime_min()</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");

        //将开始仿真时间转时间搓
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(startEmulationTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long timestamp = date.getTime();
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
        System.out.println("XML 文件已生成：" + filePath);
    }

    private static void extracted1(PrintWriter writer, Activity activity) {
        // 设置该活动平均处理时间
        writer.println("    <ModelElementAnimationText id=\"" + "3" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "平均处理时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"76\" x=\"431\" y=\"623\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Digits=\"0\" Type=\"Number\">ProcessTime_avg(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
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
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_max(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动最小等待时间
        writer.println("    <ModelElementAnimationText id=\"" + "7" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "最小等待时间</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"415\" y=\"680\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">WaitingTime_min(" + "1" + activity.getActivityId() + ")</ModelElementAnimationMode>");
        writer.println("    </ModelElementAnimationText>");
        // 设置该活动最大等待数量
        writer.println("    <ModelElementAnimationText id=\"" + "8" + activity.getActivityId() + "\">");
        writer.println("      <ModelElementName>" + activity.getName() + "最大等待数量</ModelElementName>");
        writer.println("      <ModelElementSize h=\"30\" w=\"83\" x=\"410\" y=\"755\"/>");
        writer.println("      <ModelElementFontSize>14</ModelElementFontSize>");
        writer.println("      <ModelElementColor>0,0,0</ModelElementColor>");
        writer.println("      <ModelElementAnimationMode Type=\"Number\">Statistics_max(" + "1" + activity.getActivityId() + ";nr)</ModelElementAnimationMode>");
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
            writer.println("      <ModelElementDistribution Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + disposeTime + ")</ModelElementDistribution>");
        } else if (activity.getDisposeTimeType() == 1) {
            //分
            writer.println("      <ModelElementDistribution Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (disposeTime * 60) + ")</ModelElementDistribution>");
        } else if (activity.getDisposeTimeType() == 2) {
            //时
            writer.println("      <ModelElementDistribution Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (disposeTime * 60 * 60) + ")</ModelElementDistribution>");
        } else if (activity.getDisposeTimeType() == 3) {
            //天
            writer.println("      <ModelElementDistribution Status=\"ProcessTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (disposeTime * 60 * 60 * 24) + ")</ModelElementDistribution>");
        }

        // 活动等待时间
        Integer awaitTime = activity.getAwaitTime();//awaitTimeType: 等待时间
        if (activity.getAwaitTimeType() == 0) {
            //秒
            writer.println("      <ModelElementDistribution Status=\"WaitTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + awaitTime + ")</ModelElementDistribution>");
        } else if (activity.getAwaitTimeType() == 1) {
            //分
            writer.println("      <ModelElementDistribution Status=\"WaitTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (awaitTime * 60) + ")</ModelElementDistribution>");
        } else if (activity.getAwaitTimeType() == 2) {
            //时
            writer.println("      <ModelElementDistribution Status=\"WaitTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (awaitTime * 60 * 60) + ")</ModelElementDistribution>");
        } else if (activity.getAwaitTimeType() == 3) {
            //天
            writer.println("      <ModelElementDistribution Status=\"WaitTime\" TimeBase=\"Seconds\" Type=\"ProcessingTime\">Exponential distribution (" + (awaitTime * 60 * 60 * 24) + ")</ModelElementDistribution>");
        }
    }
}
