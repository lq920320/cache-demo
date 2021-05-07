package com.cache.services.impl;

import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;
import com.cache.services.CacheableService;
import com.cache.services.EmployeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Service
public class CacheableServiceImpl implements CacheableService {

    public static final String CACHE_NAME = "local";

    @Resource
    private EmployeeService employeeService;

    @Override
    @CachePut(value = CACHE_NAME, key = "'key_'+#emp.empNo")
    public CacheResultVO putToCache(EmployeeVO emp) {
        CacheResultVO cacheResult = new CacheResultVO();
        cacheResult.setEmployee(emp);
        cacheResult.setDesc("这里是本地cache缓存到数据");
        return cacheResult;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'key_'+#empNo")
    public CacheResultVO getEmpByNo(String empNo) {
        EmployeeVO employeeVO = employeeService.getByEmpNo(empNo);
        CacheResultVO cacheResult = new CacheResultVO();
        cacheResult.setEmployee(employeeVO);
        cacheResult.setDesc("这里是从数据库中获取到数据");
        return cacheResult;
    }
}
