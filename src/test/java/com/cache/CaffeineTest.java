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
}
