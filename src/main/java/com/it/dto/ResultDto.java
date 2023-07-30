//package com.it.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ResultDto {
//
//    @JsonProperty("process")
//    private Object process;
//
//    @JsonProperty("processId")
//    private String processId;
//
//    @JsonProperty("nodes")
//    private List<Node> nodes;
//
//    @JsonProperty("edges")
//    private List<Edge> edges;
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Node {
//        @JsonProperty("id")
//        private String id;
//
//        @JsonProperty("type")
//        private String type;
//
//        @JsonProperty("x")
//        private Double x;
//
//        @JsonProperty("y")
//        private Double y;
//
//        @JsonProperty("properties")
//        private Properties properties;
//
//        @JsonProperty("graphProperties")
//        private GraphProperties graphProperties;
//
//        @JsonProperty("nodeSize")
//        private Object nodeSize;
//
//        @JsonProperty("children")
//        private List<Object> children;
//
//        @JsonProperty("text")
//        private Text text;
//
//        @JsonProperty("zindex")
//        private int zindex;
//
//        @Data
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Properties {
//            @JsonProperty("nodeSize")
//            private NodeSize nodeSize;
//            @JsonProperty("borderColor")
//            private String borderColor;
//            @JsonProperty("backgroundColor")
//            private String backgroundColor;
//
//        }
//
//        @Data
//        @NoArgsConstructor
//        @AllArgsConstructor
//        //Node
//        public static class GraphProperties {
//
//            @JsonProperty("panduantiaojian")
//            private String panduantiaojian;
//            @JsonProperty("section_no")
//            private String section_no;
//            @JsonProperty("duiyingguocheng")
//            private String duiyingguocheng;
//            @JsonProperty("section_id")
//            private String section_id;
//            @JsonProperty("yaoqiuluodi")
//            private String yaoqiuluodi;
//            @JsonProperty("type")
//            private String type;
//            @JsonProperty("section_name")
//            private String section_name;
//
//            @JsonProperty("gateway_id")
//            private String gateway_id;
//            @JsonProperty("guanjiankongzhidian")
//            private String guanjiankongzhidian;
//            @JsonProperty("jianchadan_id")
//            private String jianchadan_id;
//            @JsonProperty("miji")
//            private String miji;
//
//            @JsonProperty("mingcheng")
//            private String mingcheng;
//
//            @JsonProperty("biaodanbianhao")
//            private String biaodanbianhao;
//
//            @JsonProperty("yaoqiulaiyuan")
//            private String yaoqiulaiyuan;
//
//            @JsonProperty("modifyTime")
//            private String modifyTime;
//
//            @JsonProperty("zerenzhuti_name")
//            private String zerenzhutiName;
//
//            @JsonProperty("zerenzhuti_id")
//            private String zerenzhuti_id;
//
//            @JsonProperty("creatTime")
//            private String creatTime;
//
//            @JsonProperty("laiyuanbianhao")
//            private String laiyuanbianhao;
//
//            @JsonProperty("isDel")
//            private String isDel;
//
//            @JsonProperty("owner")
//            private String owner;
//
//            @JsonProperty("renwulaiyuan")
//            private String renwulaiyuan;
//
//            @JsonProperty("biaodan111_id")
//            private String biaodan111_id;
//
//            @JsonProperty("creator")
//            private String creator;
//
//            @JsonProperty("huodongmingcheng")
//            private String huodongmingcheng;
//
//            @JsonProperty("linghao")
//            private String linghao;
//
//            @JsonProperty("guochengbianhao")
//            private String guochengbianhao;
//
//            @JsonProperty("bdmc")
//            private String bdmc;
//
//            @JsonProperty("riqi")
//            private String riqi;
//
//            @JsonProperty("biaodansuoyouzhe")
//            private String biaodansuoyouzhe;
//
//            @JsonProperty("list_num")
//            private String list_num;
//
//            @JsonProperty("createTime")
//            private String createTime;
//
//            @JsonProperty("huodongbianhao")
//            private String huodongbianhao;
//
//            @JsonProperty("modifier")
//            private String modifier;
//
//            @JsonProperty("huodong1_id")
//            private String huodong1_id;
//
//            @JsonProperty("front_end_id")
//            private String front_end_id;
//
//            @JsonProperty("id")
//            private String id;
//
//            @JsonProperty("label")
//            private String label;
//        }
//
//        @Data
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Text {
//            @JsonProperty("x")
//            private Double x;
//
//            @JsonProperty("y")
//            private Double y;
//
//            @JsonProperty("value")
//            private String value;
//        }
//
//        public static class NodeSize{
//            @JsonProperty("width")
//            private Integer width;
//            @JsonProperty("height")
//            private Integer height;
//            @JsonProperty("rx")
//            private Integer rx;
//            @JsonProperty("ry")
//            private Integer ry;
//        }
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Edge {
//        @JsonProperty("id")
//        private String id;
//
//        @JsonProperty("type")
//        private String type;
//
//        @JsonProperty("sourceNodeId")
//        private String sourceNodeId;
//
//        @JsonProperty("targetNodeId")
//        private String targetNodeId;
//
//        @JsonProperty("sourceLabel")
//        private Object sourceLabel;
//
//        @JsonProperty("targetLabel")
//        private Object targetLabel;
//
//        @JsonProperty("sourceUidPropertyName")
//        private Object sourceUidPropertyName;
//
//        @JsonProperty("targetUidPropertyName")
//        private Object targetUidPropertyName;
//
//        @JsonProperty("properties")
//        private Properties properties;
//
//        @JsonProperty("graphProperties")
//        private GraphProperties graphProperties;
//
//        @JsonProperty("text")
//        private Object text;
//
//        @JsonProperty("endPoint")
//        private Object endPoint;
//
//        @JsonProperty("startPoint")
//        private Object startPoint;
//
//        @JsonProperty("pointsList")
//        private Object pointsList;
//
//        @JsonProperty("zindex")
//        private int zindex;
//
//        @Data
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Properties {
//            @JsonProperty("nodeSize")
//            private String nodeSize;
//            @JsonProperty("borderColor")
//            private String borderColor;
//
//            @JsonProperty("borderStyle")
//            private String borderStyle;
//        }
//
//        @Data
//        @NoArgsConstructor
//        @AllArgsConstructor
//        //Edges
//        public static class GraphProperties {
//            @JsonProperty("panduantiaojian")
//            private String panduantiaojian;
//            @JsonProperty("modifier")
//            private String modifier;
//
//            @JsonProperty("modifyTime")
//            private String modifyTime;
//
//            @JsonProperty("tree_search")
//            private String treeSearch;
//            @JsonProperty("column_search")
//            private String columnSearch;
//
//            @JsonProperty("creator")
//            private String creator;
//
//            @JsonProperty("createTime")
//            private String createTime;
//
//            @JsonProperty("from")
//            private Integer from;
//
//            @JsonProperty("front_end_id")
//            private String front_end_id;
//
//            @JsonProperty("id")
//            private Integer id;
//
//            @JsonProperty("label")
//            private String label;
//
//            @JsonProperty("to")
//            private Integer to;
//
//            @JsonProperty("container_id")
//            private String container_id;
//
//            @JsonProperty("relation_id")
//            private String  relation_id;
//        }
//    }
//}