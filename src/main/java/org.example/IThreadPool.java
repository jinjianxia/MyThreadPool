package org.example;

/**
 * @Author: jinjianxia
 * @CreateTime: 2025/3/3 14:59
 * @Description:
 */
public interface IThreadPool {
    void execute(Runnable command);
}
