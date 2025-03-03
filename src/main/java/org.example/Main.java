package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:07
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(2,
                4,
                2,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new DiscardRejectHandle());
        for (int i = 0; i < 5; i++) {
            final int t = i;
            myThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " i = " + t);
            });
        }
        System.out.println("主线程正常");
    }
}
