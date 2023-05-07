package com.ccdt.ottclient.ui.dialog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;

public class ImageDialogActivity extends Activity {
    private ImageView img;
    private View left;
    private View right;
    private ArrayList<String> list;
    private int currentIndex;
    private TextView txt_num;
    private int totalNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_image);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高

        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 1.0) - getStatusBarHeight();   //高度设置为屏幕的1.0
        p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的1.8
        p.alpha = 0.9f;        //设置本身透明度
        p.dimAmount = 0.9f;        //设置黑暗度

        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.CENTER);        //设置靠中心对齐

        img = ((ImageView) findViewById(R.id.img));
        left = findViewById(R.id.ic_arrow_left);
        right = findViewById(R.id.ic_arrow_right);
        txt_num = ((TextView) findViewById(R.id.txt_num));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            list = extras.getStringArrayList("urls");
        }

        currentIndex = 0;
        if (list != null && list.size() > 0) {
            totalNum = list.size();
            showImage();
        }
    }

    private void showImage() {
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        if (currentIndex > list.size() - 1) {
            currentIndex = list.size() - 1;
        }

        left.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);

        if (list != null && list.size() > 0) {
            if (currentIndex > 0) {
                left.setVisibility(View.VISIBLE);
            }

            if (currentIndex < list.size() - 1) {
                right.setVisibility(View.VISIBLE);
            }

            Utility.displayImage(list.get(currentIndex), img);
        }
        txt_num.setText(String.format("%d/%d", currentIndex + 1, totalNum));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (currentIndex > 0) {
                currentIndex--;
                showImage();
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (list != null) {
                if (currentIndex < list.size() - 1) {
                    currentIndex++;
                    showImage();
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void actionStart(Context context, ArrayList<String> urls) {
        Intent intent = new Intent(context, ImageDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("urls", urls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
