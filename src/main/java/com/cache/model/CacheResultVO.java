package com.cache.model;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("描述")
    private String desc;
    /**
     * 要访问的对象
     */
    @ApiModelProperty("员工返回信息")
    private EmployeeVO employee;
}
