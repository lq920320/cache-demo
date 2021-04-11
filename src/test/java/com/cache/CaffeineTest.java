package com.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zetu
 * @date 2021/3/25
 */
public class CaffeineTest {

    @Test
    public void caffeineTest() throws InterruptedException {
        Cache<String, String> caffeineCache = Caffeine.newBuilder()
                // 最大容量
                .maximumSize(10)
                // 写后有效期
                .expireAfterWrite(2, TimeUnit.SECONDS)
                // 访问后有效期
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build();

        caffeineCache.put("hello", "world");
        // 结果：world
        System.out.println(caffeineCache.getIfPresent("hello"));
        // 休眠3秒，过期
        Thread.sleep(3000);
        // 结果：null
        System.out.println(caffeineCache.getIfPresent("hello"));
    }

    @Test
    public void caffeineNoTimeoutTest() throws InterruptedException {
        Cache<String, String> caffeineCache = Caffeine.newBuilder()
                .maximumSize(10)
                .build();

        caffeineCache.put("hello", "world");
        // 结果：world
        System.out.println(caffeineCache.getIfPresent("hello"));
        // 休眠3秒，不会清除
        Thread.sleep(3000);
        // 结果：world
        System.out.println(caffeineCache.getIfPresent("hello"));
    }


    @Test
    public void fixedCacheTest() {
        Cache<String, String> fixedCache = Caffeine.newBuilder()
                .maximumSize(2)
                .build(new CacheLoader<String, String>() {
                    // 当 key 不存在时返回默认 null
                    @Override
                    public @Nullable String load(@NonNull String s) throws Exception {
                        return null;
                    }
                });

        fixedCache.put("key1", "value1");
        fixedCache.put("key2", "value2");
        // 获取一下 key = 1 的数据
        System.out.println(fixedCache.getIfPresent("key1"));

        fixedCache.put("key3", "value3");
        // 超出范围，caffeine 是基于 LRU 进行删除数据的
        fixedCache.put("key4", "value4");

        // 获取 key = 1 还是可以的
        System.out.println(fixedCache.getIfPresent("key1"));
        // 获取 key = 2 便是为 null，key = 3 未被访问过会被删除
        System.out.println(fixedCache.getIfPresent("key2"));
        System.out.println(fixedCache.estimatedSize());

    }
}
