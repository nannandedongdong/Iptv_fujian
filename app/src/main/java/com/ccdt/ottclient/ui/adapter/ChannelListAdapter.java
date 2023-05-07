package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.ui.activity.LivePlayerActivity;

import java.util.List;
import java.util.Map;

/**
 * @author hezb
 * @Package com.ccdt.stb.vision.ui.adapter
 * @ClassName: ChannelListAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年6月5日 下午1:22:14
 */

public class ChannelListAdapter extends BaseAdapter {

    private final String TAG = "ChannelListViewAdapter";
    private LayoutInflater mInflater;


    private Map<String, Object>[] zhiboSourceList;

    private int selectPosition = 0;

    private List<ChannelObj> mDataSetChannel;
    private List<ChannelObj> mDataSetChannelColl;
//    private List<CollectionObj> mDataSetColl;
    private int mChannelListType = LivePlayerActivity.LIST_TYPE_CHANNEL;

    public ChannelListAdapter(Context context, List<ChannelObj> dataSetChannel, List<ChannelObj> dataSetChannelColl, int[] channelListTypeArr) {
        mInflater = LayoutInflater.from(context);
        this.mDataSetChannel = dataSetChannel;
        this.mDataSetChannelColl = dataSetChannelColl;
    }

    public void setData(List<ChannelObj> allMap) {
        this.mDataSetChannel = allMap;
        formatValue();
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mChannelListType == LivePlayerActivity.LIST_TYPE_CHANNEL) {
            if (mDataSetChannel != null) {
                ret = mDataSetChannel.size();
            }
        } else if (mChannelListType == LivePlayerActivity.LIST_TYPE_COLLECT) {
            if (mDataSetChannelColl != null) {
                ret = mDataSetChannelColl.size();
            }
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mDataSetChannel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.live_channel_list_item, null);
            holder.channelName = (TextView) convertView
                    .findViewById(R.id.live_channel_list_channel_name);
            holder.poster = (ImageView) convertView
                    .findViewById(R.id.live_channel_list_channle_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChannelObj channelObj;
        if (mChannelListType == LivePlayerActivity.LIST_TYPE_CHANNEL) {
            channelObj = mDataSetChannel.get(position);
        } else{
            channelObj = mDataSetChannelColl.get(position);
        }

        holder.channelName.setText(channelObj.getMzName());
////        Utility.displayImage(map.get(ConstantKey.ROAD_TYPE_LOGOURL),
////                holder.poster, null, R.drawable.image_loading);
//
//        if (position == selectPosition) {
////            convertView.setBackgroundResource(R.color.color_33ffffff);
//        } else {
//            convertView.setBackgroundResource(android.R.color.transparent);
//        }

        return convertView;
    }

    private void formatValue() {
        if (mDataSetChannel != null && mDataSetChannel.size() != 0) {
            try {
                zhiboSourceList = new Map[mDataSetChannel.size()];
                for (int i = 0; i < mDataSetChannel.size(); i++) {
//                    JsonElement jsonElement = jsonParser.parse(
//                            allMap.get(i).get(ConstantKey.ROAD_TYPE_ZBSOURCE));
//                    JsonArray jsonArray = jsonElement.getAsJsonArray();
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    for (JsonElement item : jsonArray) {
//                        JsonObject jsonObject = item.getAsJsonObject();
//                        map.put(jsonObject.get(ConstantKey.ROAD_TYPE_TYPE).getAsString(),
//                                gson.fromJson(jsonObject, ZhiBoSource.class));
//                    }
//                    zhiboSourceList[i] = map;
                }

            } catch (Exception e) {
                Log.d(TAG, "json format error!");
            }
        }
    }

    public Map<String, Object> getZhiBoSource(int position) {
        return zhiboSourceList[position];
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }


    public void setChannelListType(int type) {
        this.mChannelListType = type;
    }

    @Override
    public int getItemViewType(int position) {
        return mChannelListType;
    }

    public static class ViewHolder {
        public TextView channelName;
        public ImageView poster;
    }

    public class ZhiBoSource {
        public String name;
        public String path;
        public String moveUrl;
        public String liveUrl;
        public String code;
        public String type;
    }

}