package com.cache.services.impl;

import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;
import com.cache.services.EmployeeService;
import com.cache.services.GuavaCacheService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Service
@Slf4j
public class GuavaCacheServiceImpl implements GuavaCacheService {

    @Autowired
    private EmployeeService employeeService;

    private static final LoadingCache<String, EmployeeVO> LOADING_CACHE;
    private static final LoadingCache<String, EmployeeVO> TIMED_LOADING_CACHE;

    static {
        LOADING_CACHE = CacheBuilder.newBuilder()
                // 最多容纳 30 个元素
                .maximumSize(30)
                // 这里可以监听移除动作
                .removalListener(notification -> log.info("删除原因={}，删除 key={},删除 value={}",
                        notification.getCause(), notification.getKey(), notification.getValue()))
                .build(new CacheLoader<String, EmployeeVO>() {
                    @Override
                    public EmployeeVO load(String key) throws Exception {
                        // 当 key 值对应当 value 不存在时，返回当默认值
                        return null;
                    }
                });
        TIMED_LOADING_CACHE = CacheBuilder.newBuilder()
                // 最多容纳 30 个元素
                .maximumSize(30)
                // 默认访问后60s 过期
                .expireAfterAccess(60, TimeUnit.SECONDS)
                // 这里可以监听移除动作
                .removalListener(notification -> log.info("删除原因={}，删除 key={},删除 value={}",
                        notification.getCause(), notification.getKey(), notification.getValue()))
                .build(new CacheLoader<String, EmployeeVO>() {
                    @Override
                    public EmployeeVO load(String key) throws Exception {
                        // 当 key 值对应当 value 不存在时，返回当默认值
                        return new EmployeeVO();
                    }
                });
    }

    @Override
    public CacheResultVO getEmpByNo(String empNo, Boolean timeout) throws ExecutionException {
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
    private CacheResultVO getEmpByNoWithNoExpire(String empNo) throws ExecutionException {
        CacheResultVO result = new CacheResultVO();
        if (Objects.nonNull(LOADING_CACHE.get(empNo))) {
            result.setDesc("guava loading cache 缓存数据");
            result.setEmployee(LOADING_CACHE.get(empNo));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 loading cache
        LOADING_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据");
        result.setEmployee(LOADING_CACHE.get(empNo));
        return result;
    }

    private CacheResultVO getEmpByNoWithExpireCache(String empNo) throws ExecutionException {
        CacheResultVO result = new CacheResultVO();
        if (Objects.nonNull(TIMED_LOADING_CACHE.get(empNo))) {
            result.setDesc("guava loading cache timed 缓存数据");
            result.setEmployee(TIMED_LOADING_CACHE.get(empNo));
            return result;
        }
        // 从数据库获取数据
        EmployeeVO empVo = employeeService.getByEmpNo(empNo);
        // 放入缓存的 loading cache
        TIMED_LOADING_CACHE.put(empNo, empVo);

        result.setDesc("数据库数据（可能是初始的数据库的数据，也可能是缓存过期后重新获取的）");
        result.setEmployee(TIMED_LOADING_CACHE.get(empNo));
        return result;
    }
}
