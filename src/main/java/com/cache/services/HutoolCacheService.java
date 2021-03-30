package com.cache.services;

import com.cache.model.CacheResultVO;

/**
 * hutool 创建本地缓存
 *
 * @author zetu
 * @date 2021/3/24
 */
public interface HutoolCacheService {

    /**
     * 根据员工编号获取数据
     * 普通cache
     *
     * @param empNo
     * @return
     */
    CacheResultVO getEmpByNo(String empNo);

    /**
     * 根据员工编号获取数据
     * 具有过期时间的cache
     *
     * @param empNo
     * @return
     */
    CacheResultVO getEmpByNoWithExpireMap(String empNo);
}
