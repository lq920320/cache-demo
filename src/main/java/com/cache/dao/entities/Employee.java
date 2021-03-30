package com.cache.dao.entities;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Data
public class Employee {
    /**
     * 员工ID
     */
    private Integer id;
    /**
     * 员工编号
     */
    private String empNo;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 员工邮箱
     */
    private String email;
    /**
     * 员工电话
     */
    private String phone;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
    /**
     * 更新时间
     */
    private LocalDateTime updateAt;
}
