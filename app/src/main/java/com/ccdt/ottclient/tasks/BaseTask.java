package com.ccdt.ottclient.tasks;

import android.os.AsyncTask;

/**
 * BaseTask 抽象的异步任务
 */
public abstract class BaseTask<T> extends AsyncTask<T, Integer, TaskResult> {

    private TaskCallback callback;

    public BaseTask(TaskCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPostExecute(TaskResult taskResult) {
        if (callback != null) {
            callback.onTaskFinished(taskResult);
        }
    }
}
