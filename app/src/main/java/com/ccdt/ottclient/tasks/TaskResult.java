package com.ccdt.ottclient.tasks;

/**
 * 异步任务结果的抽象的描述，用于区分结果属于哪一个Task
 */
public class TaskResult {

    public static final int SUCCESS = 200;

    /**
     * 异步任务的标示
     */
    public int taskId;

    /**
     * 异步任务内部获取的数据
     */
    public Object data;

    /**
     * 返回码
     */
    public int code;
    /**
     * 返回信息
     */
    public String message;


}
