package com.eveb.saasops.batch.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class TheadPoolExecutor {

    /***线程池维护线程的最少数量**/
    private final int minSize = 6;
    /***允许的空闲时间**/
    private final int aliveSeconds = 300;
    /***线程池维护线程的最大数量**/
    private final int maxSize=6;
    private final int bbinMaxSize=20;
    /***缓存队列**/
    private final int queueCapacity = 5000;
    private final int bbinQueueCapacity = 9999;

    @Bean
    public Executor bbinAsyncExecutor()
    {
        ThreadPoolTaskExecutor pool=new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(minSize);//线程池维护线程的最少数量
        pool.setKeepAliveSeconds(aliveSeconds);//允许的空闲时间
        pool.setMaxPoolSize(bbinMaxSize);//线程池维护线程的最大数量
        pool.setQueueCapacity(bbinQueueCapacity);//缓存队列
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public Executor ptAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(minSize);//线程池维护线程的最少数量
        pool.setKeepAliveSeconds(aliveSeconds);//允许的空闲时间
        pool.setMaxPoolSize(maxSize);//线程池维护线程的最大数量
        pool.setQueueCapacity(queueCapacity);//缓存队列
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public Executor agAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize * 6, queueCapacity);
    }

    @Bean
    public Executor mgAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor rptAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor pt2AsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor ntAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor pngAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor ibcAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor t188AsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor egAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor opusAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor pbAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor financialCountAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(minSize);//线程池维护线程的最少数量
        pool.setKeepAliveSeconds(aliveSeconds);//允许的空闲时间
        pool.setMaxPoolSize(maxSize * 6);//线程池维护线程的最大数量
        pool.setQueueCapacity(queueCapacity);//缓存队列
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public Executor ThirdPayAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(minSize);//线程池维护线程的最少数量
        pool.setKeepAliveSeconds(aliveSeconds);//允许的空闲时间
        pool.setMaxPoolSize(maxSize * 6);//线程池维护线程的最大数量
        pool.setQueueCapacity(queueCapacity);//缓存队列
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }


    @Bean
    public Executor merchantPaymentAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize * 6, queueCapacity);
    }

    @Bean
    public Executor validBetActivityAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize * 6, queueCapacity);
    }

    @Bean
    public Executor tagLoginTimeAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize * 6, queueCapacity);
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int minSize, int aliveSeconds, int maxSize, int queueCapacity) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(minSize);//线程池维护线程的最少数量
        pool.setKeepAliveSeconds(aliveSeconds);//允许的空闲时间
        pool.setMaxPoolSize(maxSize);//线程池维护线程的最大数量
        pool.setQueueCapacity(queueCapacity);//缓存队列
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public Executor mg2Progressor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }


    @Bean
    public Executor bngProgressor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor abProgressor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor n2AsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor hgAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor gnsAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor fgAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }
    @Bean
    public Executor vrAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor pgcbAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor ggcbAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }

    @Bean
    public Executor bgAsyncExecutor() {
        return getThreadPoolTaskExecutor(minSize, aliveSeconds, maxSize, queueCapacity);
    }
}
