package com.cache.services.impl;

import com.cache.dao.entities.Employee;
import com.cache.dao.mappers.EmployeeMapper;
import com.cache.model.EmployeeVO;
import com.cache.services.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zetu
 * @date 2021/3/26
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper empMapper;

    @Override
    public Integer addEmp(EmployeeVO emp) {
        Employee empEntity = new Employee();
        BeanUtils.copyProperties(emp, empEntity);
        LocalDateTime nowDate = LocalDateTime.now();
        empEntity.setCreateAt(nowDate);
        empEntity.setUpdateAt(nowDate);
        empMapper.insertEmp(empEntity);
        return empEntity.getId();
    }

    @Override
    public Boolean updateEmp(Integer id, EmployeeVO emp) {
        Employee empEntity = new Employee();
        BeanUtils.copyProperties(emp, empEntity);
        LocalDateTime nowDate = LocalDateTime.now();
        empEntity.setUpdateAt(nowDate);
        int count = empMapper.updateEmp(id, empEntity);
        return count >= 0;
    }

    @Override
    public EmployeeVO getById(Integer id) {
        Employee empEntity = empMapper.getById(id);
        if (Objects.isNull(empEntity)) {
            return null;
        }
        EmployeeVO result = new EmployeeVO();
        BeanUtils.copyProperties(empEntity, result);
        return result;
    }

    @Override
    public List<EmployeeVO> getList() {
        List<Employee> empList = empMapper.getList();
        if (Objects.isNull(empList) || empList.size() <= 0) {
            return Collections.emptyList();
        }

        return empList.stream().map(emp -> {
            EmployeeVO vo = new EmployeeVO();
            BeanUtils.copyProperties(emp, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
