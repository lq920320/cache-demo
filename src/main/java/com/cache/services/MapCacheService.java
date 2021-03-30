package com.cache.services;

import com.cache.model.CacheResultVO;

/**
 * 使用内存map作为本地缓存工具
 *
 * @author zetu
 * @date 2021/3/24
 */
public interface MapCacheService {
    /**
     * 通过员工编号获取员工信息
     * 常规map
     *
     * @param empNo
     * @return
     */
    CacheResultVO getEmpByNo(String empNo);

    /**
     * 通过员工编号获取员工信息
     * 过期时间map
     *
     * @param empNo
     * @return
     */
    CacheResultVO getEmpByNoWithExpireMap(String empNo);
}
