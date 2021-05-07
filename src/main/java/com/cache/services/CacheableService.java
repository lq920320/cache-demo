package com.cache.services;

import com.cache.model.CacheResultVO;
import com.cache.model.EmployeeVO;

/**
 * 使用 spring cache 进行本地缓存
 *
 * @author zetu
 * @date 2021/3/24
 */
public interface CacheableService {

    /**
     * 把数据放入到 cache
     *
     * @param emp
     * @return
     */
    CacheResultVO putToCache(EmployeeVO emp);

    /**
     * 根据员工编号获取信息
     *
     * @param empNo 员工编号
     * @return 员工信息
     */
    CacheResultVO getEmpByNo(String empNo);
}
