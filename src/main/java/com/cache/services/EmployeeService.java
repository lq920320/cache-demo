package com.cache.services;

import com.cache.model.EmployeeVO;

import java.util.List;

/**
 * @author zetu
 * @date 2021/3/26
 */
public interface EmployeeService {
    /**
     * 创建员工信息
     *
     * @param emp 员工基础信息
     * @return 员工ID
     */
    Integer addEmp(EmployeeVO emp);

    /**
     * 更新员工信息
     *
     * @param id  员工ID
     * @param emp 员工基础信息
     * @return 更新结果
     */
    Boolean updateEmp(Integer id, EmployeeVO emp);

    /**
     * 根据ID获取员工信息
     *
     * @param id
     * @return
     */
    EmployeeVO getById(Integer id);

    /**
     * 获取员工信息列表
     *
     * @return
     */
    List<EmployeeVO> getList();
}
