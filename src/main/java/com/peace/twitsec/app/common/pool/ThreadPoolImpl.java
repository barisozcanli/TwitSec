package com.peace.twitsec.app.common.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolImpl {

    private static final Logger logger        = LoggerFactory.getLogger(ThreadPoolImpl.class);

    private long                               keepAliveTime = 20;

    private ThreadPoolExecutor                 threadPool    = null;

    private final ArrayBlockingQueue<Runnable> queue         = new ArrayBlockingQueue<Runnable>(10000);
    
    private AtomicInteger                      threadsCount  = new AtomicInteger(0);
    
    public int getTaskCount() {
        return threadPool.getActiveCount();
    }

    public ThreadPoolImpl(int corePoolSize, int maxPoolSize) {

        threadPool = new ThreadPoolExecutor(corePoolSize,
                                            maxPoolSize,
                                            keepAliveTime,
                                            TimeUnit.SECONDS,
                                            queue,

                                            new ThreadFactory() {

                                                public Thread newThread(Runnable r) {
                                                    final Thread thread = new Thread(r);
                                                    thread.setName("net_worker_"
                                                            + threadsCount.incrementAndGet());
                                                    return thread;
                                                }
                                            },

                                            new RejectedExecutionHandler() {

                                                public void rejectedExecution(
                                                                              Runnable r,
                                                                              ThreadPoolExecutor executor) {

                                                    logger.error("new task " + r + " is discarded");
                                                }
                                            });
    }

    public void runTask(Runnable task) {
        threadPool.execute(task);
    }

    public void shutDown() {
        threadPool.shutdownNow();
        try {
            threadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Workers-pool termination failed", e);
        }

    }
}
