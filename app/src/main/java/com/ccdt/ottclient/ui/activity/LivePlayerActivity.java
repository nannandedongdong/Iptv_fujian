package com.ccdt.ottclient.ui.activity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.model.CollectionObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.ZbSourceObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.ActionOperateTask;
import com.ccdt.ottclient.tasks.impl.GetChannelListTask;
import com.ccdt.ottclient.tasks.impl.GetCollectedListTask;
import com.ccdt.ottclient.ui.adapter.ChannelListAdapter;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.ToastUtil;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LivePlayerActivity extends BaseActivity implements View.OnClickListener, TaskCallback, AdapterView.OnItemClickListener {
    private static final String TAG = "hezb";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private static final int UPDATE_INTERVAL = 1000;//更新进度条时间间隔
    private static final int HIDE_INTERVAL = 5000;//隐藏控制View时间间隔

    private static final int MSG_WHAT_HIDE_MENU = 1;
    private static final int MSG_WHAT_HIDE_CHANNELLIST = 2;

    private static final int HIDE_INTERVAL_MENU_LEFT = 5000;
    private static final int HIDE_INTERVAL_MENU_CENTER = 3000;

    private VideoView mVideoView;
    private View mLoadingView;
    private TextView mLoadingInfo;
    private View mControllerView;
    private TextView mTitle;

    private Handler mHandler = new Handler();

    private String playUrl;

    private boolean waitExit = true;

    private List<ChannelObj> listMap = new ArrayList<>();
    private List<ChannelObj> data = new ArrayList<>();
    private View mMenuLeft;

    private ChannelListAdapter mAdapter;
    private int currentChannelIndex = 0;
    private int currentListIndex = 0;
    private static final int COLLECT_ACTION = 1;
    private static final int CHANNEL_ACTION = 0;
    private static final int PLAY_ACTION = 2;
    private RelativeLayout mBottomLayout;
    private int aciton;
    private TextView tvNowChannel;
    private TextView tvNowProgram;
    private TextView tvNextProgram;
    private SeekBar mSeekBar;
    private ChannelObj playInfo;
    private LinearLayout mMenuCenter;
    private ViewStub stub;
    private TextView tvBili;
    private SeekBar seek_volume;
    int volumeValue;
    int maxValue;
    private String mMzId;
    private String mLmId;
    private TextView txtTitleLeftMenu;
    private LinearLayout layout_volume;
    private LinearLayout layout_bili;
    private TextView txtCollect;
    private TextView mCurrentTime;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_live_player;
    }

    @Override
    protected void initWidget() {
        mVideoView = (VideoView) findViewById(R.id.android_videoview);
        mCurrentTime = ((TextView) findViewById(R.id.video_current_time));
        mLoadingView = findViewById(R.id.video_loading);
        mLoadingInfo = (TextView) findViewById(R.id.loading_text);
        mControllerView = findViewById(R.id.video_controller_layout);
        mTitle = (TextView) findViewById(R.id.tv_now_channel);
        mBottomLayout = ((RelativeLayout) findViewById(R.id.video_bottom_controller_layout));
        mMenuLeft = findViewById(R.id.live_player_stb_channels_list_rl);
        tvNowChannel = ((TextView) findViewById(R.id.tv_now_channel));
        tvNowProgram = ((TextView) findViewById(R.id.tv_now_progrem));
        tvNextProgram = ((TextView) findViewById(R.id.tv_next_progrem));
        mSeekBar = ((SeekBar) findViewById(R.id.video_seekbar));
        txtTitleLeftMenu = ((TextView) findViewById(R.id.live_player_stb_channles_title));
        stub = (ViewStub) findViewById(R.id.view_stub);
        aciton = getIntent().getIntExtra("action", 0);
        mLmId = getIntent().getStringExtra("lmid");
        currentPlayTypeIndex = getIntent().getIntExtra("currentPlayTypeIndex", 0);
        currentShowTypeIndex = currentPlayTypeIndex;
        if (aciton != 2) {
            currentListIndex = aciton;
        } else {
            currentListIndex = 0;
            playInfo = (ChannelObj) getIntent().getExtras().get("channel");
            if (playInfo != null) {
                onChangeChannel(playInfo);
            }

        }
        initOperator();
        initSeekBar();

        initGrid();
        requestDataChannel();
//        requestDataCollect();

        // 初始化 菜单键 mMenuCenter
        initDialog();
    }


    private void initOperator() {

        initVideoView();

        initOther();
    }


    private Runnable hideLoadingView = new Runnable() {

        @Override
        public void run() {
            mLoadingView.setVisibility(View.GONE);
        }
    };

    private int completeTimes = 0;

    @SuppressLint("NewApi")
    private void initVideoView() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                if (ifMove) {
                    // 时移  制造缓冲
                    timeShift = mp.getDuration() - 20000;//设置到当前时间
                    mp.seekTo(timeShift);
                }

                mSeekBar.setProgress(mSeekBar.getMax());
                mHandler.post(updateSeekBarThread);
                completeTimes = 0;
                mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);

                mLoadingView.setVisibility(View.GONE);
                //7241盒子
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (mLoadingView != null) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                mLoadingView.setVisibility(View.VISIBLE);
                            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                if (mp.isPlaying()) {
                                    mLoadingView.setVisibility(View.GONE);
                                }
                            }
                        }
                        return true;
                    }
                });
                onVideoPlay();
            }
        });
        //7241盒子
