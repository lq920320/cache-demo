package com.cache.services.impl;

import com.cache.model.CacheResultVO;
import com.cache.services.GuavaCacheService;
import org.springframework.stereotype.Service;

/**
 * @author zetu
 * @date 2021/3/24
 */
@Service
public class GuavaCacheServiceImpl implements GuavaCacheService {



    @Override
    public CacheResultVO getEmpByNo(String empNo, Boolean timeout) {
        return null;
    }
}
