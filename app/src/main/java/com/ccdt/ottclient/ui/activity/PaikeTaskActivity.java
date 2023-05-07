package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.PaikeTaskInfo;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetPaikeTaskListTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaikeTaskActivity extends BaseActivity implements TaskCallback {

    private LmInfoObj mLmInfo;
    private TaskAdapter adapter;
    private CommonSearchInvokeResult<PaikeTaskInfo> invokeResult;
    private List<PaikeTaskInfo> mTaskList = new ArrayList<>();
    private ListView lvTask;
    private Handler mHandle = new Handler();
    private TextView txtNum;

    public static void actionStart(Context context, LmInfoObj lmInfoObj) {
        Intent intent = new Intent(context, PaikeTaskActivity.class);
        intent.putExtra("lmInfo", lmInfoObj);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLmInfo = intent.getParcelableExtra("lmInfo");
        if (mLmInfo != null) {
            Map<String, String> map = new HashMap<>();
            map.put(ConstantKey.ROAD_TYPE_LMID, mLmInfo.getLmId());
            map.put(ConstantKey.ROAD_MY_PAGE_NUMBER, "2000");
            new GetPaikeTaskListTask(this).execute(map);
        }

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_paike_task;
    }

    @Override
    protected void initWidget() {
        lvTask = ((ListView) findViewById(R.id.lv_task));
        txtNum = ((TextView) findViewById(R.id.txtNum));
        adapter = new TaskAdapter();
        lvTask.setAdapter(adapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               PaikeTaskInfo info =  mTaskList.get(position);
                PaikeTaskDetailActivity.actionStart(PaikeTaskActivity.this,info.getJobName(),info.getId());
            }
        });
        lvTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Runnable runnable =new Runnable() {
                        @Override
                        public void run() {
                            lvTask.setSelection(0);
                        }
                    };
                    mHandle.post(runnable);
                }
            }
        });
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETPAIKETASKLIST:
                    mTaskList.clear();
                    invokeResult = (CommonSearchInvokeResult<PaikeTaskInfo>) result.data;
                    if (invokeResult != null && invokeResult.getRtList() != null) {
                        mTaskList.addAll(invokeResult.getRtList());
                    }
                    txtNum.setText(String.valueOf(mTaskList.size()));
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }


    private class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTaskList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTaskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            ViewHodler hodler = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_paike_task, null);
                hodler = new ViewHodler();
                hodler.tvDesc = ((TextView) convertView.findViewById(R.id.tv_desc));
                hodler.tvPaikeTime = (TextView)convertView.findViewById(R.id.tv_time);
                hodler.tvPaikeTitle = (TextView)convertView.findViewById(R.id.tv_title);
                hodler.image = (ImageView)convertView.findViewById(R.id.img_task);
            }else {
                hodler = (ViewHodler)convertView.getTag();
            }
            PaikeTaskInfo info = mTaskList.get(position);
            hodler.tvPaikeTitle.setText(info.getJobName());
            hodler.tvDesc.setText(info.getJobDesc());
            hodler.tvPaikeTime.setText(info.getUpdateTime());
            ImageLoader.getInstance().displayImage(info.getBannerImg(), hodler.image);
            return convertView;
        }
    }
    private class ViewHodler {
        TextView tvPaikeTime;
        TextView tvDesc;
        TextView tvPaikeTitle;
        ImageView image;
    }
}
