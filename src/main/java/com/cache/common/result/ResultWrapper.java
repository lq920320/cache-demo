package com.cache.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultWrapper<T> {
    /**
     * 结果对象
     */
    private T data;

    /**
     * 错误码
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    public static <T> ResultWrapper<T> of(T data) {
        return new ResultWrapper<>(data, "0000", "success");
    }

    public static <T> ResultWrapper<T> success() {
        return new ResultWrapper<>(null, "0000", "success");
    }

    public static <T> ResultWrapper<T> fail() {
        return new ResultWrapper<>(null, "5000", "failed");
    }
}
