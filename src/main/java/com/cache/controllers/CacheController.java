package com.cache.controllers;

import com.cache.common.result.ResultWrapper;
import com.cache.model.CacheResultVO;
import com.cache.services.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 本地缓存调用测试相关接口
 *
 * @author zetu
 * @date 2021/3/24
 */
@RestController
@RequestMapping("api/cache")
public class CacheController {

    @Resource
    private CacheableService cacheableService;
    @Resource
    private GuavaCacheService guavaCacheService;
    @Resource
    private HutoolCacheService hutoolCacheService;
    @Resource
    private MapCacheService mapCacheService;
    @Resource
    private CaffeineService caffeineService;

    /**
     * 获取缓存数据
     *
     * @return
     */
    @GetMapping("cacheable")
    public ResultWrapper<CacheResultVO> getCacheableResult() {
        return null;
    }

    /**
     * 获取 guava 缓存
     *
     * @return
     */
    @GetMapping("guava")
    public ResultWrapper<CacheResultVO> getGuavaCacheResult() {
        return null;
    }

    /**
     * 获取 hutool 缓存
     *
     * @return
     */
    @GetMapping("hutool")
    public ResultWrapper<CacheResultVO> getHutoolCacheResult() {
        return null;
    }

    /**
     * 获取 map 缓存
     *
     * @return
     */
    @GetMapping("map")
    public ResultWrapper<CacheResultVO> getMapCacheResult() {
        return null;
    }

    /**
     * 获取 caffeine 缓存
     *
     * @return
     */
    @GetMapping("caffeine")
    public ResultWrapper<CacheResultVO> getCaffeineCacheResult() {
        return null;
    }
}
