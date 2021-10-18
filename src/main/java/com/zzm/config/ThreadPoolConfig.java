package com.zzm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${waifu2x.input}")
    private File inputDir;

    @Value("${waifu2x.output}")
    private File outputDir;

    @PostConstruct
    private void init() {
        makeWaifu2xWorkingDirectory();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数
        int corePoolSize = 5;
        executor.setCorePoolSize(corePoolSize);

        // 设置最大线程数
        int maxPoolSize = 5;
        executor.setMaxPoolSize(maxPoolSize);

        // 设置队列容量
        int queueCapacity = 15;
        executor.setQueueCapacity(queueCapacity);

        // 设置允许的空闲时间（秒）
        int keepAlive = 100;
        executor.setKeepAliveSeconds(keepAlive);

        // 设置默认线程名称
        executor.setThreadNamePrefix("bact_thread-");

        // 设置拒绝策略rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * Ensure waifu2x's working directory(input, output) is created.
     */
    private void makeWaifu2xWorkingDirectory() {
        createDirectoryIfNotExists(inputDir);
        createDirectoryIfNotExists(outputDir);
    }

    private static void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalArgumentException("Cannot create waifu2x's input directory on: " + directory.getAbsolutePath());
            }
        }
    }

}