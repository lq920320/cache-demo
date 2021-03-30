package com.cache.controllers;

import com.cache.common.result.ResultWrapper;
import com.cache.model.CacheResultVO;
import com.cache.services.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zetu
 * @date 2021/3/24
 */
@RestController
@RequestMapping("api/cache")
@Api(tags = "本地缓存调用测试相关接口")
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

    @ApiOperation("获取Cacheable缓存数据")
    @GetMapping("cacheable")
    public ResultWrapper<CacheResultVO> getCacheableResult(@ApiParam(name = "empNo", value = "员工编号") @RequestParam(value = "empNo", required = false) String empNo) {
        return null;
    }

    @GetMapping("guava")
    @ApiOperation("获取 guava 缓存")
    public ResultWrapper<CacheResultVO> getGuavaCacheResult(@ApiParam(name = "empNo", value = "员工编号") @RequestParam(value = "empNo", required = false) String empNo) {
        return null;
    }

    @GetMapping("hutool")
    @ApiOperation("获取 hutool 缓存")
    public ResultWrapper<CacheResultVO> getHutoolCacheResult(@ApiParam(name = "empNo", value = "员工编号") @RequestParam(value = "empNo", required = false) String empNo) {
        return null;
    }

    @GetMapping("map")
    @ApiOperation("获取 map 缓存")
    public ResultWrapper<CacheResultVO> getMapCacheResult(
            @ApiParam(name = "empNo", value = "员工编号") @RequestParam(value = "empNo", required = false) String empNo,
            @ApiParam(name = "timeout", value = "是否有过期时间（默认60s）") @RequestParam(value = "timeout", required = false) Boolean timeout
    ) {
        CacheResultVO result;
        if (BooleanUtils.toBoolean(timeout)) {
            // 有过期时间的 map
            result = mapCacheService.getEmpByNoWithExpireMap(empNo);
        } else {
            // 常规 map
            result = mapCacheService.getEmpByNo(empNo);
        }
        return ResultWrapper.of(result);
    }

    @GetMapping("caffeine")
    @ApiOperation("获取 caffeine 缓存")
    public ResultWrapper<CacheResultVO> getCaffeineCacheResult(@ApiParam(name = "empNo", value = "员工编号") @RequestParam(value = "empNo", required = false) String empNo) {
        return null;
    }
}
