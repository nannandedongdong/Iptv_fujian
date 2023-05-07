package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.ProgramObj;
import com.ccdt.ottclient.model.ZbSourceObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.ui.adapter.TvBackChannelAdapter;
import com.ccdt.ottclient.ui.adapter.TvBackProgramAdapter;
import com.ccdt.ottclient.ui.view.MyRadioButton;
import com.ccdt.ottclient.ui.view.VideoPlayerAndroidVideoView;
import com.ccdt.ottclient.utils.Utility;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 电视回看
 * Created by hanyue on 2015/11/10.
 */
public class TvBackActivity extends BaseActivity implements TaskCallback {

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, TvBackActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    private ListView lvChannel;
    private ListView lvProgramme;
    private VideoPlayerAndroidVideoView mVideoView;
    private RadioGroup mRadioGroup;
    private TextView tvChannelName;
    private TextView tvItemName;
    private TextView tvStatus;
    private TextView tvNextItem;
    private ArrayList<ChannelObj> channelData = new ArrayList<>();
    private ArrayList<ProgramObj> programmData = new ArrayList<>();
    private MyRadioButton[] weekdayButton = new MyRadioButton[7];
    private String channelId;
    private String[] date;
    private String[] week;
    private boolean isInit = false;

    private TvBackChannelAdapter channelListAdapter;
    private static int[] RADIO_BUTTON_ID = {
            R.id.tv_back_week_0,
            R.id.tv_back_week_1,
            R.id.tv_back_week_2,
            R.id.tv_back_week_3,
            R.id.tv_back_week_4,
            R.id.tv_back_week_5,
            R.id.tv_back_week_6,
    };
    private TvBackProgramAdapter programListAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_vod_tv;
    }

    @Override
    public void initWidget() {
        ifMove = false;
        Calendar calendar = Calendar.getInstance();
        Intent intent = getIntent();
        channelId = intent.getStringExtra("id");
        isInit = true;
        lvChannel = ((ListView) findViewById(R.id.lv_channel));
        lvProgramme = ((ListView) findViewById(R.id.lv_programme));
        mVideoView = ((VideoPlayerAndroidVideoView) findViewById(R.id.video_view));
        mRadioGroup = ((RadioGroup) findViewById(R.id.tv_back_weekdays));
        tvChannelName = ((TextView) findViewById(R.id.tv_channel_name));
        tvItemName = ((TextView) findViewById(R.id.tv_item_name));
        tvStatus = ((TextView) findViewById(R.id.tv_status));
        tvNextItem = ((TextView) findViewById(R.id.tv_nextitem_name));
        for (int i = 0; i < 7; i++) {
            weekdayButton[i] = (MyRadioButton) findViewById(RADIO_BUTTON_ID[i]);
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View v = findViewById(checkedId);
                String data = (String) v.getTag();
                (new GetProgramTask()).execute(channelId, data);
            }
        });
        channelListAdapter = new TvBackChannelAdapter(this, this.channelData);
        programListAdapter = new TvBackProgramAdapter(this, this.programmData);
        lvChannel.setAdapter(channelListAdapter);
        lvProgramme.setAdapter(programListAdapter);
        lvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvChannel.setSelection(position);
                channelId = channelData.get(position).getId();
                loadChannelData(channelData.get(position));
                mRadioGroup.check(RADIO_BUTTON_ID[RADIO_BUTTON_ID.length - 1]);
            }
        });
        lvProgramme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProgramObj programObj = programmData.get(position);

                String address = programObj.backAddress;
                if (!TextUtils.isEmpty(address)) {
                    try {
                        JSONArray object = new JSONArray(address);
                        Utility.intoPlayerActivity(TvBackActivity.this, programObj.epgName, object.getJSONObject(0).optString("bfUrl"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        initOperator();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                if(ifMove){
                    // 时移
                    mp.seekTo(mVideoView.getDuration()-20000);
                }
//
                mVideoView.start();

                //7241盒子
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        return false;
                    }
                });
            }
        });
