package com.education.common.cache.lock;


public class LockPermissionException extends RuntimeException {

    public LockPermissionException() {
        super();
    }

    public LockPermissionException(String message) {
        super(message);
    }

    public LockPermissionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
