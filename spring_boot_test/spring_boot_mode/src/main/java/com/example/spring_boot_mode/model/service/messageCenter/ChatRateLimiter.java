package com.example.spring_boot_mode.model.service.messageCenter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 聊天室发送频率限制器（滑动窗口简化版）
 * <p>
 * 设计目标：
 * 1. 防止单用户短时间大量刷消息（导致 DB 写放大和广播风暴）。
 * 2. 内存级实现，零外部依赖（项目未引入 Redis/Guava RateLimiter 依赖）。
 * 3. 滑动窗口算法：每个用户维护一个时间窗，窗口内累计计数超过阈值则拒绝。
 * <p>
 * 阈值可由 application.yml 覆盖，未配置时使用默认值（5 条/秒，30 条/分钟）。
 *
 * @author mavis
 */
@Component
public class ChatRateLimiter {

    /** 每秒允许的最大消息数（默认 5） */
    @Value("${chat.rate-limit.per-second:5}")
    private int perSecondLimit;

    /** 每分钟允许的最大消息数（默认 30） */
    @Value("${chat.rate-limit.per-minute:30}")
    private int perMinuteLimit;

    /** 用户级计数缓存 */
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    /**
     * 检查并消耗一次发送配额
     *
     * @param userId 用户ID
     * @return true=放行，false=被限流
     */
    public boolean tryAcquire(String userId) {
        if (userId == null || userId.isEmpty()) {
            return true;
        }
        WindowCounter counter = counters.computeIfAbsent(userId, k -> new WindowCounter());
        return counter.tryAcquire(perSecondLimit, perMinuteLimit);
    }

    /**
     * 简单滑动窗口计数器
     */
    private static class WindowCounter {
        /** 上一秒的时间戳（毫秒） */
        private final AtomicLong secondTick = new AtomicLong(0);
        /** 当前秒已用配额 */
        private final AtomicInteger secondUsed = new AtomicInteger(0);
        /** 上一分钟的时间戳（毫秒） */
        private final AtomicLong minuteTick = new AtomicLong(0);
        /** 当前分钟已用配额 */
        private final AtomicInteger minuteUsed = new AtomicInteger(0);

        boolean tryAcquire(int perSecond, int perMinute) {
            long now = System.currentTimeMillis();
            long curSec = now / 1000L;
            long curMin = now / 60_000L;

            // 滑动秒级窗口
            if (secondTick.compareAndSet(curSec - 1, curSec) || (secondTick.get() < curSec && secondTick.compareAndSet(secondTick.get(), curSec))) {
                secondUsed.set(0);
            }
            // 滑动分钟级窗口
            if (minuteTick.get() != curMin) {
                synchronized (this) {
                    if (minuteTick.get() != curMin) {
                        minuteTick.set(curMin);
                        minuteUsed.set(0);
                    }
                }
            }

            if (secondUsed.get() >= perSecond) return false;
            if (minuteUsed.get() >= perMinute) return false;

            secondUsed.incrementAndGet();
            minuteUsed.incrementAndGet();
            return true;
        }
    }
}
