package com.cache.common.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuqian
 * @date 2020-09-11 9:43
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String code;
    private String msg;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
