package com.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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
}
