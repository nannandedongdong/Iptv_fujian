package com.ccdt.ottclient.tasks.impl;

import android.content.Context;
import android.database.Cursor;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.model.PlayHistoryObj;
import com.ccdt.ottclient.provider.SQLDataBase;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetPlayHistoryTask extends BaseTask<Context> {
    public GetPlayHistoryTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Context... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETPLAYHISTORY;
        List<PlayHistoryObj> dataSet = new ArrayList<>();
        Cursor cursor = params[0].getContentResolver().query(
                SQLDataBase.Enum.CONTENT_URI_DIR,
                SQLDataBase.enumName,
                SQLDataBase.Enum.TYPE + "=? or " + SQLDataBase.Enum.TYPE + "=?",
                new String[]{"" + SQLDataItemModel.TYPE_MAKING, "" + SQLDataItemModel.TYPE_MOVE},
                SQLDataBase.addSortOrder(SQLDataBase.Enum.TIME, false));
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PlayHistoryObj obj = new PlayHistoryObj();
                obj.setType(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.TYPE)));
                obj.setMzId(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.MZID)));
                obj.setTitle(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.TITLE)));
                obj.setPosterUrl(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.POSTERURL)));
                obj.setPlayUrl(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.PLAYURL)));
                obj.setPlayPosition(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.PLAYPOSITION)));
                obj.setSeriesPosition(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.SERIESPOSITION)));
                obj.setTime(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.TIME)));
                dataSet.add(obj);
            }
            cursor.close();
        }
        ret.data = dataSet;
        ret.code = 200;

        return ret;
    }
}
