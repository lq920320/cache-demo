package com.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.file.LFUFileCache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * hutool 关于缓存的一些测试用例
 *
 * @author zetu
 * @date 2021/3/30
 */
public class HutoolCacheTest {

    /**
     * FIFOCache，即先进先出缓存，测试用例。
     * 先进先出策略。元素不停的加入缓存直到缓存满为止，当缓存满时，清理过期缓存对象，清理后依旧满则删除先入的缓存（链表首部对象）。
     */
    @Test
    public void fifoCacheTest() {
        Cache<String, String> fifoCache = CacheUtil.newFIFOCache(3);

        // 加入元素，每个元素可以设置其过期时长，DateUnit.SECOND.getMillis()代表每秒对应的毫秒数，在此为3秒
        fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
        fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // value1为null
        String value1 = fifoCache.get("key1");

        Assert.assertTrue(Objects.isNull(value1));
    }

    /**
     * LFUCache，即最少使用缓存，测试用例。
     * 最少使用率策略。根据使用次数来判定对象是否被持续缓存（使用率是通过访问次数计算），当缓存满时清理过期对象，
     * 清理后依旧满的情况下清除最少访问（访问计数最小）的对象并将其他对象的访问数减去这个最小访问数，以便新对象进入后可以公平计数。
     */
    @Test
    public void lfuCacheTest() {
        Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
        // 通过实例化对象创建
        // LFUCache<String, String> lfuCache = new LFUCache<String, String>(3);

        lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lfuCache.get("key1");//使用次数+1
        lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2,3被移除）
        String value2 = lfuCache.get("key2");//null
        String value3 = lfuCache.get("key3");//null

        Assert.assertTrue(Objects.isNull(value2));
        Assert.assertTrue(Objects.isNull(value3));
    }

    /**
     * LRUCache，即最近最少使用缓存
     * 根据使用时间来判定对象是否被持续缓存，当对象被访问时放入缓存，当缓存满了，最久未被使用的对象将被移除。
     * 此缓存基于LinkedHashMap，因此当被缓存的对象每被访问一次，这个对象的key就到链表头部。
     * 这个算法简单并且非常快，他比FIFO有一个显著优势是经常使用的对象不太可能被移除缓存。
     * 缺点是当缓存满时，不能被很快的访问。
     */
    @Test
    public void lruCacheTest() {
        Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
        // 通过实例化对象创建
        // LRUCache<String, String> lruCache = new LRUCache<String, String>(3);

        lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        // 使用时间推近
        lruCache.get("key1");

        lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
        String value2 = lruCache.get("key"); // null

        Assert.assertTrue(Objects.isNull(value2));
    }

    /**
     * TimedCache，即定时缓存，测试用例
     * 对被缓存的对象定义一个过期时间，当对象超过过期时间会被清理。此缓存没有容量限制，对象只有在过期后才会被移除。
     * <p>
     * 如果用户在超时前调用了get(key)方法，会重头计算起始时间。
     * 举个例子，用户设置key1的超时时间5s，用户在4s的时候调用了get("key1")，此时超时时间重新计算，
     * 再过4s调用get("key1")方法值依旧存在。如果想避开这个机制，请调用get("key1", false)方法。
     */
    @Test
    public void timedCacheTest() {
        // 创建缓存，默认4毫秒过期
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);
        // 实例化创建
        // TimedCache<String, String> timedCache = new TimedCache<String, String>(4);

        timedCache.put("key1", "value1", 1);//1毫秒过期
        timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);
        timedCache.put("key3", "value3");//默认过期(4毫秒)
        // 启动定时任务，每5毫秒秒检查一次过期
        timedCache.schedulePrune(5);
        // 等待5毫秒
        ThreadUtil.sleep(5);
        // 5毫秒后由于value2设置了 5 秒过期，因此只有value2被保留下来
        String value1 = timedCache.get("key1");// null
        String value2 = timedCache.get("key2");// value2

        Assert.assertTrue(Objects.isNull(value1));
        Assert.assertEquals("value2", value2);

        // 5毫秒后，由于设置了默认过期，key3只被保留4毫秒，因此为null
        String value3 = timedCache.get("key3");//null

        Assert.assertTrue(Objects.isNull(value3));

        // 取消定时清理
        timedCache.cancelPruneSchedule();
    }

    /**
     * WeakCache，即弱引用缓存。
     * 对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。
     * 丢弃某个键时，其条目从映射中有效地移除。该类使用了WeakHashMap做为其实现，缓存的清理依赖于JVM的垃圾回收。
     */
    @Test
    public void weakCacheTest() {
        WeakCache<String, String> weakCache = CacheUtil.newWeakCache(DateUnit.SECOND.getMillis() * 3);

        // 用法与 TimedCache 用法相同
    }

    /**
     * FileCache，文件缓存。
     * 主要是将小文件以byte[]的形式缓存到内存中，减少文件的访问，以解决频繁读取文件引起的性能问题。
     * 实现
     * LFUFileCache
     * LRUFileCache
     */
    @Test
    public void fileCacheTest() {
        // 参数1：容量，能容纳的byte数
        // 参数2：最大文件大小，byte数，决定能缓存至少多少文件，大于这个值不被缓存直接读取
        // 参数3：超时。毫秒
        LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
        byte[] bytes = cache.getFileBytes("d:/a.jpg");
    }


    @Test
    public void timedCacheTest2() {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);

        System.out.println(timedCache.containsKey("key1"));
        System.out.println(timedCache.getHitCount());

        timedCache.put("key1", "value1");
        timedCache.get("key1");
        // 等待5毫秒
        ThreadUtil.sleep(5);

        System.out.println(timedCache.containsKey("key1"));
        System.out.println(timedCache.containsKey("key2"));
        System.out.println(timedCache.getHitCount());

    }
}
