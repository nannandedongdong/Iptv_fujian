package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.JuJiInfo;
import com.ccdt.ottclient.model.OrderObj;
import com.ccdt.ottclient.model.ProductInfo;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.model.VodRecommendObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.ActionOperateTask;
import com.ccdt.ottclient.tasks.impl.GetVodAuthTask;
import com.ccdt.ottclient.tasks.impl.GetVodDetailTask;
import com.ccdt.ottclient.ui.adapter.MovieDetailAdapter;
import com.ccdt.ottclient.ui.adapter.VodDetailAdapter;
import com.ccdt.ottclient.ui.dialog.PayDialogActivity;
import com.ccdt.ottclient.ui.dialog.VODBuyDialogActivity;
import com.ccdt.ottclient.ui.widget.MovieDetailLinearLayout;
import com.ccdt.ottclient.ui.widget.SpacesItemDecoration;
import com.ccdt.ottclient.utils.StringUtil;
import com.ccdt.ottclient.utils.ToastUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;

public class VodDetailActivity extends BaseActivity
        implements View.OnFocusChangeListener, View.OnClickListener,
        TaskCallback, AdapterView.OnItemClickListener, VodDetailAdapter.OnItemClickListener {

    private static final String TAG = "VodDetailActivity";

    private static final int SCREEN_SCROLL_TIME = 1000;

    private int keyCode = -1;
    private Button btnCollect;
    private MovieDetailLinearLayout rootLayout;
    private Button btnBuyOrPlay;
    private ImageView iv_show;
    private TextView txtMzName;
    private TextView txtDirector;
    private TextView txtActors;
    private TextView txtMzDesc;
    private TextView txtCategory;
    private TextView txtRunTime;
    private RatingBar rtbStar;
    private List<VodRecommendObj> recommendObjs = new ArrayList<>();
    private VodDetailAdapter adapter;
    private VodDetailObj mVodDetail;
    private ProductInfo mProductInfo;
    private int moveWidth;
    private boolean isLogin;
    private boolean isFree;
    private boolean mHasCollected;
    private ProductInfo productInfo;
    private Bundle seriesBundle;
    private ArrayList<JuJiInfo> juJiInfos;
    private View btnSeries;
    private TextView txtYear;
    private TextView txtOrigin;
    private TextView txtLanguage;
    private View layoutMoreRight;
    private View layoutMoreLeft;
    private View viewVerticalBar;
    private String mzId;
    private RecyclerView mGrid;
    private MovieDetailAdapter.ViewHolder focusedHolder;
    private int colorTextUnselected;
    private Drawable drawableBuyF;
    private Drawable drawableBuy;
    private Drawable drawablePlay;
    private Drawable drawablePlayF;
    private GridLayoutManager layoutManager;
    //    private Drawable selectorBuy;
//    private Drawable selectorPlay;

    public static void actionStart(Context context, String mzId) {
        Intent intent = new Intent(context, VodDetailActivity.class);
        intent.putExtra("mzId", mzId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorTextUnselected = this.getResources().getColor(R.color.text_unselected_vod);
        Intent intent = getIntent();
        mzId = intent.getStringExtra("mzId");
        if (!TextUtils.isEmpty(Account.getInstance().userId)) {
            isLogin = true;
        }

//        btnCollect.setOnFocusChangeListener(this);
        btnBuyOrPlay.setOnClickListener(this);
        btnBuyOrPlay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeBuyOrPlayDrawableLeft(hasFocus);
            }
        });
        btnCollect.setOnClickListener(this);
        drawableBuy = getResources().getDrawable(R.drawable.ic_movie_detail_buy);
        drawableBuy.setBounds(0, 0, drawableBuy.getMinimumWidth(), drawableBuy.getMinimumHeight());
        drawableBuyF = getResources().getDrawable(R.drawable.ic_movie_detail_buy_focused);
        drawableBuyF.setBounds(0, 0, drawableBuyF.getMinimumWidth(), drawableBuyF.getMinimumHeight());
        drawablePlay = getResources().getDrawable(R.drawable.ic_play);
        drawablePlay.setBounds(0, 0, drawablePlay.getMinimumWidth(), drawablePlay.getMinimumHeight());
        drawablePlayF = getResources().getDrawable(R.drawable.ic_play_f);
        drawablePlayF.setBounds(0, 0, drawablePlayF.getMinimumWidth(), drawablePlayF.getMinimumHeight());
