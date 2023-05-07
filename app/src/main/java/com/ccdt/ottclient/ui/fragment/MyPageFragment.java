package com.ccdt.ottclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.CollectionObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.PlayHistoryObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetCollectedListTask;
import com.ccdt.ottclient.tasks.impl.GetPlayHistoryTask;
import com.ccdt.ottclient.ui.activity.AccountInfoActivity;
import com.ccdt.ottclient.ui.activity.CollectedActivity;
import com.ccdt.ottclient.ui.activity.PlayHistoryActivity;
import com.ccdt.ottclient.ui.activity.PurchaseHistoryActivity;
import com.ccdt.ottclient.utils.ToastUtil;
import com.ccdt.ottclient.utils.Utility;
import com.slf.frame.tv.widget.TvRelativeLayoutAsGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPageFragment extends BaseIndicatorFragment implements View.OnClickListener, TvRelativeLayoutAsGroup.OnChildSelectListener, TaskCallback {

    private View btnAccount;
    private ImageView imgHaibaoCollect;
    private ImageView imgHaibaoRecorded;
    private View shadow_shoucang;
    private View shadow_play_recorder;

    private boolean flag_play_recorder;
    private boolean flag_shoucang;
    private TextView txt_num_play;
    private TextView txt_num_shoucang;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup ret = (ViewGroup) inflater.inflate(R.layout.fragment_mine_main, container, false);
        TvRelativeLayoutAsGroup mRoot = (TvRelativeLayoutAsGroup) ret.findViewById(R.id.root);

        mRoot.setOnChildSelectListener(this);


        View btnPurchase = ret.findViewById(R.id.btn_mine_purchase);
        btnAccount = ret.findViewById(R.id.btn_mine_account);
        View btnCollected = ret.findViewById(R.id.btn_mine_collected);
        imgHaibaoCollect = ((ImageView) btnCollected.findViewWithTag("img_haibao"));
        View btnRecorded = ret.findViewById(R.id.btn_mine_recorded);
        imgHaibaoRecorded = ((ImageView) btnRecorded.findViewWithTag("img_haibao"));
        View btnPaike = ret.findViewById(R.id.btn_mine_paike);
        View btnSettings = ret.findViewById(R.id.btn_mine_settings);
        shadow_shoucang = ret.findViewById(R.id.shadow_shoucang);
        shadow_play_recorder = ret.findViewById(R.id.shadow_play_recorder);
        txt_num_play = ((TextView) ret.findViewById(R.id.txt_num_play));
        txt_num_shoucang = ((TextView) ret.findViewById(R.id.txt_num_shoucang));

        btnPurchase.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        btnCollected.setOnClickListener(this);
        btnRecorded.setOnClickListener(this);
        btnPaike.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

//        btnPurchase.setOnFocusChangeListener(this);
//        btnAccount.setOnFocusChangeListener(this);
//        btnCollected.setOnFocusChangeListener(this);
//        btnRecorded.setOnFocusChangeListener(this);
//        btnPaike.setOnFocusChangeListener(this);
//        btnSettings.setOnFocusChangeListener(this);

        btnAccount.setNextFocusUpId(getIndicatorTabId());
        btnCollected.setNextFocusUpId(getIndicatorTabId());
        btnRecorded.setNextFocusUpId(getIndicatorTabId());
        btnPaike.setNextFocusUpId(getIndicatorTabId());


        return ret;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        flag_play_recorder = false;
        flag_shoucang = false;
        txt_num_play.setText("0");
        txt_num_shoucang.setText("0");
        shadow_play_recorder.setVisibility(View.INVISIBLE);
        shadow_shoucang.setVisibility(View.INVISIBLE);
        reqeustDataCollected();
        new GetPlayHistoryTask(this).execute(mContext);
    }

    private void reqeustDataCollected() {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
//        map.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "bfUrl,id,mzId,createTime,mzCreateTime,mzName,haibao,mzAuthor");
        map.put(ConstantKey.ROAD_TYPE_PAGEYEMA, "1");
        map.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1");
        map.put(ConstantKey.ROAD_TYPE_MZTYPE, "0,1");
        new GetCollectedListTask(this).execute(map);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_mine_account:
//                SystemHiddenActivity.actionStart(mContext);
                Intent intent = new Intent(mContext, AccountInfoActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.btn_mine_collected:
                CollectedActivity.actionStart(mContext);
                break;
            case R.id.btn_mine_paike:
//                InterfaceTestActivity.actionStart(mContext);
//                PaikeListActivity.actionStart(mContext, "", "我的拍客", PaikeListActivity.PAIKE_TYPE_WODE);
                ToastUtil.toast("此功能暂未开通");
                break;
            case R.id.btn_mine_purchase:
                PurchaseHistoryActivity.actionStart(mContext);
                break;
            case R.id.btn_mine_recorded:
                PlayHistoryActivity.actionStart(mContext);
                break;
            case R.id.btn_mine_settings:
                // 系统设置
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                break;
            default:
                break;
        }
    }


    @Override
    public void onGetFocus() {
        super.onGetFocus();
        btnAccount.requestFocus();
    }

    @Override
    public void onChildSelect(View child, boolean hasFocus) {

        View tag_bg = child.findViewWithTag("tag_bg");
        ImageView tag_title = (ImageView) child.findViewWithTag("tag_title");

        int id = child.getId();
        switch (id) {
            case R.id.btn_mine_collected:
//                ImageView img_haibao = (ImageView) child.findViewWithTag("img_haibao");

                if (!flag_shoucang) {
                    if (hasFocus) {
                        tag_bg.setBackgroundResource(R.drawable.bg_my_shoucang_f);
                        tag_title.setImageResource(R.drawable.ic_my_shoucang_title_f);
                    } else {
                        tag_bg.setBackgroundResource(R.drawable.bg_my_shoucang);
                        tag_title.setImageResource(R.drawable.ic_my_shoucang_title);
                    }
                }
                break;
            case R.id.btn_mine_recorded:
                if (!flag_play_recorder) {
                    if (hasFocus) {
                        tag_bg.setBackgroundResource(R.drawable.bg_my_playrecoder_f);
                        tag_title.setImageResource(R.drawable.ic_my_play_recorder_title_f);
                    } else {
                        tag_bg.setBackgroundResource(R.drawable.bg_my_playrecoder);
                        tag_title.setImageResource(R.drawable.ic_my_play_recorder_title);
                    }
                }
                break;
            case R.id.btn_mine_account:
                if (hasFocus) {
                    tag_bg.setBackgroundResource(R.drawable.bg_my_head_f);
                    tag_title.setImageResource(R.drawable.ic_my_acc_txt_f);
                } else {
                    tag_bg.setBackgroundResource(R.drawable.bg_my_head);
                    tag_title.setImageResource(R.drawable.ic_my_acc_txt);
                }
                break;
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETCOLLECTEDLIST:
                    if (result.code == TaskResult.SUCCESS) {
                        imgHaibaoCollect.setImageResource(R.drawable.bg_transparent);
                        CommonSearchInvokeResult<CollectionObj> invokeResult = (CommonSearchInvokeResult<CollectionObj>) result.data;
                        if (invokeResult != null) {
                            if (invokeResult.getRtList() != null && invokeResult.getRtList().size() > 0) {
                                txt_num_shoucang.setText(String.valueOf(invokeResult.getTotalNum()));
                                CollectionObj collectionObj = invokeResult.getRtList().get(0);
                                if (collectionObj != null) {
                                    String haibao = collectionObj.getHaibao();
                                    if (!TextUtils.isEmpty(haibao)) {
                                        flag_shoucang = true;
                                        shadow_shoucang.setVisibility(View.VISIBLE);
                                        Utility.displayImage(haibao, imgHaibaoCollect, R.drawable.bg_transparent);
                                    }
                                }
                            }
                        }
                    } else {

                    }
                    break;
                case Constants.TASK_GETPLAYHISTORY:
                    if(result.code == TaskResult.SUCCESS){
                        imgHaibaoRecorded.setImageResource(R.drawable.bg_transparent);
                        if (result.data != null) {
                            List<PlayHistoryObj> data = (List<PlayHistoryObj>) result.data;
                            if (data.size() > 0) {
                                txt_num_play.setText(String.valueOf(data.size()));
                                PlayHistoryObj playHistoryObj = data.get(0);
                                if (playHistoryObj != null) {
                                    String posterUrl = playHistoryObj.getPosterUrl();
                                    if (!TextUtils.isEmpty(posterUrl)) {
                                        flag_play_recorder = true;
                                        shadow_play_recorder.setVisibility(View.VISIBLE);
                                        Utility.displayImage(posterUrl, imgHaibaoRecorded, R.drawable.bg_transparent);
                                    }
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

}


