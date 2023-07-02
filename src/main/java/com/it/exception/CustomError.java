package com.it.exception;

public class CustomError extends RuntimeException {
    //异常错误编码
    private int code ;
    //异常信息
    private String message;

    private CustomError(){}

    /**
     * 用于系统产生的错误
     * @param cause
     * @param errorType
     * @param message  用于替换CustomErrorType默认的异常描述信息
     */
    public CustomError(Throwable cause, CustomErrorType errorType, String message ) {
        super(message, cause);
        this.code = errorType.getCode();
        this.message = message;
    }
    /**
     * 用于系统产生的错误
     * @param cause
     * @param errorType
     */
    public CustomError(Throwable cause, CustomErrorType errorType) {
        super(errorType.getTypeDesc(), cause);
        this.code = errorType.getCode();
        this.message = errorType.getTypeDesc();
    }
    /**
     * 用于系统产生的错误
     * @param cause
     * @param message 用于替换CustomErrorType默认的异常描述信息
     */
    public CustomError(Throwable cause, String message ) {
        super(message, cause);
        this.code=500;
        this.message=message;
    }

    /**
     * 用于自定义业务异常
     * @param errorType
     * @param message 用于替换CustomErrorType默认的异常描述信息
     */
    public CustomError(CustomErrorType errorType, String message ) {
        this.code = errorType.getCode();
        this.message = message;
    }
    /**
     * 用于自定义业务异常
     * @param errorType
     */
    public CustomError(CustomErrorType errorType ) {
        this.code = errorType.getCode();
        this.message = errorType.getTypeDesc();
    }

    public int getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
