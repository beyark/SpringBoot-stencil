package com.it.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import com.alibaba.fastjson.JSON;
import com.it.domain.User;
import com.it.dto.MyResponseDto;
import com.it.service.impl.EmulationOutcomeServiceImpl;
import com.it.service.impl.UserServiceImpl;
import com.it.util.DateUtil;
import com.it.util.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Slf4j
@ServerEndpoint("/websocket/{userName}") //是你连接时的url，如果后端为127.0.0.1:8080/websocket/张三，那么前端websocket连接url写
@Component  // 此注解千万千万不要忘记，它的主要作用就是将这个监听器纳入到Spring容器中进行管理
public class WebSocketServer {

    //用来记录当前在线连接数。应该把它设计成线程安全的
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //持续仿真的参数
    private Integer velocityValue = 50;
    //用来执行持续仿真的线程
    private Thread simulationThread;
    //用户名称
    private String userName;

    /**
     * 连接成功时调用的方法(可以接收一个参数噢)
     */
    @OnOpen
    public void onOpen(@PathParam("userName") String userName,Session session) {
        this.userName =userName;
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("连接建立成功:当前在线人数为(" + getOnlineCount() + "人)");
    }

    /**
     * 收到客户端消息后处理逻辑
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端的消息:" + message);
        if (message.startsWith("updateVelocityValue:")) {
            int newVelocityValue = Integer.parseInt(message.substring("updateVelocityValue:".length()));
            this.velocityValue = newVelocityValue;
        } else {
            startSimulation();
        }
    }

    /**
     *  执行持续仿真的线程
     */
    private void startSimulation() {
        stopSimulation(); // 先停止已有的仿真线程
        simulationThread = new Thread(() -> {
            while (simulationThread != null) {
                try {
                    ApplicationContext context = SpringContextUtils.getApplicationContext();
                    MyResponseDto myResponseDto =null;
                    if (context == null) {
                        System.out.println("ApplicationContext is null");
                    } else {
                        //获取流程仿真的执行状态信息
                        EmulationOutcomeServiceImpl emulationOutcomeService = context.getBean(EmulationOutcomeServiceImpl.class);
                        if (emulationOutcomeService == null) {
                            System.out.println("emulationOutcomeService is null");
                        } else {
                            myResponseDto = emulationOutcomeService.sustainEmulation(velocityValue);
                        }
                        //根据用户名称获取用户信息(设置当前仿真时间与百分比进度)
                        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
                        if (userService == null) {
                            System.out.println("userService is null");
                        } else {
                            User user = userService.getUserByUserName(userName);
                            if (user != null){
                                //引擎返回的时间
                                long time = Long.parseLong(myResponseDto.getTime());
                                //获取用户输入的开始时间
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date startDate = null;
                                try {
                                    startDate = sdf.parse(DateUtil.forString(user.getStartEmulationTime(), "yyyy-MM-dd HH:mm:ss"));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                long startTime = startDate.getTime() / 1000; // 将毫秒转换为秒
                                //将合并的当前时间挫转为日期字符串，并赋值给myResponseDto对象，返回前端
                                Date date = new Date(time+startTime);
                                String formattedDate = sdf.format(date);
                                myResponseDto.setTime(formattedDate);
                                //设置百分比进度算法
                                Date startDateTime = user.getStartEmulationTime();
                                Date endDateTime = user.getEndEmulationTime();
                                Date currentDateTime = sdf.parse(formattedDate);
                                long totalTime = endDateTime.getTime() - startDateTime.getTime();
                                long elapsedTime = currentDateTime.getTime() - startDateTime.getTime();
                                double progress = (double) elapsedTime / totalTime;
                                myResponseDto.setProgress((int) (progress * 100) + "%");
                            }
                        }
                    }
                    String jsonStr = JSON.toJSONString(myResponseDto);
                    sendInfo(jsonStr);
                    Thread.sleep(velocityValue);
                } catch (Exception e) {
                    log.info("连接断开");
                    throw new RuntimeException(e);
                }
            }
        });
        //开启线程
        simulationThread.start();
    }

    //关闭持续仿真的线程(给线程打个中断标记)
    private void stopSimulation() {
        System.out.println("---");
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
            try {
                simulationThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            simulationThread = null;
        }
    }


    /**
     *  给前端发送消息。
     */
    public static void sendInfo(String message) throws IOException {
        log.info(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }
    //发送消息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     *  关闭连接时调用
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        this.stopSimulation();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     *  发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}