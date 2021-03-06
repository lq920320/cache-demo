package com.cache.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Data
public class EmployeeVO {
    /**
     * 员工ID
     */
    @ApiModelProperty("员工ID")
    private Integer id;
    /**
     * 员工编号
     */
    @ApiModelProperty("员工编号")
    private String empNo;
    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    private String name;
    /**
     * 员工邮箱
     */
    @ApiModelProperty("员工邮箱")
    private String email;
    /**
     * 员工电话
     */
    @ApiModelProperty("员工电话")
    private String phone;
}
