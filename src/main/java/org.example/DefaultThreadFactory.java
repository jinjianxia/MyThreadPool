package org.example;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:55
 * @Description:
 */
public class DefaultThreadFactory implements ThreadFactory {

    private final AtomicInteger threadCount = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "MyThreadPool-Thread-" + threadCount.getAndIncrement());
    }
}
