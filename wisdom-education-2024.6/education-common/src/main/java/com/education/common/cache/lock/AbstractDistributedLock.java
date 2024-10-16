package com.education.common.cache.lock;

/**
 *   
 *   
 *  
 */
public abstract class AbstractDistributedLock {

    protected String lockName;
    protected long timeOut = 0; // 获取锁的延迟时间


    public AbstractDistributedLock(String lockName, long timeOut) {
        this.lockName = lockName;
        this.timeOut = timeOut;
    }

    public AbstractDistributedLock(String lockName) {
        this.lockName = lockName;
    }

    public String getLockName() {
        return lockName;
    }

    public long getTimeOut() {
        return timeOut;
    }

    /**
     * 释放锁
     */
    abstract void release ();

    /**
     * 是否获得锁
     * @return
     */
    abstract boolean getLock();
}