//        selectorBuy = getResources().getDrawable(R.drawable.selector_movie_detail_buy);
//        selectorBuy.setBounds(0, 0, selectorBuy.getMinimumWidth(), selectorBuy.getMinimumHeight());
//        selectorPlay = getResources().getDrawable(R.drawable.selector_movie_detail_play);
//        selectorBuy.setBounds(0, 0, selectorBuy.getMinimumWidth(), selectorBuy.getMinimumHeight());

        showMidMsg(false);
        executeTask();
    }

    private void changeBuyOrPlayDrawableLeft(boolean hasFocus) {
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (hasFocus) {
            if (isFree) {
                btnBuyOrPlay.setCompoundDrawables(drawablePlayF, null, null, null);
            } else {
                btnBuyOrPlay.setCompoundDrawables(drawableBuyF, null, null, null);
            }
        } else {
            if (isFree) {
                btnBuyOrPlay.setCompoundDrawables(drawablePlay, null, null, null);
            } else {
                btnBuyOrPlay.setCompoundDrawables(drawableBuy, null, null, null);
            }
        }
    }

    private void executeTask() {

        resetController();

        Map<String, String> params = new HashMap<>();

        params.put(ConstantKey.ROAD_TYPE_ID, mzId);//资产id
        params.put(ConstantKey.ROAD_TYPE_ISRECOMMEND, "true");
        params.put(ConstantKey.ROAD_TYPE_RECOMMENDNUM, "12");
        params.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
        params.put(ConstantKey.ROAD_TYPE_RECOMMEND_SHOWFIELDS, ConstantValue.SHOWFIELDS_RECOMMEND);
        params.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, ConstantValue.SHOWFIELDS_VOD_DETAIL);

        new GetVodDetailTask(this).execute(params);
        new GetVodAuthTask(this).execute(mzId);
    }

    private void resetController() {
        btnBuyOrPlay.setEnabled(false);
        btnSeries.setEnabled(false);
        btnSeries.setVisibility(View.GONE);
        btnCollect.setEnabled(false);
    }


    /**
     * @param flag true 显示影片详情  false 显示相关影片
     */
    private void showMidMsg(boolean flag) {
        layoutMoreRight.setVisibility(View.INVISIBLE);
        layoutMoreLeft.setVisibility(View.INVISIBLE);

        if (flag) {
            layoutMoreLeft.setVisibility(View.VISIBLE);
        } else {
            layoutMoreRight.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_vod_detail;
    }

    @Override
    public void initWidget() {
        btnCollect = ((Button) findViewById(R.id.btnCollect));
        btnBuyOrPlay = ((Button) findViewById(R.id.btnBuyOrPlay));
        rootLayout = (MovieDetailLinearLayout) findViewById(R.id.rootLayout);
        iv_show = ((ImageView) findViewById(R.id.iv_show));
        txtMzName = ((TextView) findViewById(R.id.txtMzName));
        txtDirector = (TextView) findViewById(R.id.txtDirector);
        txtActors = ((TextView) findViewById(R.id.txtActors));
        txtMzDesc = ((TextView) findViewById(R.id.txtMzDesc));
        txtCategory = ((TextView) findViewById(R.id.txtCategory));
        txtRunTime = ((TextView) findViewById(R.id.txtRunTime));
        rtbStar = ((RatingBar) findViewById(R.id.rtbStar));
        btnSeries = findViewById(R.id.btnSeries);
        txtYear = ((TextView) findViewById(R.id.txt_year));
        txtOrigin = ((TextView) findViewById(R.id.txt_origin));
        txtLanguage = ((TextView) findViewById(R.id.txt_language));
        layoutMoreRight = findViewById(R.id.layoutMoreRight);
        layoutMoreLeft = findViewById(R.id.layoutMoreLeft);
        viewVerticalBar = findViewById(R.id.viewVerticalBar);

        btnBuyOrPlay.post(new Runnable() {
            @Override
            public void run() {
                btnBuyOrPlay.requestFocus();
            }
        });

        btnSeries.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //选集
                if (seriesBundle != null) {
                    Utility.showSeriesDialog(VodDetailActivity.this, seriesBundle);
                }
            }
        });

        btnCollect.setOnFocusChangeListener(this);
        initGrid();
    }

    private void initGrid() {
        mGrid = ((RecyclerView) findViewById(R.id.grid));
        int spacingInPixels = 4;
        mGrid.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        adapter = new VodDetailAdapter(this, recommendObjs);

        layoutManager = new GridLayoutManager(this, 6);
        mGrid.setLayoutManager(layoutManager);
        mGrid.setAdapter(adapter);
//        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Utility.showToast(VodDetailActivity.this, "position="+position);
//            }
//        });
//
//        mGrid.post(new Runnable() {
//            @Override
//            public void run() {
//                btnBuyOrPlay.requestFocus();
//            }
//        });
        adapter.setOnItemClickListener(this);
//        mGrid.setItemScaleValue(1.05f, 1.05f);
//        mGrid.setFocusResId(R.drawable.bg_transparent);
//        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
//        mGrid.setFocusViewId(R.id.layout_focus);
//        mGrid.setAutoChangeLine(false);
//        mGrid.setOnItemClickListener(this);
//
//        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
//            @Override
//            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
//                Log.d("slf "+TAG, "setOnItemSelectedListener " + paramBoolean);
//                if (paramBoolean) {
//                    if (paramView1 != null) {
//                        Object tag = paramView1.getTag();
//                        if (tag != null) {
//                            if (tag instanceof MovieDetailAdapter.ViewHolder) {
//                                focusedHolder = (MovieDetailAdapter.ViewHolder) tag;
//                                focusedHolder.txtTitle.setTextColor(Color.WHITE);
//                                focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_blue);
//                            }
//                        }
//                    }
//                } else {
//                    if (focusedHolder != null) {
//                        focusedHolder.txtTitle.setTextColor(colorTextUnselected);
//                        focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_black);
//                    }
//                    focusedHolder = null;
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VodRecommendObj vodInfo = recommendObjs.get(position);
        if (vodInfo != null) {
            Utility.intoVODDetailActivity(this, vodInfo.getId());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.keyCode = keyCode;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        moveWidth = rootLayout.getChildAt(0).getWidth() - rootLayout.getWidth();
        if (hasFocus) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                showMidMsg(false);
                scrollToLeft();
            }
        } else {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                showMidMsg(true);
//                mGrid.setSelection(0);

                scrollToRight();
            }
        }
    }

    /**
     * 向左滚动
     */
    private void scrollToLeft() {
        Log.d("slf " + TAG, "scrollToLeft");
        rootLayout.startScroll(moveWidth, 0, -moveWidth, 0, SCREEN_SCROLL_TIME);
    }

    /**
     * 向右滚动
     */
    private void scrollToRight() {
        Log.d("slf " + TAG, "scrollToRight");
        rootLayout.startScroll(0, 0, moveWidth, 0, SCREEN_SCROLL_TIME);
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                View v = layoutManager.findViewByPosition(0);
                if (v != null) {
                    v.requestFocus();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBuyOrPlay:
                // 购买/播放
                if (mVodDetail != null && isLogin) {
                    if (isFree) {
                        // 可以播放 免费 或 已购买
                        if (seriesBundle != null && juJiInfos != null && juJiInfos.size() > 0) {
                            Utility.intoPlayerActivity(
                                    this,
                                    mVodDetail.getMzName(),
                                    mVodDetail.getShowUrl(),
                                    mVodDetail.getId(),
                                    0,
                                    mVodDetail.getJuji()
                            );
                        }
                    } else {
                        // 购买
                        if (productInfo != null) {
                            String status = productInfo.getMessage().getStatus();
                            if ("10".equals(status)) {
                                ToastUtil.toast("已生成订单，请到购买记录界面支付！");
                            } else if ("1".equals(status)) {
                                Intent intent = new Intent(this, VODBuyDialogActivity.class);
                                intent.putExtra("productInfo", productInfo);
                                intent.putExtra("assetId", mVodDetail.getId());
                                startActivityForResult(intent, 888);
                            }
                        } else {
//                            Utility.showToast(this, getString(R.string.vod_auth_error));
                        }
                    }
                }

                break;
            case R.id.btnCollect:
                if (mVodDetail != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put(ConstantKey.ROAD_TYPE_MZID, mVodDetail.getId());
                    // 收藏
                    if (mHasCollected) {// 已收藏则删除，为收藏则添加x
                        // 点击“已收藏”，删除收藏操作
                        map.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.delCollect.name());
                    } else {
                        // 点击“收藏”，添加收藏操作
                        map.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.addCollect.name());
                    }
                    new ActionOperateTask(this).execute(map);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 888 && resultCode == 8888) {

            int code = data.getIntExtra("code", -999);
            String message = data.getStringExtra("message");
            if (code == -999) {
                ToastUtil.toast(R.string.buy_product_error);
                return;
            }

            if (code == 200) {
                OrderObj order = data.getParcelableExtra("order");
                ToastUtil.toast("订购成功，请支付");
                Intent intent = new Intent(this, PayDialogActivity.class);
                intent.putExtra("order", order);
                startActivityForResult(intent, 999);
            } else {
                ToastUtil.toast(message);
            }

            new GetVodAuthTask(this).execute(mzId);

        } else if (requestCode == 999 && resultCode == 999) {
            // 购买成功！
//            btnBuyOrPlay.setText("播放");
            int code = data.getIntExtra("code", -999);
            if (200 == code) {
                isFree = true;
            }
            new GetVodAuthTask(this).execute(mzId);
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETVODDETAIL:
                    if (result.code == TaskResult.SUCCESS) {
                        mVodDetail = (VodDetailObj) result.data;
                        showData();
                    }
                    break;
                case Constants.TASK_GETVODAUTH:
                    isFree = false;
//                    if(result.code == TaskResult.SUCCESS){
                    productInfo = (ProductInfo) result.data;
                    Log.d("slf", "productInfo = " + productInfo.toString());
                    switch (Integer.valueOf(productInfo.getMessage().getStatus())) {
                        case 0:
                        case 5:
                            // 已购买 免费
                            btnBuyOrPlay.setEnabled(true);
                            btnBuyOrPlay.setText("播放");
                            isFree = true;
                            changeBuyOrPlayDrawableLeft(btnBuyOrPlay.hasFocus());
                            break;
                        case 1:
                            // 马上购买
                            btnBuyOrPlay.setEnabled(true);
                            btnBuyOrPlay.setText("购买");
                            changeBuyOrPlayDrawableLeft(btnBuyOrPlay.hasFocus());
                            break;
                        case 10:
                            // 未支付
                            btnBuyOrPlay.setEnabled(true);
                            btnBuyOrPlay.setText("支付");
                            changeBuyOrPlayDrawableLeft(btnBuyOrPlay.hasFocus());
                            break;
                        default:
                            btnBuyOrPlay.setEnabled(false);
                            switch (result.code) {
                                case 2:
                                    // 产品下架
                                    break;
                                case 3:
                                    // 票据已过期，请重新登录
                                    break;
                                case 407:
                                    // 参数缺失
                                    break;
                                case 500:
                                    // 系统异常
                                    break;
                                default:

                                    break;
                            }
                            break;

                    }
//                    } else {
//                        ToastUtil.toastLong("该产品已下架");
//                    }


                    break;
                case Constants.TASK_ACTIONOPERATE:
                    String toastMessage = "操作失败";
                    if ("1".equals(String.valueOf(result.data))) {
                        mHasCollected = !mHasCollected;
                        showTxtCollect();
                        if (mHasCollected) {
                            toastMessage = "添加收藏成功";
                        } else {
                            toastMessage = "删除收藏成功";
                        }
                    }
                    ToastUtil.toast(toastMessage);

                    break;
            }

        }
    }

    private Handler handler = new Handler();

    private void showData() {

        if (TextUtils.isEmpty(mVodDetail.getId())) {
            ToastUtil.toastLong("该产品已下架");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    VodDetailActivity.this.finish();
                }
            }, 2000);
            return;
        }

        this.recommendObjs.clear();

        String strMzName = "";
        String strRunTime = "";
        String strCategory = "";
        String strStar = "";
        String strDirector = "";
        String strActors = "";
        String strVodType = "";
        String strMzDesc = "";
        String strUpdateTime = "";
        String strPubTime = "";
        String strShowUrl = "";
        String ifCollect = "";
        String year = "";
        String origin = "";
        String language = "";
        int jishu = 0;
        int realJishu = 0;
        List<VodRecommendObj> recommends = null;

        String strDefault = "未知";


        int runTime = 0;
        int star = 0;

        if (mVodDetail != null) {
            strShowUrl = mVodDetail.getShowUrl();
            strMzName = StringUtil.getNotNullStr(mVodDetail.getMzName(), strDefault);
            strDirector = StringUtil.getNotNullStr(mVodDetail.getDirector(), strDefault);
            strMzDesc = StringUtil.getNotNullStr(mVodDetail.getMzDesc(), strDefault);
            strActors = StringUtil.getNotNullStr(mVodDetail.getActors(), strDefault);
            strCategory = StringUtil.getNotNullStr(mVodDetail.getCategory(), strDefault);
            strVodType = StringUtil.getNotNullStr(mVodDetail.getVodType(), strDefault);
            year = StringUtil.getNotNullStr(mVodDetail.getYear(), year);
            language = StringUtil.getNotNullStr(mVodDetail.getLanguage(), strDefault);
            origin = StringUtil.getNotNullStr(mVodDetail.getOrigin(), strDefault);
            strRunTime = StringUtil.getNotNullStr(mVodDetail.getRunTime(), "0");
            strStar = StringUtil.getNotNullStr(mVodDetail.getStar(), "0");
            ifCollect = StringUtil.getNotNullStr(mVodDetail.getIfCollect(), "0");
            jishu = Integer.valueOf(StringUtil.getNotNullStr(mVodDetail.getJishu(), "0"));
            realJishu = Integer.valueOf(StringUtil.getNotNullStr(mVodDetail.getRealJishu(), "0"));
            recommends = mVodDetail.getRecommends();


            runTime = Integer.valueOf(strRunTime) / 60;
            star = Integer.valueOf(strStar);

            juJiInfos = mVodDetail.getJuji();
            seriesBundle = new Bundle();
            seriesBundle.putParcelableArrayList("JujiInfos", juJiInfos);
            seriesBundle.putString("title", mVodDetail.getMzName());
            seriesBundle.putString("posterUrl", mVodDetail.getShowUrl());
            seriesBundle.putString("mzId", mVodDetail.getId());
            if ("0".equals(mVodDetail.getVodType())) {
                //电视剧
                int allSeries = mVodDetail.getJuji().size();
                int realSeries = allSeries;
                if (jishu > 0 && realJishu > 0) {
                    allSeries = jishu;
                    realSeries = realJishu;
                }
                seriesBundle.putInt("allSeries", allSeries);
                seriesBundle.putInt("realSeries", realSeries);
                for (int i = 0; i < allSeries; i++) {
                    if (!TextUtils.isEmpty(juJiInfos.get(i).getChapter())) {//转为整形
                        juJiInfos.get(i).juJiNum = Integer.parseInt(juJiInfos.get(i).getChapter());
                    } else {
                        juJiInfos.get(i).juJiNum = i + 1;
                    }
                }
                btnSeries.setEnabled(true);
                btnSeries.setVisibility(View.VISIBLE);
            }
        }

        txtMzName.setText(strMzName);
        txtMzDesc.setText(String.format("%s%s", "        ", strMzDesc));
        txtDirector.setText(String.format("导演：%s", strDirector));
        txtActors.setText(String.format("主演：%s", strActors));
        txtCategory.setText(String.format("类型：%s", strCategory));
        txtRunTime.setText(String.format("片长：%d分钟", runTime));
        txtYear.setText(String.format("上映日期：%s", year));
        txtLanguage.setText(String.format("语言：%s", language));
        txtOrigin.setText(String.format("制片国家/地区：%s", origin));
