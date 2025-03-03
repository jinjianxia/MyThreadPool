package org.example;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:19
 * @Description:
 */
public class DiscardRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable command, MyThreadPool threadPool) {
        threadPool.getBlockingQueue().poll();
        threadPool.execute(command);
    }
}
