package com.education.common.exception; 

import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class SystemExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result resolveException(Exception ex) {
        log.error("系统异常", ex);
        if (ex instanceof BusinessException) {
            BusinessException exception = (BusinessException)ex;

            if (exception.getResult() != null) {
                return exception.getResult();
            }

            return Result.fail(exception.getResultCode().getCode(), exception.getResultCode().getMessage());
        } else if (ex instanceof UnauthenticatedException
           || ex instanceof UnauthorizedException) {
            return Result.fail(ResultCode.NO_PERMISSION, "权限不足,无法访问");
        } else if (ex instanceof MaxUploadSizeExceededException) {
            return Result.fail(ResultCode.NO_PERMISSION, "文件不能超过200M, 请重新上传");
        }
        return Result.fail(ResultCode.FAIL, "系统异常");
    }
}
