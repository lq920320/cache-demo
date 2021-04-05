package com.cache.services.impl;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;
import com.cache.services.EmployeeService;
import com.cache.services.HutoolCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Service
public class HutoolCacheServiceImpl implements HutoolCacheService {

    @Resource
    private EmployeeService employeeService;

    /**
     * 创建一个 LRUCache ，作为没有过期时间的缓存，删除策略为最近最少访问删除，默认容量为 10
     * value 为员工数据，可以为空，避免击穿
     */
    private static final Cache<String, EmployeeVO> LRU_CACHE = CacheUtil.newLRUCache(10);
    /**
     * 创建一个有缓存时间的 TimedCache，默认过期时间为 60 s
     * value 为员工数据，可以为空，避免击穿
     */
    private static final Cache<String, EmployeeVO> TIMED_CACHE = CacheUtil.newTimedCache(60000);


    @Override
    public CacheResultVO getEmpByNo(String empNo) {
        CacheResultVO result = new CacheResultVO();
        if (LRU_CACHE.containsKey(empNo)) {
            result.setDesc("hutool cache 缓存数据");
            result.setEmployee(LRU_CACHE.get(empNo));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 map
        LRU_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据");
        result.setEmployee(LRU_CACHE.get(empNo));
        return result;
    }

    /**
     * 获取过期缓存中的数据，检查是否过期这一步，直接通过获取数据来判断
     * hutool 没有提供检查固定 key 是否过期的方法
     *
     * @param empNo
     * @return
     */
    @Override
    public CacheResultVO getEmpByNoWithExpireMap(String empNo) {
        CacheResultVO result = new CacheResultVO();

        if (TIMED_CACHE.containsKey(empNo)) {
            result.setDesc("带有过期的 hutool cache 缓存数据");
            // 第二个参数设置为 false，超时时间不进行重新计算
            result.setEmployee(TIMED_CACHE.get(empNo, false));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 map
        TIMED_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据（可能是初始的数据库的数据，也可能是缓存过期后重新获取的）");
        result.setEmployee(TIMED_CACHE.get(empNo));
        return result;
    }
}
