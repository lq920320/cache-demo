package com.cache.dao.mappers;

import com.cache.dao.entities.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Mapper
@Repository
public interface EmployeeMapper {

    /**
     * 插入数据
     *
     * @param empEntity
     */
    void insertEmp(Employee empEntity);

    /**
     * 更新员工信息
     *
     * @param id
     * @param empEntity
     * @return
     */
    int updateEmp(@Param("id") Integer id, @Param("emp") Employee empEntity);

    /**
     * 根据ID获取员工信息
     *
     * @param id
     * @return
     */
    Employee getById(@Param("id") Integer id);

    /**
     * 获取员工信息列表
     *
     * @return
     */
    List<Employee> getList();

    /**
     * 根据员工编号获取员工信息
     *
     * @param empNo
     * @return
     */
    Employee getByEmpNo(@Param("empNo") String empNo);
}
