package com.it.exception;

public enum CustomErrorType {
    USER_INPUT_ERROR(400,"参数异常"),
    SYSTEM_ERROR (500,"系统异常"),
    ACCOUNT_ERROR(403,"账户异常"),
    FILE_UPLOAD_ERROR(501,"文件上传异常"),
    RESOURCE_NOT_FOUND_ERROR(404,"无法找到对应的资源"),
    DATABASE_OP_ERROR(600,"数据库操作异常"),
    OTHER_ERROR(999,"其他未知异常");

    CustomErrorType(int code, String typeDesc) {
        this.code = code;
        this.typeDesc = typeDesc;
    }
    //异常类型中文描述
    private String typeDesc;
    //异常code
    private int code;

    public String getTypeDesc() {
        return typeDesc;
    }

    public int getCode() {
        return code;
    }
}