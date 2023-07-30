package com.it.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.*;

/**
 * 封装的分页信息
 */
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 每页记录数
     */
    private int pageSize = 10;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage = 1;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 前端分页列表
     */
    private List<String> pageList;

    public Integer getStart(){
        // currPage 1 0
        return (this.currPage-1) * this.pageSize + 1 ;
    }

    public Integer getEnd(){
        return this.currPage  * this.pageSize;
    }

    /**
     * 获取前端分页列表数据
     * 一共显示7个数据
     * @return
     */
    public List<String> getPageList() {
        pageList = new ArrayList<>();
        if(totalPage < 7){
            for (int i = 1; i <= totalPage ; i++) {
                pageList.add(i+"");
            }
        }else{
            if(currPage == 1|| currPage == 2 || currPage == 3){
                pageList = Arrays.asList("1","2","3","4","...",totalPage+"");
            }else{
                pageList = Arrays.asList((currPage-2)+"",(currPage-1)+"",currPage+"",(currPage+1)+"","...",totalPage+"");
            }
        }
        return pageList;
    }


    public  PageUtils(){

    }

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = (int)page.getTotal();
        this.pageSize = (int)page.getSize();
        this.currPage = (int)page.getCurrent();
        this.totalPage = (int)page.getPages();
    }

    public void setPage(IPage<?> page){
        this.list = page.getRecords();
        this.totalCount = (int)page.getTotal();
        this.pageSize = (int)page.getSize();
        this.currPage = (int)page.getCurrent();
        this.totalPage = (int)page.getPages();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String,Object> getMap(){
        Map<String,Object> map = new HashMap<>();
        map.put(Constant.PAGE_SIZE,this.getPageSize());
        map.put(Constant.PAGE,this.getCurrPage());
        return map;
    }
}
