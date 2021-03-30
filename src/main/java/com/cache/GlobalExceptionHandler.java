package com.cache;

import com.cache.common.result.ResultWrapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liuqian
 * @date 2019/8/20 13:11.
 * 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResultWrapper<String> errorResponse(Throwable throwable) {
        Throwable cause = Throwables.getRootCause(throwable);
        String message = String.valueOf(cause.getMessage());
        log.error("There are some exceptions.", throwable);
        return new ResultWrapper<String>() {{
            setCode("50000");
            setMsg(message);
            setData(null);
        }};
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public ResultWrapper<String> handleException(Throwable e) {
        log.warn("HANDLED EXCEPTION: {}", e.getMessage(), e);
        return errorResponse(e);
    }

}
