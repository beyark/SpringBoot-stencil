package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @program: SpringBoot-stencil
 * @description: 流程接收
 * @create: 2023-07-22 10:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto2 {
    private String process;
    private String processId;
    private List<Node> nodes;
    private List<Edge> edges;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private String id;
        private String type;
        private Double x;
        private Double y;
        private Properties properties;
        private GraphProperties graphProperties;
        private Object nodeSize;
        private List<String> children;
        private Text text;
        private Integer zindex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edge {
        private String id;
        private String type;
        private String sourceNodeId;
        private String targetNodeId;
        private String sourceLabel;
        private String targetLabel;
        private String sourceUidPropertyName;
        private String targetUidPropertyName;
        private Properties properties;
        private GraphProperties graphProperties;
        private Text text;
        private Point endPoint;
        private Point startPoint;
        private List<Point> pointsList;
        private Integer zindex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Properties {
        private String component;
        private String showTitle;
        private String icon;
        private String gradientColor;
        private String borderType;
        private String borderColor;
        private String backgroundColor;
        private String textAlign;
        private String textDecoration;
        private String title;
        private String fontStyle;
        private String defaultProperty;
        private String fontFamily;
        private Integer borderWidth;
        private Integer fontSize;
        private String lineHeight;
        private String borderStyle;
        private String fontColor;
        private String fontWeight;
        private NodeSize nodeSize;
        private Boolean __edit;
        private String type;
        private List departmentList;
        private String biaodan111_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NodeSize {
        private Integer width;
        private Integer height;
        private Integer rx;
        private Integer ry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GraphProperties {
        private String biaodan111_id;
        private String miji;
        private String guanjiankongzhidian;
        private String biaodanbianhao;
        private String yaoqiulaiyuan;
        private String jianchadan_id;
        private String creator;
        private String modifyTime;
        private String createTime;
        private String modifier;
        private String zerenzhuti_name;
        private String front_end_id;
        private String id;
        private String label;
        private String zerenzhuti_id;
        private String huodongmingcheng;
        private String huodongbianhao;
        private String huodong1_id;
        private String gateway_id;
        private String mingcheng;
        private Integer from;
        private Integer to;
        private String container_id;
        private String relation_id;
        private String bdmc;
        private String list_num;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        private Double x;
        private Double y;
        private String value;

        // getters and setters
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private Double x;
        private Double y;

        // getters and setters
    }

}

