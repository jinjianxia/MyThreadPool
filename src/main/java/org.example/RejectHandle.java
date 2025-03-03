package org.example;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:18
 * @Description:
 */
public interface RejectHandle {
    void reject(Runnable command, MyThreadPool threadPool);
}
