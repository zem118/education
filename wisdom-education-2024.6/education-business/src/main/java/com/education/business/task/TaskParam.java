package com.education.business.task;

import com.education.common.model.ModelBeanMap;


public class TaskParam extends ModelBeanMap {

    private Class<? extends TaskListener> taskListenerClass;
    private final long timestamp;

    public long getTimestamp() {
        return timestamp;
    }


    public TaskParam(Class<? extends TaskListener> taskListenerClass) {
        this(taskListenerClass, null);
    }

    public TaskParam(Class<? extends TaskListener> taskListenerClass, long timestamp) {
        this.timestamp = timestamp;
        this.taskListenerClass = taskListenerClass;
    }

    public TaskParam(Class<? extends TaskListener> taskListenerClass, Object data) {
        this(taskListenerClass, System.currentTimeMillis());
    }

    public Class<? extends TaskListener> getTaskListenerClass() {
        return taskListenerClass;
    }
}
