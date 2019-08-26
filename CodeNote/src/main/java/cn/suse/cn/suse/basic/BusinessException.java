/*
 * Copyright 2016 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */
package cn.suse.cn.suse.basic;


import cn.suse.constants.ErrorCodeEnum;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1604303483653130736L;

    /**
     * 错误枚举
     */
    private ErrorCodeEnum    error;

    /**
     * 错误编号
     */
    private String           errorCode;

    /**
     * 错误消息
     */
    private String            errorMsg;

    public BusinessException(ErrorCodeEnum error) {
        super(error.getDesc());
        this.error = error;
        errorCode = error.getCode();
        errorMsg = error.getDesc();
    }
    public BusinessException(ErrorCodeEnum error,String msg) {
        super(msg);
        this.error = error;
        this.errorCode = error.getCode();
        this.errorMsg = msg;
    }
    
    public ErrorCodeEnum getError() {
        return error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