//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                LogUtil.d(TAG, "setOnInfoListener:   what:" + what + "     extra:" + extra + "       mp.isPlaying:" + mp.isPlaying());
//                if (mLoadingView != null) {
//                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                        mLoadingView.setVisibility(View.VISIBLE);
//                        mHandler.removeCallbacks(hideLoadingView);//TODO 没执行 end 因而手动5S后隐藏加载
//                        mHandler.postDelayed(hideLoadingView, HIDE_INTERVAL);
//                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                        if (mp.isPlaying()) {
//                            mLoadingView.setVisibility(View.GONE);
//                        }
//                    }
//                }
//                return true;
//            }
//        });


//        Field field= null;
//        try {
//            field = VideoView.class.getDeclaredField("mMediaPlayer");
//            field.setAccessible(true);
//            mMediaPlayer = (MediaPlayer) field.get(mVideoView);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.e("slf setOnCompletionListener 播放进度", mp.getCurrentPosition() + "/" + mp.getDuration());
//                completeTimes++;
//                if (completeTimes > 3) {
//                    ToastUtil.ToastMessage(LivePlayerActivity.this, getString(R.string.player_error_play_complete));
//                } else {
                mLoadingView.setVisibility(View.VISIBLE);
                mVideoView.setVideoPath(playUrl);
//                }
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtil.ToastMessage(LivePlayerActivity.this, getString(R.string.player_error_play_complete));
                return true;
            }
        });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mControllerView.getVisibility() == View.VISIBLE) {
                    mHandler.post(hiddenViewThread);
                } else {
                    setControllerBarVisible(true);
                }

                return false;
            }
        });
        //7241
//      mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
     * 初始化其他组件
     */
    private void initOther() {

    }


    private void onVideoPlay() {
        mVideoView.start();
    }

