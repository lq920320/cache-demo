package com.cache.services;

import com.cache.model.CacheResultVO;

/**
 * 使用 caffeine 创建本地缓存
 *
 * @author zetu
 * @date 2021/3/25
 */
public interface CaffeineService {
    /**
     * 根据员工编号获取缓存数据
     *
     * @param empNo   员工编号
     * @param timeout 是否有有效期
     * @return 结果
     */
    CacheResultVO getEmpByNo(String empNo, Boolean timeout);
}
