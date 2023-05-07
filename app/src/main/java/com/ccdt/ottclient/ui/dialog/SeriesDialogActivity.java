package com.ccdt.ottclient.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.JuJiInfo;
import com.ccdt.ottclient.provider.SQLDataOperationMethod;
import com.ccdt.ottclient.ui.activity.BaseActivity;
import com.ccdt.ottclient.ui.adapter.SeriesGridAdapter;
import com.ccdt.ottclient.utils.FastBlur;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * 选集弹出框
 * Created by ccdt on 2015/9/25.
 */
public class SeriesDialogActivity extends BaseActivity {

    private static final int GROUP_COUNT = 10;

    private Context mContext;
    private TextView mCount;
    private GridView mSeries;
    private RadioGroup mRadioGroup;
    private int seriesPosition = 1;//默认播放第一集
    private SeriesGridAdapter mSeriesGridAdapter;

    private ArrayList<JuJiInfo> juJiInfos;
    private int allSeries;
    private int realSeries;
    private String title;
    private String posterUrl;
    private String mzId;
    private View root;
    private Bitmap mBitmap;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
        mContext = this;
        Bundle bundle = getIntent().getBundleExtra("isSeriesInfo");
        if (bundle != null) {
            juJiInfos = bundle.getParcelableArrayList("JujiInfos");
            allSeries = bundle.getInt("allSeries");
            realSeries = bundle.getInt("realSeries");
            title = bundle.getString("title");
            posterUrl = bundle.getString("posterUrl");
            mzId = bundle.getString("mzId");
            seriesPosition = SQLDataOperationMethod.searchSeriesPosition(this, mzId);
        }

        initOperator();

        if (juJiInfos != null && juJiInfos.size() != 0) {
            divideIntoGroups();
        }

//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vod_series_dialog_bg);
//        ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
//        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                blur(mBitmap, root);
//                return true;
//            }
//        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dialog_series;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initWidget() {
        root = findViewById(R.id.root);
        mCount = (TextView) findViewById(R.id.series_count_tv);
        mSeries = (GridView) findViewById(R.id.series_gv);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    }


    private void initOperator() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View view = group.findViewById(checkedId);
                List<JuJiInfo> subsetInfos = (List<JuJiInfo>) view.getTag();
                mSeriesGridAdapter = new SeriesGridAdapter(mContext,
                        subsetInfos, seriesPosition);
                mSeries.setAdapter(mSeriesGridAdapter);
            }
        });
        mSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                JuJiInfo juJiInfo = (JuJiInfo) parent.getItemAtPosition(position);
                if (juJiInfo != null) {
                    Log.d("hezb", "jujiinfo:" + juJiInfo.juJiNum);
                    seriesPosition = juJiInfo.juJiNum;
                    Utility.intoPlayerActivity(mContext, title, posterUrl, mzId, seriesPosition, juJiInfos);
                    mSeriesGridAdapter.setSeriesItem(seriesPosition);
                }
            }
        });
    }

    private void addRadioButton(int i, List<JuJiInfo> list) {
        String title;
        if (list.size() % GROUP_COUNT == 0) {
            title = (i * GROUP_COUNT + 1) + "-" + (i + 1) * GROUP_COUNT;
        } else {
            title = (i * GROUP_COUNT + 1) + "-" + (i * GROUP_COUNT + list.size());
        }
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setButtonDrawable(android.R.color.transparent);
        radioButton.setText(title);
        radioButton.setTextColor(mContext.getResources()
                .getColorStateList(R.color.selector_color_series));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        radioButton.setLines(1);
        radioButton.setTag(list);
        int padding = mContext.getResources()
                .getDimensionPixelOffset(R.dimen.detail_radiobutton_padding);
        radioButton.setPadding(padding, padding, padding, padding);
        radioButton.setNextFocusDownId(R.id.series_gv);
        mRadioGroup.addView(radioButton, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 分组
     *
     * @author hezb
     * @date 2015年6月10日下午2:45:16
     */
    private void divideIntoGroups() {
        List<List<JuJiInfo>> groupList = new ArrayList<List<JuJiInfo>>();
        List<JuJiInfo> temp = null;
        for (int i = 0; i < juJiInfos.size(); i++) {
            if (i % GROUP_COUNT == 0) {
                temp = new ArrayList<JuJiInfo>();
                groupList.add(temp);
            }
            temp.add(juJiInfos.get(i));
//            if (!TextUtils.isEmpty(juJiInfos.get(i).getChapter())) {//转为整形
//                juJiInfos.get(i).juJiNum = Integer.parseInt(juJiInfos.get(i).getChapter());
//            } else {
//                juJiInfos.get(i).juJiNum = i + 1 ;
//            }
        }
        if (groupList.size() != 0) {
            for (int i = 0; i < groupList.size(); i++) {
                addRadioButton(i, groupList.get(i));
            }
            mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
            if (realSeries >= allSeries) {
                mCount.setText(getString(R.string.all_series, realSeries));
            } else {
                mCount.setText(getString(R.string.update_series, realSeries));
            }
        }
    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 2;

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));
        System.out.println(System.currentTimeMillis() - startMs + "ms");
    }


}