//    private void showControllerBar() {
//
//        mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);
//
//    }


    private void setControllerBarVisible(boolean isShow) {
        mHandler.removeCallbacks(hiddenViewThread);
        if (isShow) {
            mControllerView.setVisibility(View.VISIBLE);
            if (ifMove) {
                mSeekBar.setProgress(
                        (int) (mVideoView.getCurrentPosition() / (float) mVideoView.getDuration() * 1000)
                );
            }
        } else {
            mControllerView.setVisibility(View.GONE);
        }
    }


    /**
     * 隐藏控制View
     */
    private Runnable hiddenViewThread = new Runnable() {

        @Override
        public void run() {
            mControllerView.setVisibility(View.GONE);
        }
    };


    private void removeCallbacks() {
        mHandler.removeCallbacks(hideMenuCenter);
        mHandler.removeCallbacks(hideMenuLeft);
    }


    ///////////////////////////////////////////////////////////////
    // 中间菜单

    // 设置 菜单是否可见
    private void setMenuCenterShow(boolean isShow) {
        changeCollectText();
        if (mMenuCenter != null) {
            if (isShow) {
                mMenuCenter.setVisibility(View.VISIBLE);
                mMenuCenter.requestFocus();
                mHandler.postDelayed(hideMenuCenter, HIDE_INTERVAL_MENU_CENTER);
            } else {
                mHandler.removeCallbacks(hideMenuCenter);
                mMenuCenter.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏中间菜单
     */
    private Runnable hideMenuCenter = new Runnable() {

        @Override
        public void run() {
            setMenuCenterShow(false);
        }
    };


    // 加载菜单界面
    private void initDialog() {
        if (mMenuCenter == null) {
            if (stub != null) {
                mMenuCenter = (LinearLayout) stub.inflate();
                LinearLayout huikan = (LinearLayout) mMenuCenter.findViewById(R.id.item_huikan);
                huikan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMenuCenterShow(false);
                        TvBackActivity.actionStart(LivePlayerActivity.this, mMzId);
                    }
                });
                LinearLayout collect = (LinearLayout) mMenuCenter.findViewById(R.id.item_collect);
                tvBili = (TextView) mMenuCenter.findViewById(R.id.tv_bili);
                layout_volume = (LinearLayout) mMenuCenter.findViewById(R.id.item_volume);
                seek_volume = (SeekBar) mMenuCenter.findViewById(R.id.seekbar_volume);
                layout_bili = (LinearLayout) mMenuCenter.findViewById(R.id.item_bili);
                txtCollect = ((TextView) mMenuCenter.findViewById(R.id.txtCollect));


                collect.setOnClickListener(this);

                /*
                Map<String, String> map = new HashMap<>();
                        map.put(ConstantKey.ROAD_TYPE_MZID, playInfo.getId());
                        map.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.addCollect.name());
                        new ActionOperateTask(this).execute(map);
                        mMenuCenter.setVisibility(View.GONE);
                 */

                final AudioManager audiomanage = (AudioManager) getSystemService(AUDIO_SERVICE);
                maxValue = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
                seek_volume.setMax(maxValue);   //拖动条最高值与系统最大声匹配
                volumeValue = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                seek_volume.setProgress(volumeValue);
                seek_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                tvBili = (TextView) mMenuCenter.findViewById(R.id.tv_bili);
                tvBili.setText("16:9");
            }
            setMenuCenterShow(false);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.item_collect:
                Map<String, String> map = new HashMap<>();
                map.put(ConstantKey.ROAD_TYPE_MZID, playInfo.getId());
                if (ifCollect) {
                    // 已收藏则取消
                    map.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.delCollect.name());
                } else {
                    // 未收藏则添加收藏
                    map.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.addCollect.name());
                }
                new ActionOperateTask(this).execute(map);
                setMenuCenterShow(false);
                break;
        }
    }


    ///////////////////////////////////////////////////////////
    //  退出
    private long exitTime = 0;

    /**
     * 退出方法
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.ToastMessage(this, "再按一次退出播放");
            exitTime = System.currentTimeMillis();
        } else {
            mVideoView.stopPlayback();
            finish();
        }
    }

    ////////////////////////////////////////////////////////////
    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    /**
     * 设置SeekBar
     */
    private void initSeekBar() {
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
//                if(mCurrentTime!=null){
//                    mCurrentTime.setText(Utility.generateTime(
//                            (int) (mVideoView.getDuration() * position / (float) seekbar.getMax())
//                    ));
//                }
            }
        });
    }

    /**
     * 更新进度条
     */
    private Runnable updateSeekBarThread = new Runnable() {

        @Override
        public void run() {
            mHandler.postDelayed(updateSeekBarThread, UPDATE_INTERVAL);
            if (timeShift >= mVideoView.getDuration() - 20000) {
                mCurrentTime.setText("现场直播");
            } else {
                mCurrentTime.setText(format.format(new Date(
                        System.currentTimeMillis() - (mVideoView.getDuration() - timeShift))));
            }
            mCurrentTime.setText(format.format(new Date()));
        }
    };

    ///////////////////////////////////////////////


    // 收藏频道列表
    private List<ChannelObj> mDataSetChannelColl = new ArrayList<>();
    // 频道列表
    private List<ChannelObj> mDataSetChannel = new ArrayList<>();
    // 频道列表控件
    private ListView mListView;

    // 是否可时移
    private boolean ifMove = false;
    // 是否已收藏
    private boolean ifCollect = false;
    private int multiple = 1;
    private int timeShift = 0;//当前时移

    /**
     * 切换频道
     */
    private void changeChannel() {
        ifMove = false;
        // 频道
        ChannelObj info = null;
        if (currentShowTypeIndex == LIST_TYPE_CHANNEL) {
            // 频道
            info = mDataSetChannel.get(currentPlayIndex);
        } else {
            if (mDataSetChannelColl != null && mDataSetChannelColl.size() > 0) {
                info = mDataSetChannelColl.get(currentPlayIndex);
            } else {
                // 频道
                info = mDataSetChannel.get(currentPlayIndex);
            }


        }

        onChangeChannel(info);


    }

    /**
     * 切换频道
     */
    private void onChangeChannel(ChannelObj info) {
        playInfo = info;
        ifMove = false;
        ifCollect = false;
        if (info != null) {
            ifMove = "1".equals(info.getIfMove());
            String collectUids = info.getCollectUids();
            if (!TextUtils.isEmpty(collectUids)) {
                String userId = Account.getInstance().userId;
                String format = String.format(", %s,", userId);
                ifCollect = collectUids.contains(format);
                if (!ifCollect) {
                    format = String.format("[%s,", userId);
                    ifCollect = collectUids.contains(format);
                }
                if (!ifCollect) {
                    format = String.format(", %s]", userId);
                    ifCollect = collectUids.contains(format);
                }
                if (!ifCollect) {
                    format = String.format("[%s]", userId);
                    ifCollect = collectUids.contains(format);
                }
            }
//            changeCollectText();
            mMzId = info.getId();
            if (info.getStartTime() != null && info.getEndTime() != null) {
                String time = info.getStartTime() + " — "
                        + info.getEndTime();
                String programName = info.getEpgName();
                mTitle.setText(time + programName);
            } else {
                mTitle.setText("");
            }
            changeChannelZb(info.getZbSource());
            tvNowChannel.setText(info.getChannelId());
            tvNowProgram.setText(info.getEpgName());
            if (ifMove) {
                mSeekBar.setVisibility(View.VISIBLE);
            } else {
                mSeekBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置播放地址
     *
     * @param zbList
     */
    private void changeChannelZb(List<ZbSourceObj> zbList) {
        if (zbList != null) {
            if (zbList.size() > 0) {
                ZbSourceObj zbSourceObj = null;
                for (int i = 0; i < zbList.size(); i++) {
                    if (zbList.get(i).getType().equals("HD")) {
                        zbSourceObj = zbList.get(i);
                        break;
                    } else if (zbList.get(i).getType().equals("SD")) {
                        zbSourceObj = zbList.get(i);
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
        LogUtil.d("slf " + "直播播放地址", (ifMove ? "可时移" : "不可时移") + "  " + playUrl);

        if (!TextUtils.isEmpty(playUrl)) {
            mVideoView.setVideoPath(playUrl);
        } else {
        }
        setControllerBarVisible(true);
        mHandler.postDelayed(hiddenViewThread, HIDE_INTERVAL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPlayIndex = position;
        currentPlayTypeIndex = currentShowTypeIndex;
        changeChannel();
    }

    // 加载列表控件（收藏频道，直播频道）
    private void initGrid() {
        mListView = (ListView) findViewById(R.id.live_player_stb_channels_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    removeCallbacks();
                    mHandler.postDelayed(hideMenuLeft, HIDE_INTERVAL_MENU_LEFT);
                }
                return false;
            }
        });
        mAdapter = new ChannelListAdapter(this, mDataSetChannel, mDataSetChannelColl, mChannelListTypeArr);
        mListView.setAdapter(mAdapter);


    }


    /////////////////////////////////////////////////////////

    /**
     * 频道列表处理按键事件
     * 1、左右键 切换列表
     * 2、后退键 隐藏左侧菜单
     * 3、菜单键 （隐藏左侧菜单，显示中间菜单）
     * 4、其他处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    private boolean onKeyDownMenuLeft(int keyCode, KeyEvent event) {
        boolean ret = false;
        mHandler.postDelayed(hideMenuLeft, HIDE_INTERVAL_MENU_LEFT);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 后退键 隐藏左侧菜单
                setMenuLeftShow(false);
                ret = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //TODO 左右键 切换列表
                changeChannelListType(keyCode);
                break;
            case KeyEvent.KEYCODE_MENU:
                setMenuLeftShow(false);
                setMenuCenterShow(true);
                ret = true;
                break;
        }

        if (!ret) {
            ret = super.onKeyDown(keyCode, event);
        }
        return ret;
    }


    /**
     * 中间菜单处理按键事件
     * 1、菜单键 隐藏中间菜单
     * 2、后退键 隐藏中间菜单
     * 3、其余根据所在按钮处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    private boolean onKeyDownMenuCenter(int keyCode, KeyEvent event) {
        boolean ret = false;
        mHandler.postDelayed(hideMenuCenter, HIDE_INTERVAL_MENU_CENTER);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                setMenuCenterShow(false);
                ret = true;
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (layout_volume.hasFocus()) {
                    volumeSeek(keyCode);
                } else if (layout_bili.hasFocus()) {
                    final int currentPosition = mVideoView.getCurrentPosition();

                    // 画面比例
                    String bili = tvBili.getText().toString();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                    if ("16:9".equals(bili)) {
                        tvBili.setText("4:3");
                        layoutParams.width = (int) (MyApp.width * 0.75);
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    } else if ("4:3".equals(bili)) {
                        tvBili.setText("16:9");
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    } else {
                        tvBili.setText("16:9");
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                    mVideoView.setLayoutParams(layoutParams);
                    mVideoView.invalidate();

                    mVideoView.post(new Runnable() {
                        @Override
                        public void run() {
                            mVideoView.setVideoPath(playUrl);
                            mVideoView.seekTo(currentPosition);
                        }
                    });
                }
                break;
            default:
                break;
        }
        if (!ret) {
            ret = super.onKeyDown(keyCode, event);
        }

        return ret;
    }

    /**
     * 视频界面处理按键事件
     * 1、菜单键 呼出中间菜单
     * 2、确认键 呼出左侧菜单
     * 3、左右方向键 调节进度（针对可时移）
     * 4、上下方向键 切换频道
     * 5、后退键 退出当前Activity（双击）
     *
     * @param keyCode
     * @param event
     * @return
     */
    private boolean onKeyDownVideo(int keyCode, KeyEvent event) {
        boolean ret = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 后退键 退出当前Activity（双击）
                exit();
                ret = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //TODO 左右方向键 调节进度（针对可时移）
                if (ifMove) {
                    setVideoPositionByKey(keyCode);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                //TODO 上下方向键 切换频道
                if (currentShowTypeIndex == LIST_TYPE_CHANNEL) {
                    if (currentPlayIndex > 0) {
                        currentPlayIndex--;
                    } else {
                        currentPlayIndex = mDataSetChannel.size() - 1;
                    }
                } else if (currentShowTypeIndex == LIST_TYPE_COLLECT) {
                    if (currentPlayIndex > 0) {
                        currentPlayIndex--;
                    } else {
                        currentPlayIndex = mDataSetChannelColl.size() - 1;
                    }
                }
                if (currentPlayIndex < 0) {
                    currentPlayIndex = 0;
                }
                currentShowTypeIndex = currentPlayTypeIndex;
                changeChannel();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                //TODO 上下方向键 切换频道
                if (currentShowTypeIndex == LIST_TYPE_CHANNEL) {
                    if (currentPlayIndex < mDataSetChannel.size() - 1) {
                        currentPlayIndex++;
                    } else {
                        currentPlayIndex = 0;
                    }
                } else if (currentShowTypeIndex == LIST_TYPE_COLLECT) {
                    if (currentPlayIndex < mDataSetChannelColl.size() - 1) {
                        currentPlayIndex++;
                    } else {
                        currentPlayIndex = 0;
                    }
                }
                if (currentPlayIndex < 0) {
                    currentPlayIndex = 0;
                }
                changeChannel();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //TODO 确认键 呼出左侧菜单
                setMenuLeftShow(true);
//                mAdapter.setChannelListType(mChannelListTypeArr[currentPlayType]);
//                mAdapter.notifyDataSetChanged();
//                mListView.setSelection(currentPlayIndex);
                break;
            case KeyEvent.KEYCODE_MENU:
                //TODO 菜单键 呼出中间菜单
                setMenuCenterShow(true);
                break;

        }

        if (!ret) {
            ret = super.onKeyDown(keyCode, event);
        }

        return ret;
    }


    /**
     * 快进，快退
     */
    public void setVideoPositionByKey(int keyCode) {
        LogUtil.d("时移", "keyCode=" + keyCode);
        mHandler.removeCallbacks(hiddenViewThread);
        setControllerBarVisible(true);
        mHandler.postDelayed(hiddenViewThread, 3000);
        if (mVideoView.getDuration() > 15000) {//小于15s不是时移动
            mHandler.removeCallbacks(resetMultiple);
            mHandler.postDelayed(resetMultiple, 500);
            multiple++;
//        mCenterStatus.setVisibility(View.VISIBLE);
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                forward(multiple);
//            mCenterStatus.setImageResource(R.drawable.video_forward_stb);
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                rewind(multiple);
//            mCenterStatus.setImageResource(R.drawable.video_previous_stb);
            }

            mSeekBar.setProgress(
                    (int) (mVideoView.getCurrentPosition() / (float) mVideoView.getDuration() * 1000)
            );
        }
    }

    /**
     * 重置倍速度
     */
    private Runnable resetMultiple = new Runnable() {

        @Override
        public void run() {
            multiple = 1;
        }
    };

    /**
     * 快退 15s
     */
    private void rewind(int multiple) {
        double temp = Math.sqrt(multiple);//开平方跟 快进速度不会那么快
        timeShift = (int) (mVideoView.getCurrentPosition() - 15000 * temp);
        if (timeShift < 0) {
            timeShift = 0;
        }
        mVideoView.seekTo(timeShift);
//        Log.d(TAG, "rewind progress:" + timeShift);
    }

    /**
     * 快进 15s
     */
    private void forward(int multiple) {
        double temp = Math.sqrt(multiple);//开平方跟 快进速度不会那么快
        timeShift = (int) (15000 * temp + mVideoView.getCurrentPosition());
        if (timeShift > mVideoView.getDuration() - 20000) {
            timeShift = mVideoView.getDuration() - 20000;
        } else if (timeShift < 0) {
            timeShift = 0;
        }
        mVideoView.seekTo(timeShift);
//        Log.d(TAG, "forward progress:" + timeShift);
    }

    /**
     * 声音
     *
     * @param keyCode
     */
    private void volumeSeek(int keyCode) {
        int stepValue = (int) maxValue / 16;
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (volumeValue < maxValue) {
                volumeValue += stepValue;
            }
        } else {
            if (volumeValue > 0) {
                volumeValue -= stepValue;
            }
        }
        seek_volume.setProgress(volumeValue);
    }

    // 拦截处理按键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        removeCallbacks();
        boolean ret = false;
        if (mMenuCenter.getVisibility() == View.VISIBLE) {
            // 中间菜单可见
            ret = onKeyDownMenuCenter(keyCode, event);
        } else if (mMenuLeft.getVisibility() == View.VISIBLE) {
            // 左侧列表菜单可见
            ret = onKeyDownMenuLeft(keyCode, event);
        } else {
            // 中间菜单+左侧菜单均不可见
            ret = onKeyDownVideo(keyCode, event);
        }
        return ret;
    }

    private void changeCollectText() {
        if (ifCollect) {
            txtCollect.setText("取消收藏");
        } else {
            txtCollect.setText("收藏");
        }
    }

    //////////////////////////////////////////////////
    // 异步任务
    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_ACTIONOPERATE:
                    if ("1".equals(String.valueOf(result.data))) {
                        ToastUtil.toast("操作成功");
                        if (ifCollect) {
                            mDataSetChannelColl.remove(playInfo);
                            if (mDataSetChannelColl.size() <= 0) {
                                // 取消操作时
                                currentPlayTypeIndex = 0;
                                currentPlayIndex = 0;
                                mAdapter.setChannelListType(mChannelListTypeArr[currentPlayTypeIndex]);
                                txtTitleLeftMenu.setText(leftMenuTitleArr[currentPlayTypeIndex]);
                                mAdapter.notifyDataSetChanged();
                                mListView.setSelection(0);
                            }
                        } else {
                            mDataSetChannelColl.add(playInfo);
                            mAdapter.notifyDataSetChanged();
                        }
                        ifCollect = !ifCollect;
//                        changeCollectText();

                    } else {
                        ToastUtil.toast("操作失败");
                    }
                    break;
                case Constants.TASK_GETCOLLECTEDLIST:
                    mDataSetChannelColl.clear();
                    if (result.code == TaskResult.SUCCESS) {
                        CommonSearchInvokeResult<CollectionObj> invokeResult = (CommonSearchInvokeResult<CollectionObj>) result.data;
                        if (invokeResult != null) {
                            if (invokeResult.getRtList() != null && invokeResult.getRtList().size() > 0) {
                                for (CollectionObj coll : invokeResult.getRtList()) {
                                    for (ChannelObj chan : mDataSetChannel) {
                                        if (coll.getMzId().equals(chan.getId())) {
                                            mDataSetChannelColl.add(chan);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                    }
                    mAdapter.notifyDataSetChanged();
                    if (playInfo == null) {
                        changeChannel();
                    }
                    break;
                case Constants.TASK_GETCHANNELLIST:
                    mDataSetChannel.clear();
                    if (result.code == TaskResult.SUCCESS) {
                        CommonSearchInvokeResult<ChannelObj> invokeResult = (CommonSearchInvokeResult<ChannelObj>) result.data;
                        if (invokeResult != null) {
                            if (invokeResult.getRtList() != null) {
                                mDataSetChannel.addAll(invokeResult.getRtList());
                                if (playInfo != null) {
                                    for (int i = 0; i < mDataSetChannel.size(); i++) {
                                        ChannelObj channelObj = mDataSetChannel.get(i);
                                        if (channelObj != null) {
                                            if (playInfo.getId().equals(channelObj.getId())) {
                                                currentPlayIndex = i;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                    }
                    requestDataCollect();

                    break;
                default:
                    break;
            }
        }
    }

    ///////////////////////////////////////////////////
    // 取收藏频道列表
    private void requestDataCollect() {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
        map.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "ifMove,endTime,channelId,startTime,memberId,zbSource,id,logoUrl,mzType,mzName,mzId,validFlag,createTime,haibao");
        map.put(ConstantKey.ROAD_TYPE_PAGEYEMA, "1");
        map.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        map.put(ConstantKey.ROAD_TYPE_MZTYPE, "2");
        new GetCollectedListTask(this).execute(map);
    }

    // 取频道列表
    private void requestDataChannel() {
        Map<String, String> parms = new HashMap<>();
        parms.put(ConstantKey.ROAD_TYPE_LMID, mLmId);
        parms.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        parms.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "sourceType,channelId,mzName,mzId,logoUrl,zbSource,endTime,recordTime,ifBack,id,startTime,moveTime,epgName,ifMove,collectUids");
        new GetChannelListTask(this).execute(parms);
    }


    // 当前播放频道类表类型
    private int currentPlayTypeIndex = 0;
    // 当前播放频道index
    private int currentPlayIndex = 0;
    // 当前显示频道列表类型
    private int currentShowTypeIndex = 0;

    // 频道列表
    public static final int LIST_TYPE_CHANNEL = 0;
    // 收藏列表
    public static final int LIST_TYPE_COLLECT = 1;
    //
    private String[] leftMenuTitleArr = {"全部频道", "收藏频道"};
    private int[] mChannelListTypeArr = {LIST_TYPE_CHANNEL, LIST_TYPE_COLLECT};

    /**
     * 切换频道列表（频道列表/收藏列表）
     */
    private void changeChannelListType(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (--currentShowTypeIndex < 0) {
                currentShowTypeIndex = mChannelListTypeArr.length - 1;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (++currentShowTypeIndex >= mChannelListTypeArr.length) {
                currentShowTypeIndex = 0;
            }
        }
        txtTitleLeftMenu.setText(leftMenuTitleArr[currentShowTypeIndex]);
        mAdapter.setChannelListType(mChannelListTypeArr[currentShowTypeIndex]);
        mAdapter.notifyDataSetChanged();
        mListView.requestFocus();
        mListView.setSelection(0);
    }

//////////////////////////////////////////////////////////////
    // 左侧频道选择菜单

    /**
     * 设置左侧频道列表是否可见
     *
     * @param isShow
     */
    private void setMenuLeftShow(boolean isShow) {

        if (mVideoView != null) {
            LogUtil.d("播放进度", mVideoView.getCurrentPosition() + "/" + mVideoView.getDuration());
        }

        if (mMenuLeft != null) {
            if (isShow) {
                mMenuLeft.setVisibility(View.VISIBLE);
                setControllerBarVisible(true);
                mHandler.postDelayed(hideMenuLeft, HIDE_INTERVAL_MENU_LEFT);
                currentShowTypeIndex = currentPlayTypeIndex;
                mAdapter.setChannelListType(mChannelListTypeArr[currentPlayTypeIndex]);
                txtTitleLeftMenu.setText(leftMenuTitleArr[currentPlayTypeIndex]);
                mAdapter.notifyDataSetChanged();
                mListView.requestFocus();
                mListView.setSelection(currentPlayIndex);
            } else {
                mHandler.removeCallbacks(hideMenuLeft);
                setControllerBarVisible(false);
                mMenuLeft.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏左侧频道列表
     */
    private Runnable hideMenuLeft = new Runnable() {

        @Override
        public void run() {
            setMenuLeftShow(false);
        }
    };
}
