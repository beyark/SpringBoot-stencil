package com.it;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXMLFileWithNamespace {

    public static void main(String argv[]) {

        try {

            // 创建DocumentBuilderFactory实例
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

            // 创建DocumentBuilder实例
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // 创建Document对象
            Document doc = docBuilder.newDocument();

            // 创建根元素
            Element rootElement = doc.createElementNS("https://a-herzog.github.io", "Model");
            rootElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "https://a-herzog.github.io https://a-herzog.github.io/Warteschlangensimulator/Simulator.xsd");
            doc.appendChild(rootElement);



            // 创建参数元素
            Element parameter = doc.createElementNS("https://a-herzog.github.io", "parameter");
            parameter.setAttribute("name", "arrivalRate");
            parameter.setAttribute("value", "10");
            rootElement.appendChild(parameter);

            // 创建TransformerFactory实例
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // 创建Transformer实例
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

            // 创建DOMSource实例
            DOMSource source = new DOMSource(doc);

            // 创建StreamResult实例
            StreamResult result = new StreamResult(new File("D:/桌面/商飞项目/流程仿真项目-启动/xml/data.xml"));

            // 将XML文件输出到文件
            transformer.transform(source, result);

            System.out.println("XML文件创建成功！");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}