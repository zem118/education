package com.education.common.exception;


import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;

/**
 * 业务异常
 *   

 **/
public class BusinessException extends RuntimeException {

    private ResultCode resultCode;

    private Result result;

    public BusinessException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public BusinessException(Result result){
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(Throwable cause){
        super(cause);
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
