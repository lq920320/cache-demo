package com.cache.model;

import lombok.Data;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Data
public class CacheResultVO {
    /**
     * 描述
     */
    private String desc;
    /**
     * 要访问的对象
     */
    private EmployeeVO employee;
}
