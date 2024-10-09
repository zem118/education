package com.education.business.task;

import com.education.common.component.SpringBeanManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;



public class TaskManager {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public TaskManager(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void pushTask(TaskParam taskParam) {
        TaskListener taskListener = SpringBeanManager.getBean(taskParam.getTaskListenerClass());
        if (taskListener != null) {
            threadPoolTaskExecutor.execute(() -> {
                taskListener.onMessage(taskParam);
            });
        }
    }

    public void pushTaskByNewInstance(TaskParam taskParam) {
        Class<? extends TaskListener> taskListenerClass = taskParam.getTaskListenerClass();
        if (taskListenerClass != null) {
            try {
                TaskListener taskListener = taskListenerClass.newInstance();
                threadPoolTaskExecutor.execute(() -> {
                    taskListener.onMessage(taskParam);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
