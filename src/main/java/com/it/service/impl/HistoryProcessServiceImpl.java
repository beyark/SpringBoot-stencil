package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.domain.*;
import com.it.dto.ResultDto2;
import com.it.dto.SelectHistoryProcessDto;
import com.it.mapper.HistoryProcessMapper;
import com.it.service.*;
import com.it.util.PageUtils;
import com.it.util.Query;
import com.it.vo.GatewayVo;
import com.it.vo.ProcessInquiryVo;
import com.it.vo.SaveHistoricalProcessesVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @description 针对表【sys_history_process】的数据库操作Service实现
 * @createDate 2023-07-20 16:11:40
 */
@Service
public class HistoryProcessServiceImpl extends ServiceImpl<HistoryProcessMapper, HistoryProcess> implements HistoryProcessService {
    @Resource
    private ProcessInquiryService processInquiryService;
    @Resource
    private HistoryProcessService historyProcessService;
    @Resource
    private ActivityService activityService;
    @Resource
    private GatewayService gatewayService;
    @Resource
    private ActivityActivityService activityActivityService;
    @Resource
    private ActivityGatewayService activityGatewayService;
    @Resource
    private GatewaySideService gatewaySideService;
    @Resource
    private HistoryProcessMapper historyProcessMapper;

    //流程效验接口
    @Override
    public String processValidation(String historyProcessId, String nodeId, String type) {
        //如果是活动 activity
        if ("activity".equals(type)) {
            Activity activity = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId)
                    .eq("id", nodeId));
            if (activity.getParameterValidation() == 0) {
                return "没有设置参数";
            }
        }
        //如果是网关 gateway
        else if ("gateway".equals(type)) {
            Gateway gateway = gatewayService.getBaseMapper().selectOne(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId)
                    .eq("id", nodeId));
            if (gateway.getParameterValidation() == 0) {
                return "没有配置参数";
            }
        }
        return "已设置参数";
    }

    //保存&另存为历史流程接口
    @Override
    @Transactional
    public String saveHistoricalProcesses(SaveHistoricalProcessesVO saveHistoricalProcessesVO) {
        //获取到历史流程ID
        int historyProcessId = saveHistoricalProcessesVO.getHistoryProcess().getHistoryProcessId();
        HistoryProcess historyProcessDomain = null;
        //判断 按钮 是 保存还是另存为
        if ("save".equals(saveHistoricalProcessesVO.getSaveType())) {
            //说明是保存按钮
            //1、进来首先进行整个图 删除
            //删除活动
            activityService.getBaseMapper().delete(new QueryWrapper<Activity>().in("history_process_id", historyProcessId));
            //删除活动与活动关系
            activityActivityService.getBaseMapper().delete(new QueryWrapper<ActivityActivity>().in("history_process_id", historyProcessId));
            //删除网关参数
            gatewaySideService.getBaseMapper().delete(new QueryWrapper<GatewaySide>().in("history_process_id", historyProcessId));
            //删除网关
            gatewayService.getBaseMapper().delete(new QueryWrapper<Gateway>().in("history_process_id", historyProcessId));
            //删除活动与网关关系
            activityGatewayService.getBaseMapper().delete(new QueryWrapper<ActivityGateway>().in("history_process_id", historyProcessId));
        }
        ResultDto2 resultDto = null;
        try {
            //说明是另存为按钮
            //2、调用接口，拿到总体数据
            ProcessInquiryVo processInquiryVo = new ProcessInquiryVo();
            processInquiryVo.setAuthorization(saveHistoricalProcessesVO.getAuthorization());
            processInquiryVo.setEnvironment(saveHistoricalProcessesVO.getEnvironment());
            processInquiryVo.setLicheng1_id(saveHistoricalProcessesVO.getHistoryProcess().getId());
            resultDto = processInquiryService.selectProcessDetails(processInquiryVo);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "调用查询流程接口嘎了...";
        }
        if (resultDto != null) {
            //3、获取数据
            List<ResultDto2.Node> nodes = resultDto.getNodes();
            List<ResultDto2.Edge> edges = resultDto.getEdges();
            System.out.println("源数据节点个数：" + nodes.size());
            System.out.println(nodes);
            System.out.println("源数据边个数：" + edges.size());
            System.out.println(edges);

            //4.过滤数据
            List<ResultDto2.Node> nodeList = new ArrayList();
            List<ResultDto2.Edge> edgeList = new ArrayList();
            for (ResultDto2.Node node : nodes) {
                String label = node.getGraphProperties().getLabel();
                if ("huodong1".equals(label) || "gateway".equals(label)) {
                    nodeList.add(node);
                }
            }
            for (ResultDto2.Edge edge : edges) {
                String label = edge.getGraphProperties().getLabel();
                if ("gateway_huodong1_in".equals(label) || "huodong1_gateway_out".equals(label) || "huodong1_huodong1_order".equals(label)) {
                    edgeList.add(edge);
                }
            }

            System.out.println("过滤后节点个数：" + nodeList.size());
            System.out.println(nodeList);
            System.out.println("过滤后边个数：" + edgeList.size());
            System.out.println(edgeList);

            //5、存历史流程
            HistoryProcess historyProcess = historyProcessService.getBaseMapper().selectById(historyProcessId);
            System.out.println("用户名称:" + saveHistoricalProcessesVO.getUserName());
            if (historyProcess == null) {
                historyProcessDomain = new HistoryProcess();
                historyProcessDomain.setId(saveHistoricalProcessesVO.getHistoryProcess().getId());
                historyProcessDomain.setName(saveHistoricalProcessesVO.getHistoryProcess().getName());
                historyProcessDomain.setDescription(saveHistoricalProcessesVO.getHistoryProcess().getDescription());
                historyProcessDomain.setStartEmulationTime(saveHistoricalProcessesVO.getHistoryProcess().getStartEmulationTime());
                historyProcessDomain.setEndEmulationTime(saveHistoricalProcessesVO.getHistoryProcess().getEndEmulationTime());
                historyProcessDomain.setUpdateTime(new Date());
                historyProcessDomain.setCreateTime(new Date());
                historyProcessDomain.setUserName(saveHistoricalProcessesVO.getUserName());
                historyProcessMapper.inserts(historyProcessDomain);
            }
            System.out.println("历史流程:" + historyProcessDomain);

            //6、存节点
            for (ResultDto2.Node node : nodeList) {
                //判断节点是活动、网关
                if ("huodong1".equals(node.getGraphProperties().getLabel())) {
                    //存活动
                    Activity activity = new Activity();
                    activity.setId(node.getGraphProperties().getId() + "");
                    activity.setName(node.getGraphProperties().getHuodongmingcheng());
                    activity.setUpdataTime(new Date());
                    activity.setCreateTime(new Date());
                    if (historyProcess == null) {
                        activity.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                    } else {
                        activity.setHistoryProcessId(historyProcess.getHistoryProcessId());
                    }
                    activityService.getBaseMapper().insert(activity);
                } else if ("gateway".equals(node.getGraphProperties().getLabel())) {
                    //存网关
                    Gateway gateway = new Gateway();
                    gateway.setId(node.getGraphProperties().getId() + "");
                    //网关名称
                    gateway.setName(node.getGraphProperties().getMingcheng());
                    gateway.setUpdateTime(new Date());
                    gateway.setCreateTime(new Date());
                    if (historyProcess == null) {
                        gateway.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                    } else {
                        gateway.setHistoryProcessId(historyProcess.getHistoryProcessId());
                    }
                    gatewayService.getBaseMapper().insert(gateway);
                }
            }

            //7、存关系
            for (ResultDto2.Edge edge : edgeList) {
                //源节点ID
                String sourceNodeId = edge.getSourceNodeId();
                //目标节点ID
                String targetNodeId = edge.getTargetNodeId();

                //获取到当前的边的label
                String label = edge.getGraphProperties().getLabel();
                String[] str = label.split("_");

                // str数据 第一个值为 "gateway" 说明是 网关指向活动
                if ("gateway".equals(str[0])) {
                    ActivityGateway activityGateway = new ActivityGateway();
                    activityGateway.setGatewayId(sourceNodeId);
                    activityGateway.setActivityId(targetNodeId);
                    activityGateway.setValue(1);
                    activityGateway.setSideId(edge.getId());
                    if (historyProcess == null) {
                        activityGateway.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                    } else {
                        activityGateway.setHistoryProcessId(historyProcess.getHistoryProcessId());
                    }
                    activityGatewayService.getBaseMapper().insert(activityGateway);
                } else
                    //str数据 第一个值为 "huodong1" 第二个值为 "gateway" 说明是 活动指向网关
                    if ("huodong1".equals(str[0]) && "gateway".equals(str[1])) {
                        ActivityGateway activityGateway = new ActivityGateway();
                        activityGateway.setGatewayId(targetNodeId);
                        activityGateway.setActivityId(sourceNodeId);
                        activityGateway.setSideId(edge.getId());
                        if (historyProcess == null) {
                            activityGateway.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                        } else {
                            activityGateway.setHistoryProcessId(historyProcess.getHistoryProcessId());
                        }
                        activityGatewayService.getBaseMapper().insert(activityGateway);
                    } else
                        // //str数据 第一个值为 "huodong1" 第二个值为 "huodong1" 说明是 活动指向活动
                        if ("huodong1".equals(str[0]) && "huodong1".equals(str[1])) {
                            ActivityActivity activityActivity = new ActivityActivity();
                            activityActivity.setActivityOne(sourceNodeId);
                            activityActivity.setActivityTwo(targetNodeId);
                            if (historyProcess == null) {
                                activityActivity.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                            } else {
                                activityActivity.setHistoryProcessId(historyProcess.getHistoryProcessId());
                            }
                            activityActivityService.getBaseMapper().insert(activityActivity);
                        }
            }

            //8、存储活动 和 网关参数
            //存活动参数
            List<Activity> activityList = saveHistoricalProcessesVO.getActivity();
            if (activityList != null) {
                for (Activity activity : activityList) {
                    //1、先根据活动ID查询活动信息
                    Activity activityMessage = null;
                    if ("saveAs".equals(saveHistoricalProcessesVO.getSaveType())) {
                        activityMessage = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>()
                                .eq("id", activity.getId())
                                .eq("history_process_id", historyProcessDomain.getHistoryProcessId()));
                    } else if ("saveAs".equals(saveHistoricalProcessesVO.getSaveType()) && historyProcessId != 0) {
                        activityMessage = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>()
                                .eq("id", activity.getId())
                                .eq("history_process_id", historyProcessId));
                    } else {
                        activityMessage = activityService.getBaseMapper().selectOne(new QueryWrapper<Activity>()
                                .eq("id", activity.getId()));
                    }
                    activityMessage.setUpdataTime(new Date());
                    // 设置参数效验为 1
                    activityMessage.setParameterValidation(1);
                    if (activity.getName() != null) {
                        activityMessage.setName(activity.getName());
                    }
                    // 2、判空设值
                    if (activity.getType() != null) {
                        activityMessage.setType(activity.getType());
                    }
                    if (activity.getType() == 0 || activity.getType() == 2) {
                        // 说明 是中间活动 或者 结束活动
                        if (activity.getDisposeTime() != null) {
                            activityMessage.setDisposeTime(activity.getDisposeTime());
                        }
                        if (activity.getDisposeTimeType() != null) {
                            activityMessage.setDisposeTimeType(activity.getDisposeTimeType());
                        }
                        if (activity.getAwaitTime() != null) {
                            activityMessage.setAwaitTime(activity.getAwaitTime());
                        }
                        if (activity.getAwaitTimeType() != null) {
                            activityMessage.setAwaitTimeType(activity.getAwaitTimeType());
                        }
                    } else if (activity.getType() == 1) {
                        // 说明 是开始活动
                        if (activity.getDisposeTime() != null) {
                            activityMessage.setDisposeTime(activity.getDisposeTime());
                        }
                        if (activity.getDisposeTimeType() != null) {
                            activityMessage.setDisposeTimeType(activity.getDisposeTimeType());
                        }
                        if (activity.getAwaitTime() != null) {
                            activityMessage.setAwaitTime(activity.getAwaitTime());
                        }
                        if (activity.getAwaitTimeType() != null) {
                            activityMessage.setAwaitTimeType(activity.getAwaitTimeType());
                        }
                        if (activity.getAwaitTime() != null) {
                            activityMessage.setAwaitTime(activity.getAwaitTime());
                        }
                        if (activity.getAwaitTimeType() != null) {
                            activityMessage.setAwaitTimeType(activity.getAwaitTimeType());
                        }
                        if (activity.getIntervals() != null) {
                            activityMessage.setIntervals(activity.getIntervals());
                        }
                        if (activity.getExecutionFrequency() != null) {
                            activityMessage.setExecutionFrequency(activity.getExecutionFrequency());
                        }
                        if (activity.getTimeType() != null) {
                            activityMessage.setTimeType(activity.getTimeType());
                        }
                        if (activity.getEventType() != null) {
                            activityMessage.setEventType(activity.getEventType());
                        }
                        if (activity.getEventType() != null && activity.getEventType() == 1) {
                            activityMessage.setRandom1(activity.getRandom1());
                            activityMessage.setRandom2(activity.getRandom2());
                        }
                        if (activity.getDistinctValue() != null) {
                            activityMessage.setDistinctValue(activity.getDistinctValue());
                        }
                    }
                    //3、更新
                    activityService.getBaseMapper().updateById(activityMessage);
                }
            }

            //存网关参数
            List<GatewayVo> gatewayList = saveHistoricalProcessesVO.getGateway();
            if (gatewayList != null) {
                for (GatewayVo gateway : gatewayList) {
                    List<GatewayVo.Data> dataList = gateway.getDataList();
                    for (GatewayVo.Data data : dataList) {
                        GatewaySide gatewaySide = new GatewaySide();
                        gatewaySide.setGatewayId(gateway.getId());
                        gatewaySide.setSideId(data.getId());
                        gatewaySide.setProbabilityValue(data.getProbabilityValue());
                        if (historyProcess == null) {
                            gatewaySide.setHistoryProcessId(historyProcessDomain.getHistoryProcessId());
                        } else {
                            gatewaySide.setHistoryProcessId(historyProcess.getHistoryProcessId());
                        }
                        //新增
                        gatewaySideService.getBaseMapper().insert(gatewaySide);
                        //修改 网关的参数效验为 1
//                            Gateway gatewayValidation = null;
                        if (historyProcessId == 0) {
                            List<Gateway> gateways = gatewayService.getBaseMapper().selectList(new QueryWrapper<Gateway>()
                                    .eq("id", gateway.getId()));
                            for (Gateway gatewayValidation : gateways) {
                                gatewayValidation.setParameterValidation(1);
                                gatewayService.getBaseMapper().updateById(gatewayValidation);
                            }
                        } else {
                            Gateway gatewayValidation = gatewayService.getBaseMapper().selectOne(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId)
                                    .eq("id", gateway.getId()));
                            gatewayValidation.setParameterValidation(1);
                            gatewayService.getBaseMapper().updateById(gatewayValidation);
                        }

                    }
                }
            }
        }
        return historyProcessDomain != null && historyProcessDomain.getHistoryProcessId() + "" != null
                ? historyProcessDomain.getHistoryProcessId() + ""
                : "该请求为更新参数";
    }

    //查询历史流程列表(分页)
    @Override
    public PageUtils selectHistoryProcessList(Map<String, Object> map) {
        QueryWrapper<HistoryProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", map.get("userName"));
        IPage<HistoryProcess> page = this.page(new Query<HistoryProcess>().getPage(map), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    //查询历史流程
    @Override
    public SelectHistoryProcessDto selectHistoryProcess(Integer historyProcessId) {
        SelectHistoryProcessDto selectHistoryProcessDto = new SelectHistoryProcessDto();
        //1、查询历史记录信息
        HistoryProcess historyProcess = historyProcessService.getBaseMapper().selectById(historyProcessId);
        selectHistoryProcessDto.setHistoryProcess(historyProcess);
        //2、查询该历史流程所有活动信息
        List<Activity> activityList = activityService.getBaseMapper().selectList(new QueryWrapper<Activity>().eq("history_process_id", historyProcessId));
        selectHistoryProcessDto.setActivityList(activityList);
        //3、查询该历史流程所有网关信息以及该网关下对应的边信息
        List<Gateway> gatewayList = gatewayService.getBaseMapper().selectList(new QueryWrapper<Gateway>().eq("history_process_id", historyProcessId));

        List<GatewayVo> gatewayVoList = new ArrayList();
        for (Gateway gateway : gatewayList) {
            GatewayVo gatewayVo = new GatewayVo();
            BeanUtils.copyProperties(gateway, gatewayVo);

            List<GatewayVo.Data> dataList = new ArrayList<>();
            List<GatewaySide> gatewaySideList = gatewaySideService.getBaseMapper().selectList(new QueryWrapper<GatewaySide>().eq("history_process_id", historyProcessId)
                    .eq("gateway_id", gateway.getId()));

            for (GatewaySide gatewaySide : gatewaySideList) {
                GatewayVo.Data data = new GatewayVo.Data();
                data.setId(gatewaySide.getSideId());
                data.setProbabilityValue(gatewaySide.getProbabilityValue());
                dataList.add(data);
            }
            gatewayVo.setDataList(dataList);
            gatewayVoList.add(gatewayVo);
        }
        selectHistoryProcessDto.setGatewayList(gatewayVoList);
        return selectHistoryProcessDto;
    }
}




