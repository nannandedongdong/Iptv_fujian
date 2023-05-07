package com.ccdt.ottclient.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetChannelListTask;
import com.ccdt.ottclient.ui.activity.LivePlayerActivity;
import com.ccdt.ottclient.ui.activity.TvBackActivity;
import com.ccdt.ottclient.ui.adapter.MoviePageAdapter;
import com.ccdt.ottclient.utils.LogUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivePageFragment extends BaseIndicatorFragment implements TaskCallback, MoviePageAdapter.OnItemClickLitener, MoviePageAdapter.OnZhiboClickListener, MoviePageAdapter.OnHuikanClickListener, MoviePageAdapter.OnShoucangClickListener {
    private VideoView mVideoView;
    private RecyclerView mRecyclerView;
    private MoviePageAdapter adapter;
    private List<ChannelObj> mDataSet = new ArrayList<>();
    private View view_front;
    private ImageView fousedImage;
    private boolean isVisible;
    private String url;
    private Handler mHanler = new Handler();
    private Runnable play = new Runnable() {
        @Override
        public void run() {
            liveStart();
        }
    };
    ImageView ivfront;
    private boolean ifMove = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_main, container, false);
        ifMove = false;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        fousedImage = (ImageView) view.findViewById(R.id.iv_foused);

        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mContext)
                        .color(Color.TRANSPARENT)
                        .sizeResId(R.dimen.divider_channel)
                        .build());
        mRecyclerView.addItemDecoration(
                new VerticalDividerItemDecoration.Builder(mContext)
                        .color(Color.TRANSPARENT)
                        .sizeResId(R.dimen.divider_channel)
                        .build());

        adapter = new MoviePageAdapter(mDataSet, mContext, mRecyclerView, fousedImage);
        adapter.setIndicatorTabId(getIndicatorTabId());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
        adapter.setOnZhiboClickListener(this);
        adapter.setOnHuikanClickListener(this);
        adapter.setOnShoucangClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRecyclerView.getChildCount() > 0) {
                    mVideoView = (VideoView) mRecyclerView.getChildAt(0).findViewById(R.id.video);

                    ivfront = (ImageView) mRecyclerView.findViewById(R.id.view_image);
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
//                            LogUtil.d("slf", "总进度： " + mp.getDuration());
                            if(ifMove){
                                // 时移
                                mp.seekTo(mp.getDuration() - 20000);//制造缓冲
                            }
//
                            mVideoView.start();

                            //7241盒子
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    LogUtil.d("setOnInfoListener", "what=" + what + " ;extra=" + extra);
                                    return false;
                                }
                            });
                        }
                    });
                    mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {

                            LogUtil.d("setOnErrorListener", "what=" + what + " ;extra=" + extra);

                            return false;
                        }
                    });
                    Map<String, String> map = new HashMap<>();
                    map.put(ConstantKey.ROAD_TYPE_LMID, getLmId());
                    // 限制最多取5条
                    map.put(ConstantKey.ROAD_TYPE_PAGESIZE, "5");
                    map.put("recFlag", "1"); // 取推荐的直播
                    new GetChannelListTask(LivePageFragment.this).execute(map);

                }
            }
        }, 2 * 1000);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGetFocus() {
        super.onGetFocus();
        if (mRecyclerView != null) {
            if (view_front == null) {
                view_front = mRecyclerView.findViewById(R.id.view_front);
            }
            if (view_front != null) {
                view_front.requestFocus();
            }
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETCHANNELLIST:
                    if (result.code == TaskResult.SUCCESS) {
                        mDataSet.clear();
                        if (result.data != null) {
                            CommonSearchInvokeResult<ChannelObj> invokeResult = (CommonSearchInvokeResult<ChannelObj>) result.data;
                            if (invokeResult.getRtList() == null) {
                                return;
                            }
                            mDataSet.clear();
                            mDataSet.addAll(invokeResult.getRtList());
                        }
                        adapter.notifyDataSetChanged();
                        String playUrl = null;
                        if (mDataSet.size() == 0) {
                            return;
                        }
                        ChannelObj info = mDataSet.get(0);

                        if (info.getZbSource() != null) {
                            ifMove = "1".equals(info.getIfMove());
                            if (info.getZbSource().size() > 0) {
                                for (int i = 0; i < info.getZbSource().size(); i++) {
                                    if (info.getZbSource().get(i).getType().equals("HD")) {
                                        playUrl = info.getZbSource().get(i).getLiveUrl();
                                    } else {
                                        playUrl = info.getZbSource().get(i).getLiveUrl();
                                    }
                                }
                            }
                        }
                        if (playUrl != null) {
                            url = playUrl;
                            ivfront.setVisibility(View.GONE);
                            mHanler.postDelayed(play, 2000);
                        }
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(int dataPosition) {
        if (mDataSet == null || mDataSet.size() <= 0) {
            return;
        }

        ChannelObj channelObj = mDataSet.get(dataPosition);
        Intent intent = new Intent(mContext, LivePlayerActivity.class);
        intent.putExtra("lmid", getLmId());
        intent.putExtra("channel", channelObj);
        intent.putExtra("action", 2);
        startActivity(intent);


    }

    @Override
    public void onResume() {
        if (isVisible && mVideoView != null) {
            mHanler.postDelayed(play, 2000);
        }
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            if (mVideoView != null) {
                mHanler.postDelayed(play, 2000);
            }
        } else {
            if (mVideoView != null) {
                StopLive();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            StopLive();
        }
    }

    private void liveStart() {
        ivfront.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(url)) {
            mVideoView.setVideoPath(url);
        }
    }

    private void StopLive() {
        mHanler.removeCallbacks(play);
        ivfront.setVisibility(View.VISIBLE);
        mVideoView.stopPlayback();
        mVideoView.setVisibility(View.GONE);
    }

    @Override
    public void onZhiboClick() {
        Intent i = new Intent(mContext, LivePlayerActivity.class);
        i.putExtra("action", 0);
        i.putExtra("lmid", getLmId());
        mContext.startActivity(i);
    }

    @Override
    public void onHuikanClick() {
        TvBackActivity.actionStart(mContext, "");
    }

    @Override
    public void onShoucangClick() {
        Intent i = new Intent(mContext, LivePlayerActivity.class);
        i.putExtra("action", 1);
        i.putExtra("lmid", getLmId());
        i.putExtra("currentPlayTypeIndex", 1);
        mContext.startActivity(i);
    }
}
