package com.peace.twitsec.app.common.pool;

public enum TwitSecThreadPool {

    EMAIL_POOL(10, 10);

    private ThreadPoolImpl instance;

    private TwitSecThreadPool(int corePoolSize, int maxPoolSize) {
        this.instance = new ThreadPoolImpl(corePoolSize, maxPoolSize);
    }
    
    public void runTask(Runnable task) {
        instance.runTask(task);
    }
    
    public void shutdown() {
        instance.shutDown();
    }
}
