package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetNewsDetailTask;
import com.ccdt.ottclient.ui.dialog.ImageDialogActivity;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;

public class NewsDetailActivity extends BaseActivity implements TaskCallback {

    private static final String TAG = NewsDetailActivity.class.getName();
    public NewsInfoObj newsInfo;
    public ImageView news_img;
    public TextView txtTitle;
    private TextView txtContent;
    private int currentPosition;
    private ArrayList<NewsInfoObj> newsLists;
    private ImageView imgArrowLeft;
    private ImageView imgArrowRight;
    private ArrayList<String> urls;
    private View btnImg;
    private int imgNum;
    private TextView txtNum;
    private View imgOk;

    public static void actionStart(Context context, ArrayList<NewsInfoObj> newsList, int position) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putParcelableArrayListExtra("newsList", newsList);
        intent.putExtra("currentPosition", position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        newsLists = intent.getParcelableArrayListExtra("newsList");
        initData();
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initWidget() {
        txtNum = ((TextView) findViewById(R.id.txt_num));
        news_img = ((ImageView) findViewById(R.id.img_news));
        txtTitle = ((TextView) findViewById(R.id.txt_news_title));
        txtContent = ((TextView) findViewById(R.id.txt_news_content));
        imgArrowRight = ((ImageView) findViewById(R.id.img_arrow_right));
        imgArrowLeft = ((ImageView) findViewById(R.id.img_arrow_left));
        imgOk = findViewById(R.id.img_ok);
    }

    private void refreshImgArrow() {
        imgArrowLeft.setVisibility(View.INVISIBLE);
        imgArrowRight.setVisibility(View.INVISIBLE);

        if (newsLists != null) {
            int size = newsLists.size();
            if (size > 0) {
                if (currentPosition > 0) {
                    //  下标大于0 显示左箭头
                    imgArrowLeft.setVisibility(View.VISIBLE);
                }

                if (currentPosition + 1 < size) {
                    imgArrowRight.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            int taskId = result.taskId;
            int code = result.code;
            if (code == 200) {
                switch (taskId) {
                    case Constants.TASK_GETNEWSDETAIL:
                        newsInfo = (NewsInfoObj) result.data;
                        showData();
                        break;
                    default:

                        break;
                }
            } else {
                LogUtil.e(TAG, "信息详细信息加载失败  taskId:" + taskId + "  error:" + result.message);
            }


        }
    }

    private void initData() {
        refreshImgArrow();
        if (currentPosition >= 0 && currentPosition < newsLists.size()) {
            NewsInfoObj info = newsLists.get(currentPosition);
            if (info != null) {
                new GetNewsDetailTask(this).execute(info.getId());
            }
//            showData();
        }
    }

    private void showData() {
        imgNum = 0;
        String url = null;
        if (newsInfo != null) {
            txtTitle.setText(newsInfo.getMzName());
            String newsContent = newsInfo.getContent();
            if (!TextUtils.isEmpty(newsContent)) {
                txtContent.setText(Html.fromHtml(newsContent));
            }
            urls = newsInfo.getNewsImgs();
            if (urls != null && urls.size() > 0) {
                imgNum = urls.size();
                url = urls.get(0);
            }
        }
        Utility.displayImage(url, news_img);
        if(imgNum > 0){
            imgOk.setVisibility(View.VISIBLE);
        } else {
            imgOk.setVisibility(View.INVISIBLE);
        }
        txtNum.setText(String.format("1/%d", imgNum));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            // 方向键  左
            if (newsLists != null && currentPosition > 0) {
                currentPosition--;
                initData();
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            // 方向键  右
            if (newsLists != null && currentPosition + 1 < newsLists.size()) {
                currentPosition++;
                initData();
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if(imgNum > 0){
                ImageDialogActivity.actionStart(this, urls);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
