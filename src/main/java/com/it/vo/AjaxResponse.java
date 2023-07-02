package com.it.vo;
import com.it.exception.CustomError;
import lombok.Data;

/**
 * 响应HTTP，统一前端消息数据结构
 * 无特殊情况下，当HTTP状态码为200，业务处理状态码也为200时，表示服务器响应成功<br>
 * 并且业务处理也正常返回
 */
@Data
public class AjaxResponse {
    //业务处理是否成功
    private boolean isSuccess;
    //业务处理状态码
    private int code;
    //对用户友好的语言描述异常的发生情况
    private String message;
    //向前端返回的数据
    private Object data;
    //私有构造方法，不允许外界调用
    private AjaxResponse() {
    }

    /**
     * 请求出现异常时的响应数据封装
     * @param e:异常
     * @return AjaxResponse 封闭后的响应对象
     */
    public static AjaxResponse error(CustomError e) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(false);
        resultBean.setCode(e.getCode());
        resultBean.setMessage(e.getMessage());
        return resultBean;
    }
    /**
     * 请求正常的响应时进行数据封装
     * @return AjaxResponse 封闭后的响应对象
     */
    public static AjaxResponse success() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        return resultBean;
    }
    /**
     * 请求正常的响应时进行数据封装
     * @param data:要封装到响应对象的数据
     * @return AjaxResponse 封闭后的响应对象
     */
    public static AjaxResponse success(Object data) {
       AjaxResponse resultBean = new AjaxResponse();
        resultBean.setSuccess(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }
}