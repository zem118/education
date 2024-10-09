package com.education.common.cache.lock;


/**
 * Zookpeer 分布式锁

 */
public class ZookeeperDistributedLock extends AbstractDistributedLock {

    public ZookeeperDistributedLock(String lockName, long timeOut) {
        super(lockName, timeOut);
    }

    @Override
    public void release() {

    }

    @Override
    public boolean getLock() {
        return false;
    }
}
