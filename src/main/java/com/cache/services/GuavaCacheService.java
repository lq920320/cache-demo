package com.cache.services;

import com.cache.model.CacheResultVO;

import java.util.concurrent.ExecutionException;

/**
 * 使用 guava 创建本地缓存
 *
 * @author zetu
 * @date 2021/3/24
 */
public interface GuavaCacheService {
    /**
     * 获取员工数据
     *
     * @param empNo   员工编号
     * @param timeout 是否有期限
     * @return 员工数据
     */
    CacheResultVO getEmpByNo(String empNo, Boolean timeout) throws ExecutionException;
}
