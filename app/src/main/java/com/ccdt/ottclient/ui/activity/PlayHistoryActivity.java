package com.ccdt.ottclient.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.PlayHistoryObj;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.provider.SQLDataOperationMethod;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetPlayHistoryTask;
import com.ccdt.ottclient.ui.adapter.PlayHistoryAdapter;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.ToastUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;


/**
 * 播放记录
 */

public class PlayHistoryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TaskCallback {

    private static final String TAG = PlayHistoryActivity.class.getSimpleName();
    private PlayHistoryAdapter adapter;
//    private AlertDialog.Builder builder;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlayHistoryActivity.class);
        context.startActivity(intent);
    }

    private FocusedGridView mGrid;
    private Context mContext;
    private TextView mTitle;
    private TextView mCount;
    private List<PlayHistoryObj> mDataSet = new ArrayList<>();
    private int selectedId;
    private PlayHistoryAdapter.ViewHolder focusedHolder;
    private int colorTextUnselected;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        colorTextUnselected = this.getResources().getColor(R.color.text_unselected_vod);
        mTitle.setText(R.string.play_history);
        requestData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_secondary_grid;
    }

    @Override
    protected void initWidget() {
        mTitle = (TextView) findViewById(R.id.txtTitle);
        mCount = (TextView) findViewById(R.id.txtNum);
//        builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        dialog = new Dialog(this, R.style.CustomDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_play_history, null);
        View btnDelOne = dialogView.findViewById(R.id.btnDelOne);
        View btnDelAll = dialogView.findViewById(R.id.btnDelAll);
        btnDelOne.setOnClickListener(this);
        btnDelAll.setOnClickListener(this);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
//        dialog = builder.create();
        initGrid();
    }

    private void initGrid() {
        adapter = new PlayHistoryAdapter(this, mDataSet);
        mGrid = ((FocusedGridView) findViewById(R.id.grid));
        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_root);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(this);
//        mGrid.setOnItemLongClickListener(this);

        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if (paramBoolean) {
                    if (paramView1 != null) {
                        Object tag = paramView1.getTag();
                        if (tag != null) {
                            if (tag instanceof PlayHistoryAdapter.ViewHolder) {
                                focusedHolder = (PlayHistoryAdapter.ViewHolder) tag;
                                focusedHolder.txtTitle.setTextColor(Color.WHITE);
                                focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_blue);
                            }
                        }
                    }
                } else {
                    if (focusedHolder != null) {
                        focusedHolder.txtTitle.setTextColor(colorTextUnselected);
                        focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_black);
                    }
                    focusedHolder = null;
                }
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();

    }

    private void requestData(){
        new GetPlayHistoryTask(this).execute(this);
    }

//
//private void DataLoader() {
//        mDataSet.clear();
//        Cursor cursor = getContentResolver().query(
//                SQLDataBase.Enum.CONTENT_URI_DIR,
//                SQLDataBase.enumName,
//                SQLDataBase.Enum.TYPE + "=? or " + SQLDataBase.Enum.TYPE + "=?",
//                new String[]{"" + SQLDataItemModel.TYPE_MAKING, "" + SQLDataItemModel.TYPE_MOVE},
//                SQLDataBase.addSortOrder(SQLDataBase.Enum.TIME, false));
//        String count = "0";
//        if (cursor != null) {
//            count = String.valueOf(cursor.getCount());
//            while (cursor.moveToNext()) {
//                PlayHistoryObj obj = new PlayHistoryObj();
//                obj.setType(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.TYPE)));
//                obj.setMzId(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.MZID)));
//                obj.setTitle(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.TITLE)));
//                obj.setPosterUrl(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.POSTERURL)));
//                obj.setPlayUrl(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.PLAYURL)));
//                obj.setPlayPosition(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.PLAYPOSITION)));
//                obj.setSeriesPosition(cursor.getInt(cursor.getColumnIndex(SQLDataBase.Enum.SERIESPOSITION)));
//                obj.setTime(cursor.getString(cursor.getColumnIndex(SQLDataBase.Enum.TIME)));
//                mDataSet.add(obj);
//            }
//            cursor.close();
//        }
//        mCount.setText(count);
//        adapter.notifyDataSetChanged();
//
//    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int result = -1;
        switch (id) {
            case R.id.btnDelOne:
                PlayHistoryObj playHistoryObj = mDataSet.get(selectedId);

                if (playHistoryObj != null) {
                    result = SQLDataOperationMethod.deleteData(mContext, playHistoryObj.getType(), playHistoryObj.getMzId());
                }
                break;
            case R.id.btnDelAll:
                result = SQLDataOperationMethod.deleteData(this);
//                result = SQLDataOperationMethod.deleteData(this, SQLDataItemModel.TYPE_MOVE);
                break;
            default:
                break;
        }
        if (result > 0) {
            ToastUtil.toast("删除成功");
        } else {
            ToastUtil.toast("删除失败");
        }
        dialog.dismiss();
        requestData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            this.selectedId = -1;
            this.selectedId = mGrid.getSelectedItemPosition();

            LogUtil.d("selectedId" , ""+selectedId);
            if(this.selectedId > -1){
                dialog.show();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlayHistoryObj playHistory = mDataSet.get(position);
        if (playHistory != null) {
            if (SQLDataItemModel.TYPE_MAKING == playHistory.getType()) {
                SQLDataItemModel sqlDataItemModel = new SQLDataItemModel();
                sqlDataItemModel.setTitle(playHistory.getTitle());
                sqlDataItemModel.setPlayUrl(playHistory.getPlayUrl());
                sqlDataItemModel.setPosterUrl(playHistory.getPosterUrl());
                sqlDataItemModel.setMzId(playHistory.getMzId());
                sqlDataItemModel.setType(playHistory.getType());
                sqlDataItemModel.setPlayPosition(playHistory.getPlayPosition());
                Utility.intoPlayerActivity(mContext, sqlDataItemModel);
            } else {
                Utility.intoVODDetailActivity(mContext, playHistory.getMzId());
            }
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {

        if (result != null) {
            switch (result.taskId){
                case Constants.TASK_GETPLAYHISTORY:
                    String count = "0";
                    mDataSet.clear();
                    if (result.data != null) {
                        List<PlayHistoryObj> data = (List<PlayHistoryObj>)result.data;
                        mDataSet.addAll(data);
                        count = String.valueOf(mDataSet.size());
                    }
                    mCount.setText(count);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }



        }
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        this.selectedId = position;
//        builder.show();
//        return true;
//    }
}
