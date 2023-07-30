package com.it;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class XmlGenerator {

    public static void main(String[] args) throws IOException {
        // 定义文件名和路径
        String fileName = "queueing_model.xml";
        String filePath = "./" + fileName;

        // 创建 PrintWriter 对象，用于输出 XML 内容到文件
        PrintWriter writer = new PrintWriter(new FileWriter(filePath));

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

        // 输出模型元素
        writer.println("  <ModelElements>");
        writer.println("    <ModelElementSource id=\""+ UUID.randomUUID() + "\">");
        writer.println("      <ModelElementName>Clients</ModelElementName>");
        writer.println("      <ModelElementSize h=\"50\" w=\"100\" x=\"0\" y=\"60\"/>");
        writer.println("      <ModelElementConnection Element=\"5\" Type=\"Out\"/>");
        writer.println("      <ModelElementExpression FirstArrivalAtStart=\"1\" TimeBase=\"Seconds\">10</ModelElementExpression>");
        writer.println("      <ModelElementBatchData Size=\"1;1;1;1;1;1;1;1;1;1\"/>");
        writer.println("    </ModelElementSource>");
        writer.println("  </ModelElements>");

        // 输出根元素的结尾标记
        writer.println("</Model>");

        // 关闭 PrintWriter 对象
        writer.close();

        System.out.println("XML 文件已生成：" + filePath);
    }

}