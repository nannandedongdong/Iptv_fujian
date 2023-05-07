package com.ccdt.ottclient.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.JuJiInfo;
import com.ccdt.ottclient.model.VisitAddress;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.provider.SQLDataOperationMethod;
import com.ccdt.ottclient.ui.adapter.VODPlayerMenuAdapter;
import com.ccdt.ottclient.ui.view.ControlerPopupWindow;
import com.ccdt.ottclient.ui.view.VideoPlayerAndroidVideoView;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import pullscreen.PullScreenBinder;
import pullscreen.PullScreenServiceSTB;
import pullscreen.SendPacketThread;

/**
 * @ClassName: VODPlayerActivity
 * @Description: 点播播放器页面
 */

public class VODPlayerActivity extends Activity implements DialogInterface.OnClickListener, ControlerPopupWindow.OnRateChangeListener, AdapterView.OnItemClickListener {

    private static final String TAG = "hezb";

    private static final int HIDE_INTERVAL = 5000;//隐藏控制View时间间隔
    private static final int UPDATE_INTERVAL = 1000;//更新进度条时间间隔

    private Context mContext;
    private VideoPlayerAndroidVideoView mVideoView;
    private View mLoadingView;
    private TextView mLoadingInfo;
    private View mControllerView;
    private View mButtomView;
    private TextView mTitle, mCurrentTime, mTotalTime;
    private SeekBar mSeekBar;
    private ImageView mCenterStatus;
    private ListView lvMenu;

    private Handler mHandler = new Handler();

    private String playUrl;
    private String title;

    private int currentPosition = 0;
    private boolean isPlayComplete = false;
    private boolean isPlayError = false;
    private boolean needUpdataSeekBar = true;

    private boolean waitExit = true;

    private int multiple = 1;

    private List<JuJiInfo> juJiInfos;
    private int seriesPosition = 0;// 1 到 N 集
    private int juJiListIndex = 0;// 剧集所在列表位置，解决剧集不连贯问题
    private boolean isFilm = true;// 默认是电影

    private ControlerPopupWindow popupWindow;
    private SQLDataItemModel sqlDataItemModel;
    private AlertDialog.Builder builder;
    private int totalDuration;
    private RelativeLayout mMenuLayout;