//        rtbStar.setNumStars(star);
        rtbStar.setRating(star);
//        ImageLoader.getInstance().displayImage(strShowUrl, iv_show);

        mHasCollected = "1".equals(ifCollect);
        Utility.displayImage(strShowUrl, iv_show);

        if (recommends != null) {
            for (VodRecommendObj rec : recommends) {
                this.recommendObjs.add(rec);
            }
        }

        showTxtCollect();

        if (juJiInfos == null || juJiInfos.size() <= 0) {
            ToastUtil.toast("剧集获取失败");
            btnBuyOrPlay.setEnabled(false);
        }

        adapter.notifyDataSetChanged();
    }

    private void showTxtCollect() {
        btnCollect.setEnabled(true);
        btnCollect.setText(mHasCollected ? "已收藏" : "收藏");

        int resId = R.drawable.ic_movie_detail_collect;
        if (mHasCollected) {
            resId = R.drawable.ic_movie_detail_collected;
        }
        btnCollect.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }


//    @Override
//    public void onItemClick(int position) {
//        if (recommendObjs != null && position < recommendObjs.size()) {
//            VodRecommendObj recObj = recommendObjs.get(position);
//            if (recommendObjs != null) {
//                mzId = recObj.getId();
//                executeTask();
//                scrollToLeft();
//                btnBuyOrPlay.requestFocus();
//            }
//        }
//    }

    @Override
    public void onItemClick(View v, int position) {
        if (recommendObjs != null && position < recommendObjs.size()) {
            VodRecommendObj recObj = recommendObjs.get(position);
            if (recommendObjs != null) {
//                mzId = recObj.getId();

                actionStart(this, recObj.getId());

//                executeTask();
//                scrollToLeft();
//                btnBuyOrPlay.requestFocus();
            }
        }
    }
}
