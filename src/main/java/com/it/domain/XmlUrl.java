package com.it.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_xml_url
 */
@TableName(value ="sys_xml_url")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlUrl implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String userName;

    /**
     * XML文件的路径地址
     */
    private String xmlUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}