    private SeekBar volumeBar;
    private int volumeValue;
    private View layoutVideo;
    private int maxVolume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_player);
        mContext = this;

        ////////////////////////////////////////////

        /////////////////////////////////////////////
        builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.network_disconnected));
        builder.setPositiveButton(getResources().getString(R.string.sure), this);

        Bundle isSeriesInfo = getIntent().getBundleExtra("isSeriesInfo");

        /////////////////////////////////////////////

        if (getIntent().getBundleExtra("videoInfo") != null) { // 回看
            Bundle bundle = getIntent().getBundleExtra("videoInfo");
            playUrl = bundle.getString("playUrl");
            title = bundle.getString("title");

        } else if (getIntent().getSerializableExtra("SQLDataItemModel") != null) { // 拍客
            sqlDataItemModel = (SQLDataItemModel) getIntent()
                    .getSerializableExtra("SQLDataItemModel");
            playUrl = sqlDataItemModel.getPlayUrl();
            title = sqlDataItemModel.getTitle();
            if (TextUtils.isEmpty(sqlDataItemModel.getMzId())) {
                sqlDataItemModel = null;
            } else {
                currentPosition = (int) sqlDataItemModel.getPlayPosition();
            }
        } else if (getIntent().getBundleExtra("isSeriesInfo") != null) { // 电影 电视剧
            Bundle seriesBundle = getIntent().getBundleExtra("isSeriesInfo");
            juJiInfos = seriesBundle.getParcelableArrayList("JujiInfos");// 外部传参需保证此参数不能为空！
            seriesPosition = seriesBundle.getInt("seriesPosition", 0);
            title = seriesBundle.getString("title");
            String posterUrl = seriesBundle.getString("posterUrl");
            String mzId = seriesBundle.getString("mzId");

            if (!TextUtils.isEmpty(mzId) && seriesPosition == 0) { // 查历史记录
                seriesPosition = SQLDataOperationMethod.searchSeriesPosition(this, mzId);
                currentPosition = SQLDataOperationMethod.searchPosition(this,
                        SQLDataItemModel.TYPE_MOVE, mzId);
            }

            sqlDataItemModel = new SQLDataItemModel();// 用于存历史记录的对象
            sqlDataItemModel.setTitle(title);
            sqlDataItemModel.setPosterUrl(posterUrl);
            sqlDataItemModel.setMzId(mzId);
            sqlDataItemModel.setType(SQLDataItemModel.TYPE_MOVE);

            popupWindow = new ControlerPopupWindow(this);// 多码率切换弹出窗
            popupWindow.setOnRateChangeListener(this);

            if (seriesPosition == 0 && juJiInfos.size() == 1) { // 电影
                isFilm = true;
                List<VisitAddress> list = juJiInfos.get(0).getVisitAddress();
                dataSet.clear();
                dataSet.addAll(list);
                if (list != null && list.size() != 0) {
                    playUrl = list.get(0).getBfUrl();
                    popupWindow.setRateList(list);
                } else {
                    Utility.showToast(this, getString(R.string.play_url_error));
                }
            } else { // 电视剧
                isFilm = false;
                boolean findCurrentSeries = false;
                for (int i = 0; i < juJiInfos.size(); i++) {
                    if (seriesPosition == juJiInfos.get(i).juJiNum) {
                        List<VisitAddress> list = juJiInfos.get(i)
                                .getVisitAddress();
                        dataSet.clear();
                        dataSet.addAll(list);
                        if (list != null && list.size() != 0) {
                            playUrl = list.get(0).getBfUrl();
                            popupWindow.setRateList(list);
                        } else {
                            Utility.showToast(this, getString(R.string.play_url_error));
                        }
                        findCurrentSeries = true;
                        juJiListIndex = i;
                        break;
                    }
                }
                if (!findCurrentSeries) {//错误情况，没找到剧集所在列表位置
                    Log.e(TAG, "can not find current series: " + seriesPosition);
                    List<VisitAddress> list = juJiInfos.get(0)
                            .getVisitAddress();
                    dataSet.clear();
                    dataSet.addAll(list);
                    if (list != null && list.size() != 0) {
                        playUrl = list.get(0).getBfUrl();
                        popupWindow.setRateList(list);
                    } else {
                        Utility.showToast(this, getString(R.string.play_url_error));
                    }
                }
            }
        }

        mFindViewById();

        initOperator();
    }

    private List<VisitAddress> dataSet = new ArrayList<>();

    private void mFindViewById() {
        mVideoView = (VideoPlayerAndroidVideoView) findViewById(R.id.android_videoview);
        mLoadingView = findViewById(R.id.video_loading);
        mLoadingInfo = (TextView) findViewById(R.id.loading_text);
        mControllerView = findViewById(R.id.video_controller_layout);
        mButtomView = findViewById(R.id.video_bottom_layout);
        mTitle = (TextView) findViewById(R.id.video_title);
        mCurrentTime = (TextView) findViewById(R.id.video_current_time);
        mTotalTime = (TextView) findViewById(R.id.video_total_time);
        mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        mCenterStatus = (ImageView) findViewById(R.id.video_center_status);
        lvMenu = (ListView) findViewById(R.id.lv_menu);
        mMenuLayout = ((RelativeLayout) findViewById(R.id.layout_menu));
        volumeBar = ((SeekBar) findViewById(R.id.bar_volume));
//        layoutVideo = findViewById(R.id.layout_video);
        final AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeBar.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        volumeValue = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        volumeBar.setProgress(volumeValue);
        volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                volumeValue = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                volumeBar.setProgress(volumeValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View header = getLayoutInflater().inflate(R.layout.header_vod_menu, null);
        lvMenu.addHeaderView(header, null, false);
        lvMenu.setAdapter(new VODPlayerMenuAdapter(this, dataSet));
        lvMenu.setOnItemClickListener(this);
        lvMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static final int SCALE_16_9 = 0;
    private static final int SCALE_4_3 = 1;

    public void setScle(int scaleFlag) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        switch (scaleFlag) {
            case SCALE_16_9:
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case SCALE_4_3:
//                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = (int) (MyApp.width * 0.75);
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                break;
            default:
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
        LogUtil.d("slf " + TAG, "setScle");
        mVideoView.setLayoutParams(layoutParams);
        mVideoView.invalidate();
    }


    private void initOperator() {

        initVideoView();

        initSeekBar();

        initOther();

        startPlay();

        isBind = bindService(new Intent(this, PullScreenServiceSTB.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @SuppressLint("NewApi")
    private void initVideoView() {

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);
                onVideoPlay();
                if (currentPosition != 0) {
                    if (currentPosition > totalDuration) {
                        // 播放时间超过总时间，播放时间重置
                        currentPosition = 0;
                    }
                    mVideoView.seekTo(currentPosition);
                }
                mLoadingView.setVisibility(View.GONE);
//                //7241盒子
//                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//
//
//                    @Override
//                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                        if (mLoadingView != null) {
//                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                                mLoadingView.setVisibility(View.VISIBLE);
//                            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                                if (mp.isPlaying()) {
//                                    mLoadingView.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                        return false;
//                    }
//                });
                mTotalTime.setText(Utility.generateTime((int) mVideoView.getDuration()));
            }


        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isFilm && juJiInfos != null && juJiListIndex < juJiInfos.size()) {
                    juJiListIndex++;
                    if (juJiListIndex < juJiInfos.size()) {
                        seriesPosition = juJiInfos.get(juJiListIndex).juJiNum;
                        initOther();
                        List<VisitAddress> list = juJiInfos.get(juJiListIndex)
                                .getVisitAddress();
                        popupWindow.setRateList(list);
                        if (list != null && list.size() != 0) {
                            playUrl = list.get(0).getBfUrl();
                            currentPosition = 0;
                            startPlay();
                            Utility.showToast(mContext, getString(R.string.play_complete_goto_next));
                        } else {
                            Utility.showToast(mContext, getString(R.string.play_url_error));
                        }
                        return;
                    }
                }
                mCenterStatus.setImageResource(R.drawable.img_text_detail_play_icon_unfocus);
                mCenterStatus.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(updateSeekBarThread);
                mCurrentTime.setText(Utility.generateTime(
                        (int) mVideoView.getDuration()));
                mSeekBar.setProgress(mSeekBar.getMax());
                isPlayComplete = true;
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPlayError = true;
//                Utility.showToast(VODPlayerActivity.this, getString(R.string.player_error_play_complete));
                builder.show();
                return true;
            }
        });

        mVideoView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mControllerView.getVisibility() == View.VISIBLE) {
                    mHandler.post(hiddenViewThread);
                } else {
                    showControllerBar();
                }

                return false;
            }
        });
        //7241 无此接口
