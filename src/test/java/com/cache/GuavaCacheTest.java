package com.cache;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * guava cache 相关测试
 *
 * @author zetu
 * @date 2021/3/30
 */
@Slf4j
public class GuavaCacheTest {

    private LoadingCache<Integer, AtomicLong> loadingCache;

    private Cache<String, String> cacheFormCallable;

    @Before
    public void init() {
        loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                // 这里可以监听移除动作
                .removalListener(notification -> log.info("删除原因={}，删除 key={},删除 value={}",
                        notification.getCause(), notification.getKey(), notification.getValue()))
                .build(new CacheLoader<Integer, AtomicLong>() {
                    @Override
                    public AtomicLong load(Integer key) throws Exception {
                        // 当 key 值对应当 value 不存在时，返回当默认值
                        return new AtomicLong(0);
                    }
                });

        cacheFormCallable = CacheBuilder.newBuilder().maximumSize(10L).build();
    }

    @Test
    public void loadingCacheTest() throws ExecutionException {
        loadingCache.put(1, new AtomicLong(1000));
        // 放入当值 1000
        System.out.println(loadingCache.get(1));
        // 默认值 0，而且会放入到 cache 中
        System.out.println(loadingCache.get(2));

        ThreadUtil.sleep(4000);
        // 过期，返回 默认值 0
        System.out.println(loadingCache.get(1));
    }

    @Test
    public void callableCacheTest() throws ExecutionException {
        cacheFormCallable.put("key1", "This is the original value!!!!!");
        String value1 = cacheFormCallable.get("key1", () -> "This is value1 !");

        System.out.println("key1's value is : " + value1);

    }
}