//        (new GetChannelListTask()).execute();
        requestDataChannel();

    }


    // 取频道列表
    private void requestDataChannel() {
        Map<String, String> parms = new HashMap<>();
        parms.put(ConstantKey.ROAD_TYPE_LMID, MyApp.getInstance().getOneLevelLmInfoByType(MyApp.LM_ONELEVEL_TYPE_ZB).getLmId());
        parms.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "sourceType,channelId,mzName,mzId,logoUrl,zbSource,endTime,recordTime,ifBack,id,startTime,moveTime,epgName,ifMove,collectUids");
        parms.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        new com.ccdt.ottclient.tasks.impl.GetChannelListTask(this).execute(parms);
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETCHANNELLIST:
                    channelData.clear();
                    if (result.code == TaskResult.SUCCESS) {
                        CommonSearchInvokeResult<ChannelObj> invokeResult = (CommonSearchInvokeResult<ChannelObj>) result.data;
                        if (invokeResult != null) {
                            channelData.addAll(invokeResult.getRtList());
                            channelListAdapter.notifyDataSetChanged();
                            if (channelData.size() > 0) {
                                if (TextUtils.isEmpty(channelId)) {
                                    loadChannelData(channelData.get(0));
                                } else {
                                    for (ChannelObj obj : channelData) {
                                        if (obj != null) {
                                            if (channelId.equals(obj.getId())) {
                                                loadChannelData(obj);
                                            }
                                        }
                                    }
                                    channelId = "";
                                }
                            }
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        mVideoView.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    private void initOperator() {
        // 计算日期 初始化日期界面
        date = new String[7];
        week = new String[7];
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd EEEE");
        long current = System.currentTimeMillis();
        String tempString;
        String[] tempStringGroup;
        for (int i = 0; i < 7; i++) {
            tempString = format.format(new Date(current - (7 - 1 - i) * 24 * 60 * 60 * 1000));
            tempStringGroup = tempString.split(" ");
            date[i] = tempStringGroup[0];
            week[i] = tempStringGroup[1];
            if (week[i].equals("Sunday") || week[i].equals("星期日")) {
                weekdayButton[i].setWeekText("星期日");
            } else if (week[i].equals("Monday") || week[i].equals("星期一")) {
                weekdayButton[i].setWeekText("星期一");
            } else if (week[i].equals("Tuesday") || week[i].equals("星期二")) {
                weekdayButton[i].setWeekText("星期二");
            } else if (week[i].equals("Wednesday") || week[i].equals("星期三")) {
                weekdayButton[i].setWeekText("星期三");
            } else if (week[i].equals("Thursday") || week[i].equals("星期四")) {
                weekdayButton[i].setWeekText("星期四");
            } else if (week[i].equals("Friday") || week[i].equals("星期五")) {
                weekdayButton[i].setWeekText("星期五");
            } else if (week[i].equals("Saturday") || week[i].equals("星期六")) {
                weekdayButton[i].setWeekText("星期六");
            }
            weekdayButton[i].setText("\n" + date[i]);
            weekdayButton[i].setTag(date[i]);
            if (i == 6) {
                weekdayButton[i].setChecked(true);

            }
        }
    }

    private class GetProgramTask extends AsyncTask<String, Void, CommonSearchInvokeResult<ProgramObj>> {

        @Override
        protected CommonSearchInvokeResult<ProgramObj> doInBackground(String... params) {
            Map<String, String> parms = new HashMap<>();
            parms.put(ConstantKey.ROAD_TYPE_ZBMZID, params[0]);
            parms.put(ConstantKey.ROAD_TYPE_PAGESIZE, "200");
            parms.put(ConstantKey.ROAD_TYPE_DATE, params[1]);
            try {
                return OTTServiceApi.getInstance().getLiveEpgsSearchResult(parms);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof SuperException) {
                    Log.i(TAG, ((SuperException) e).getErrorCode() + ((SuperException) e).getErrorDesc());
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonSearchInvokeResult<ProgramObj> result) {
            if (result != null) {
                programmData.clear();
                programmData.addAll(result.getRtList());
                programListAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(result);
        }
    }


    private boolean ifMove = false;

    private void loadChannelData(ChannelObj info) {
        if (info == null) {
            return;
        }
        channelId = info.getId();
        ifMove = "1".equals(info.getIfMove());
        String mzName = TextUtils.isEmpty(info.getMzName())?"无":info.getMzName();
        String epgName = TextUtils.isEmpty(info.getEpgName())?"无":info.getEpgName();
        tvChannelName.setText("频道：" + mzName);
        tvItemName.setText("节目：" + epgName);
        if (info.getZbSource().size() != 0) {
            tvStatus.setText("状态：直播");
        }

        String playUrl = null;
        if (info.getZbSource() != null) {
            if (info.getZbSource().size() > 0) {
                ZbSourceObj zbSourceObj = null;
                for (int i = 0; i < info.getZbSource().size(); i++) {
                    if (info.getZbSource().get(i).getType().equals("HD")) {
                        zbSourceObj = info.getZbSource().get(i);
                        break;
                    } else if (info.getZbSource().get(i).getType().equals("SD")) {
                        zbSourceObj = info.getZbSource().get(i);
                    }
                }
                if (zbSourceObj != null) {
                    if (ifMove) {
                        playUrl = zbSourceObj.getMoveUrl();
                    } else {
                        playUrl = zbSourceObj.getLiveUrl();
                    }
                }
            }
        }
//        playUrl = "http://12.0.2.100:15414//live/CHANNELaf175fdf06bd4e39a5c9ab0bc1a32f007316865667629538318/live.m3u8";
        if (playUrl != null) {
            mVideoView.setVideoPath(playUrl);
        }

        (new GetProgramTask()).execute(channelId, date[6]);
    }

}