//      mVideoView.setOnInfoListener(new OnInfoListener() {
//
//          @Override
//          public boolean onInfo(MediaPlayer mp, int what, int extra) {
//              if(mLoadingView!=null){
//                  if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                      mLoadingView.setVisibility(View.VISIBLE);
//                  } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                      if(mp.isPlaying()){
//                          mLoadingView.setVisibility(View.GONE);
//                      }
//                  }
//              }
//              return true;
//          }
//      });
    }

    /**
     * 设置SeekBar
     */
    private void initSeekBar() {
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                float percent = seekbar.getProgress() / (float) seekbar.getMax();
                int seekDuration = (int) (mVideoView.getDuration() * percent);
                if (seekDuration > 0) {
                    mVideoView.seekTo(seekDuration);
                }
                mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);
                mHandler.post(updateSeekBarThread);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                mHandler.removeCallbacks(updateSeekBarThread);
                mHandler.removeCallbacks(hiddenViewThread);
            }

            @Override
            public void onProgressChanged(SeekBar seekbar, int position, boolean arg2) {
                if (mCurrentTime != null) {
                    mCurrentTime.setText(Utility.generateTime(
                            (int) (mVideoView.getDuration() * position / (float) seekbar.getMax())
                    ));
                }
            }
        });
    }

    /**
     * 初始化其他组件
     */
    private void initOther() {
        if (seriesPosition != 0) {
            mTitle.setText(title + getString(R.string.what_series, seriesPosition));
        } else {
            mTitle.setText(title);
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (!TextUtils.isEmpty(playUrl)) {
            mVideoView.setVideoPath(playUrl);
        } else {
            Utility.showToast(this, getString(R.string.play_url_error));
        }
    }

    /**
     * 隐藏控制View
     */
    private Runnable hiddenViewThread = new Runnable() {

        @Override
        public void run() {
            mControllerView.setVisibility(View.GONE);
            Log.d(TAG, "=====================hiddenViewThread");
        }
    };
    /**
     * 更新进度条
     */
    private Runnable updateSeekBarThread = new Runnable() {

        @Override
        public void run() {
            mHandler.postDelayed(updateSeekBarThread, UPDATE_INTERVAL);
            if (needUpdataSeekBar && mVideoView.getDuration() != 0) {
                mSeekBar.setProgress(
                        (int) (mVideoView.getCurrentPosition() / (float) mVideoView.getDuration() * 1000)
                );
                mSeekBar.setSecondaryProgress(mVideoView.getBufferPercentage() * 10);
                mCurrentTime.setText(Utility.generateTime((int) mVideoView.getCurrentPosition()));
                double time = mVideoView.getDuration();
                mTotalTime.setText(Utility.generateTime((int) mVideoView.getDuration()));
            }
        }
    };

    private void onVideoPause() {
        mVideoView.pause();
        mCenterStatus.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(updateSeekBarThread);
    }

    private void onVideoPlay() {
        mVideoView.start();
        totalDuration = (int) mVideoView.getDuration();
        mCenterStatus.setVisibility(View.GONE);
        mHandler.post(updateSeekBarThread);
    }

    private void showControllerBar() {
        mHandler.removeCallbacks(hiddenViewThread);
        if (mControllerView.getVisibility() != View.VISIBLE) {
            mControllerView.setVisibility(View.VISIBLE);
        }
//        Utility.translateAnimationmethod(mButtomView,
//                0f, 0f, 1.0f, 0f, 300);
        mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sqlDataItemModel != null && mVideoView != null
                && mVideoView.getCurrentPosition() >= 0) {
            currentPosition = mVideoView.getCurrentPosition();
            sqlDataItemModel.setPlayPosition(currentPosition);
            if (seriesPosition != -1) {
                sqlDataItemModel.setSeriesPosition(seriesPosition);
            }
            SQLDataOperationMethod.saveData(this, sqlDataItemModel);
            Log.d("hezb", "vod onPause========" + currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
            Log.d(TAG, "解绑服务成功！");
        }
        super.onDestroy();
        mHandler.removeCallbacks(updateSeekBarThread);
        mVideoView.stopPlayback();
    }


    //------------------------------------------已下为盒子的相关操作

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        showControllerBar();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                setVideoPositionByKey(keyCode);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (isPlayComplete) {
                    currentPosition = 0;
                    mVideoView.setVideoPath(playUrl);
                    isPlayComplete = false;
                    mCenterStatus.setVisibility(View.GONE);
                } else if (!mVideoView.isPlaying()) {
                    onVideoPlay();
                } else if (mCenterStatus.getVisibility() != View.VISIBLE) {
                    mCenterStatus.setImageResource(R.drawable.video_pause_stb);
                    onVideoPause();

                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (mMenuLayout.getVisibility() == View.VISIBLE) {
                    mMenuLayout.setVisibility(View.GONE);
                } else {
                    mMenuLayout.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(menuFoused, 200);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(volumeValue < maxVolume){
                    volumeValue = volumeValue + 1;
                    volumeBar.setProgress(volumeValue);
                    volumeBar.setVisibility(View.VISIBLE);
                    mHandler.removeCallbacks(hideVolume);
                    mHandler.postDelayed(hideVolume, 1500);
                    return true;
                }
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(volumeValue > 0){
                    volumeValue = volumeValue - 1;
                    volumeBar.setProgress(volumeValue);
                    volumeBar.setVisibility(View.VISIBLE);
                    mHandler.removeCallbacks(hideVolume);
                    mHandler.postDelayed(hideVolume, 1500);
                }
                return true;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    private int menuSelectPosition = 3;

    private Runnable menuFoused = new Runnable() {
        @Override
        public void run() {
            lvMenu.requestFocus();
            lvMenu.setSelection(menuSelectPosition);
            View v = lvMenu.getChildAt(0);
            if (v != null) {
                v.requestFocus();
            }
        }
    };

    /**
     * 退出方法
     */
    private void exit() {
        if (waitExit) {
            waitExit = false;
            Utility.showToast(this, getString(R.string.press_more_time_to_exit));
            mHandler.postDelayed(cancleExit, 2000);
        } else {
            finish();
        }
    }

    /**
     * 取消退出
     */
    private Runnable cancleExit = new Runnable() {
        @Override
        public void run() {
            waitExit = true;
        }
    };

    /**
     * 快进，快退
     */
    public void setVideoPositionByKey(int keyCode) {
        onVideoPlay();
        mHandler.removeCallbacks(resetMultiple);
        mHandler.postDelayed(resetMultiple, 500);
        mHandler.removeCallbacks(hideStatusView);
        mHandler.postDelayed(hideStatusView, 1500);
        multiple++;
        mCenterStatus.setVisibility(View.VISIBLE);
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            forward(multiple);
            mCenterStatus.setImageResource(R.drawable.video_forward_stb);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            rewind(multiple);
            mCenterStatus.setImageResource(R.drawable.video_previous_stb);
        }
    }

    /**
     * 隐藏状态
     */
    private Runnable hideStatusView = new Runnable() {

        @Override
        public void run() {
            mCenterStatus.setVisibility(View.GONE);
        }
    };

    /**
     * 重置倍速度
     */
    private Runnable resetMultiple = new Runnable() {

        @Override
        public void run() {
            multiple = 1;
        }
    };
    private Runnable hideVolume = new Runnable() {
        @Override
        public void run() {
            volumeBar.setVisibility(View.GONE);
        }
    };

    /**
     * 快进 15s
     */
    private void forward(int multiple) {
        double temp = Math.sqrt(multiple);//开平方跟 快进速度不会那么快
        int progress = (int) (15000 * temp + mVideoView.getCurrentPosition());
        if (progress > mVideoView.getDuration()) {
            progress = (int) mVideoView.getDuration() - 2000;
        } else if (progress < 0) {
            progress = 0;
        }
        mVideoView.seekTo(progress);
        Log.d(TAG, "forward progress:" + progress);
    }

    /**
     * 快退 15s
     */
    private void rewind(int multiple) {
        double temp = Math.sqrt(multiple);//开平方跟 快进速度不会那么快
        int progress = (int) (mVideoView.getCurrentPosition() - 15000 * temp);
        if (progress < 0) {
            progress = 0;
        }
        mVideoView.seekTo(progress);
        Log.d(TAG, "rewind progress:" + progress);
    }


    //TODO 以下为拉屏相关
    private boolean isBind = false;// 是否绑定成功
    private PullScreenBinder pullScreenBinder;
    private PullScreenBinder.OnPullScreenListener onPullScreenListener = new PullScreenBinder.OnPullScreenListener() {

        @Override
        public void onPullScreen(String ip) {
            SendPacketThread sendPacket = new SendPacketThread(ip, title, playUrl);
            sendPacket.setPosition((int) mVideoView.getCurrentPosition());
            sendPacket.start();
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            pullScreenBinder = (PullScreenBinder) arg1;
            if (pullScreenBinder != null) {
                pullScreenBinder.setListener(onPullScreenListener);
            }
        }
    };

    @Override
    public void onClick(DialogInterface dialog, int which) {
        this.onBackPressed();
    }


    @Override
    public void onRateChange(String playUrl) {
        this.playUrl = playUrl;
        currentPosition = mVideoView.getCurrentPosition();
        mVideoView.setVideoPath(playUrl);
        mVideoView.post(new Runnable() {
            @Override
            public void run() {
                mVideoView.seekTo(currentPosition);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.currentPosition = mVideoView.getCurrentPosition() - 2000;

        LogUtil.d("slf " + TAG, "左侧菜单  position=" + position);
        if (position == 1) {
            setScle(SCALE_16_9);
        } else if (position == 2) {
            setScle(SCALE_4_3);
        } else if (position > 2) {
            this.menuSelectPosition = position;
            int p = position - 3;
            if (p >= 0 && p < dataSet.size()) {
                this.playUrl = dataSet.get(p).getBfUrl();
            }
        }
        LogUtil.d("slf" + TAG, "(onItemClick)playUrl=" + playUrl);

        mVideoView.post(new Runnable() {
            @Override
            public void run() {
                mVideoView.setVideoPath(playUrl);
            }
        });
        mVideoView.post(new Runnable() {
            @Override
            public void run() {
                mVideoView.seekTo(currentPosition);
            }
        });
    }
}
