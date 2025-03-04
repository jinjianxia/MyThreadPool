package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:08
 * @Description:
 */

public class MyThreadPool implements IThreadPool {
    private final int coreSize;
    private final int maxSize;
    private final int timeout;
    private final TimeUnit timeUnit;
    private final BlockingQueue<Runnable> blockingQueue;
    private final List<Thread> coreList = new ArrayList<>();
    private final List<Thread> supportList = new ArrayList<>();
    private final RejectHandle rejectHandle;
    private final ThreadFactory threadFactory;

    public BlockingQueue<Runnable> getBlockingQueue() {
        return blockingQueue;
    }

    public MyThreadPool(int coreSize, int maxSize, int timeout, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, RejectHandle rejectHandle, ThreadFactory threadFactory) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockingQueue = blockingQueue;
        this.rejectHandle = rejectHandle;
        this.threadFactory = threadFactory;
    }

    public void execute(Runnable command) {
        if (coreList.size() < coreSize) {
            Thread coreThread = threadFactory.newThread(new CoreThread());
            coreList.add(coreThread);
            coreThread.start();
        }
        // 核心线程数量到上限了，优先加入阻塞队列。加入阻塞队列失败后，创建辅助线程
        if (blockingQueue.offer(command)) {
            return;
        }
        if (coreList.size() + supportList.size() < maxSize) {
            Thread supportThread = threadFactory.newThread(new SupportThread());
            supportList.add(supportThread);
            supportThread.start();
        }
        if (!blockingQueue.offer(command)) {
            rejectHandle.reject(command, this);
        }
    }

    private class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable command;
                try {
                    command = blockingQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                command.run();
            }
        }
    }

    private class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable command;
                try {
                    command = blockingQueue.poll(timeout, timeUnit);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (command == null) {
                    break;
                }
                command.run();
            }
            System.out.println(Thread.currentThread().getName() + "结束了。");
        }
    }
}
