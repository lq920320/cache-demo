package com.cache.services.impl;

import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;
import com.cache.services.CaffeineService;
import com.cache.services.EmployeeService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zetu
 * @date 2021/3/25
 */
@Service
@Slf4j
public class CaffeineServiceImpl implements CaffeineService {

    @Autowired
    private EmployeeService employeeService;

    private static final Cache<String, EmployeeVO> CAFFEINE_CACHE;
    private static final Cache<String, EmployeeVO> TIMED_CAFFEINE_CACHE;

    static {
        CAFFEINE_CACHE = Caffeine.newBuilder()
                // 最多容纳 30 个元素
                .maximumSize(30)
                // 这里可以监听移除动作
                .removalListener((key, value, removalCause) ->
                        log.info("删除原因={}，删除 key={},删除 value={}",
                                removalCause.name(), key, value))
                .build(key -> {
                    // 当 key 值对应当 value 不存在时，返回当默认值
                    return null;
                });
        TIMED_CAFFEINE_CACHE = Caffeine.newBuilder()
                // 最多容纳 30 个元素
                .maximumSize(30)
                // 默认访问后60s 过期
                .expireAfterAccess(60, TimeUnit.SECONDS)
                // 这里可以监听移除动作
                .removalListener((key, value, removalCause) ->
                        log.info("删除原因={}，删除 key={},删除 value={}",
                                removalCause.name(), key, value))
                .build(key -> {
                    // 当 key 值对应当 value 不存在时，返回当默认值
                    return null;
                });
    }

    @Override
    public CacheResultVO getEmpByNo(String empNo, Boolean timeout) {
        if (BooleanUtils.isTrue(timeout)) {
            return getEmpByNoWithExpireCache(empNo);
        }

        return getEmpByNoWithNoExpire(empNo);
    }

    /**
     * 不带过期时间的 LoadoingCache 数据
     *
     * @param empNo
     * @return
     */
    private CacheResultVO getEmpByNoWithNoExpire(String empNo) {
        CacheResultVO result = new CacheResultVO();
        if (Objects.nonNull(CAFFEINE_CACHE.getIfPresent(empNo))) {
            result.setDesc("guava loading cache 缓存数据");
            result.setEmployee(CAFFEINE_CACHE.getIfPresent(empNo));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 loading cache
        CAFFEINE_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据");
        result.setEmployee(CAFFEINE_CACHE.getIfPresent(empNo));
        return result;
    }

    private CacheResultVO getEmpByNoWithExpireCache(String empNo) {
        CacheResultVO result = new CacheResultVO();
        if (Objects.nonNull(TIMED_CAFFEINE_CACHE.getIfPresent(empNo))) {
            result.setDesc("guava loading cache timed 缓存数据");
            result.setEmployee(TIMED_CAFFEINE_CACHE.getIfPresent(empNo));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 loading cache
        TIMED_CAFFEINE_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据（可能是初始的数据库的数据，也可能是缓存过期后重新获取的）");
        result.setEmployee(TIMED_CAFFEINE_CACHE.getIfPresent(empNo));
        return result;
    }
}
