package com.it.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.it.vo.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public AjaxResponse defaultServiceErrorHandler(HttpServletRequest request, Exception e){
        log.info("GlobalExceptionHandler开始输出异常信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>：");
        e.printStackTrace();
        log.info("GlobalExceptionHandler输出异常信息完毕>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>！");
        if(e instanceof NoHandlerFoundException){
            log.info("没有找到匹配的资源");
            return AjaxResponse.error(new CustomError(CustomErrorType.RESOURCE_NOT_FOUND_ERROR,e.getMessage()));
        }
        if(e instanceof NullPointerException){
            log.info("对象为空");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"对象为空"));
        }
        if(e instanceof IndexOutOfBoundsException){
            log.info("下标越界");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"下标越界"));
        }
        if(e instanceof NumberFormatException){
            log.info("不能转换成数值");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"不能转换成数值"));
        }
        if(e instanceof ClassCastException){
            log.info("类型无法转换");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"类型无法转换"));
        }

        //全局数据效验异常处理
        if (e instanceof BindException){
            log.info("数据校验错误");
                List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            Map<String, Object> map = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                System.out.println(fieldError.getField() + ":" + fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return AjaxResponse.error(new CustomError(CustomErrorType.Data_Validation_ERROR,"数据效验失败"),map);
        }
        return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,e.getMessage()));
    }
    @ExceptionHandler({NestedRuntimeException.class})
    @ResponseBody
    public AjaxResponse defaultSystemErrorHandler(HttpServletRequest request, Exception e){
        log.info("GlobalExceptionHandler开始输出异常信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>：");
        e.printStackTrace();
        log.info("GlobalExceptionHandler输出异常信息完毕>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>！");
        if(e instanceof MethodArgumentTypeMismatchException){
            log.info("参数类型不匹配");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"参数类型不匹配"));
        }
        if (e instanceof NullPointerException){
            log.info("对象为空");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,"对象为空"));
        }
        if(e instanceof InvalidFormatException){
            log.info("JSON数据反序列化失败");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,e.getMessage()));
        }
        if(e instanceof HttpMessageNotReadableException){
            log.info("参数JSON序列化失败");
            return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,e.getMessage()));
        }
        if(e instanceof MaxUploadSizeExceededException){
            log.info("上传文件超出指定大小");
            return AjaxResponse.error(new CustomError(
                    CustomErrorType.FILE_UPLOAD_ERROR,
                    "总文件不能超过10M，单个文件不能超过5M！"));
        }
        return AjaxResponse.error(new CustomError(CustomErrorType.SYSTEM_ERROR,e.getMessage()));
    }
}