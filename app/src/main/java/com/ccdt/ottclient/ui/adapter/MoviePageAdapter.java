package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.ui.view.FloatingLayout;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class MoviePageAdapter extends RecyclerView.Adapter<MoviePageAdapter.MyViewHolder> implements View.OnFocusChangeListener {
    private Context mContext;
    private List<ChannelObj> mDataSet;
    private RecyclerView mRecycler;
    public static final int TYPE_MOVIE = 0;
    public static final int TYPE_CHANEL = 2;
    private static final int ITEM_NUM_MIN = 1;
    private Animation bigAnim;
    private VideoView mVideoView = null;
    private int mIndicatorTabId;
    private ImageView focusView;
    private View fouseView;
    private Handler mHandler = new Handler();
    Runnable zoom = new Runnable() {
        @Override
        public void run() {
            zoomIn(fouseView);
        }
    };
    private int padding;

    public void setIndicatorTabId(int id) {
        this.mIndicatorTabId = id;
    }


    public MoviePageAdapter(List<ChannelObj> dataSet, Context context, RecyclerView recycler, ImageView focusView) {
        this.mContext = context;
        this.mDataSet = dataSet;
        this.mRecycler = recycler;
        this.focusView = focusView;
        padding = context.getResources().getDimensionPixelOffset(R.dimen.px33);
        bigAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_big_common);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        if (viewType == TYPE_MOVIE) {
            holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_movie_spec, null, false));
        } else {
            holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_channel, null, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position == 0) {
            View iv_zhibo = holder.itemView.findViewById(R.id.iv_zhibo);
            View iv_huikan = holder.itemView.findViewById(R.id.iv_huikan);
            View iv_favorite = holder.itemView.findViewById(R.id.iv_favorite);
//            mVideoView = (VideoView)holder.itemView.findViewById(R.id.video);
            View view_front = holder.itemView.findViewById(R.id.view_front);
            view_front.setNextFocusUpId(mIndicatorTabId);

            View.OnFocusChangeListener menuFousedListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

//                    View v_focus = v.findViewWithTag("v_focus");

                    if (hasFocus) {
//                        v_focus.setVisibility(View.VISIBLE);

                        v.bringToFront();
                        v.startAnimation(bigAnim);
                    } else {
//                        v_focus.setVisibility(View.INVISIBLE);
                        v.clearAnimation();
                    }
                }
            };
//            iv_zhibo.setOnFocusChangeListener(menuFousedListener);
//            iv_huikan.setOnFocusChangeListener(menuFousedListener);
//            iv_favorite.setOnFocusChangeListener(menuFousedListener);
//            view_front.setOnFocusChangeListener(this);

            iv_zhibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnZhiboClickListener != null) {
                        mOnZhiboClickListener.onZhiboClick();
                    }
                }
            });


            iv_huikan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnHuikanClickListener != null) {
                        mOnHuikanClickListener.onHuikanClick();
                    }
                }
            });

            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnShoucangClickListener != null) {
                        mOnShoucangClickListener.onShoucangClick();
                    }
                }
            });
            view_front.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果设置了回调，则设置点击事件
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(position);
                    }
                }
            });
        } else {

            if (position == 1) {
                holder.itemView.setNextFocusLeftId(R.id.view_front);
            }

            if (position % 2 == 1) {
                holder.itemView.setNextFocusUpId(mIndicatorTabId);
            }

            ChannelObj channelObj = mDataSet.get(position);
            if (channelObj != null) {
                Utility.displayImage(channelObj.getLogoUrl(), holder.iv_channel);
            }
            holder.itemView.setOnFocusChangeListener(this);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果设置了回调，则设置点击事件
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(position);
                    }
                }
            });
            if (position == mDataSet.size() - 1) {
                holder.clean();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MOVIE;
        }

        return TYPE_CHANEL;
    }

    private void zoomIn(View v) {

        int shadowWith = (int) (v.getWidth() + 2 * padding);
        int shadowHeight = (int) (v.getHeight() + 2 * padding);
        FloatingLayout.LayoutParams focusParams = new FloatingLayout.LayoutParams(shadowWith, shadowHeight);
        focusParams.setX((int) (v.getX()) - padding);
        focusParams.setY((int) (v.getY()) - padding);
        focusParams.customPosition = true;
        focusView.setLayoutParams(focusParams);

        v.destroyDrawingCache();
        v.setDrawingCacheEnabled(true);
        if (v.getDrawingCache() != null) {
            Bitmap cache = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
//            Drawable drawable =new BitmapDrawable(cache);
            focusView.setImageBitmap(cache);
//            focusView.setBackgroundDrawable(drawable);
            focusView.setVisibility(View.VISIBLE);
            focusView.startAnimation(bigAnim);
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mDataSet != null) {
            ret = mDataSet.size();
        }
        if (ret < ITEM_NUM_MIN) {
            ret = ITEM_NUM_MIN;
        }
        Log.i("SIZE", ret + "");
        return ret;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            fouseView = v;
            mHandler.postDelayed(zoom, 0);
        } else {
            focusView.clearAnimation();
            focusView.setVisibility(View.GONE);
        }
    }


    class MyViewHolder extends ViewHolder {

        public ImageView iv_channel;

        public ImageView layout_video;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_channel = (ImageView) itemView.findViewById(R.id.iv_channel);


        }

        public void clean() {
            iv_channel = null;
            layout_video = null;
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(int dataPosition);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private OnZhiboClickListener mOnZhiboClickListener;

    public void setOnZhiboClickListener(OnZhiboClickListener l){
        this.mOnZhiboClickListener = l;
    }

    public interface OnZhiboClickListener{
        void onZhiboClick();
    }


    private OnShoucangClickListener mOnShoucangClickListener;

    public void setOnShoucangClickListener(OnShoucangClickListener l){
        this.mOnShoucangClickListener = l;
    }

    public interface OnShoucangClickListener{
        void onShoucangClick();
    }

    private OnHuikanClickListener mOnHuikanClickListener;

    public void setOnHuikanClickListener(OnHuikanClickListener l){
        this.mOnHuikanClickListener = l;
    }

    public interface OnHuikanClickListener{
        void onHuikanClick();
    }



}
