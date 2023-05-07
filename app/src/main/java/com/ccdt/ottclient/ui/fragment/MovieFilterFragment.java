package com.ccdt.ottclient.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.ui.activity.MovieListActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MovieFilterFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnKeyListener {
    private RadioGroup rgPaixu, rgNiandai, rgDiqu, rgLeixing;
    private Map<String, String> maps = new HashMap<>();

    private MovieListActivity mActivity;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = (MovieListActivity) getActivity();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String text = "";
        for (int i = 0; i < rgNiandai.getChildCount(); i++) {
            RadioButton child = ((RadioButton) rgNiandai.getChildAt(i));
            if (i == 0) {
                text = "全部";
            } else if (i == rgNiandai.getChildCount() - 1) {
                text = "其他";
            } else {
                text = String.valueOf(year + 1 - i);
            }
            if (child != null) {
                child.setText(text);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_filter, null);
//        Bitmap sentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fiter_bg);
//        view.setBackgroundDrawable(new BitmapDrawable(ImageUtil.fastblur(getActivity(),sentBitmap,10)));
        rgPaixu = ((RadioGroup) view.findViewById(R.id.rg_paixu));
        rgDiqu = ((RadioGroup) view.findViewById(R.id.rg_diqu));
        rgLeixing = ((RadioGroup) view.findViewById(R.id.rg_leixing));
        rgNiandai = ((RadioGroup) view.findViewById(R.id.rg_niandai));

        rgPaixu.getChildAt(0).setOnKeyListener(this);
        rgDiqu.getChildAt(0).setOnKeyListener(this);
        rgLeixing.getChildAt(0).setOnKeyListener(this);
        rgNiandai.getChildAt(0).setOnKeyListener(this);

        rgPaixu.setOnCheckedChangeListener(this);
        rgDiqu.setOnCheckedChangeListener(this);
        rgLeixing.setOnCheckedChangeListener(this);
        rgNiandai.setOnCheckedChangeListener(this);
        return view;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = (RadioButton) group.findViewById(checkedId);
        String filterTag = (String) button.getText();
        switch (group.getId()) {
            case R.id.rg_paixu:
                break;
            case R.id.rg_leixing:
                maps.put(ConstantKey.ROAD_TYPE_FCATEGORY, filterTag);
                break;
            case R.id.rg_niandai:
                maps.put(ConstantKey.ROAD_TYPE_YEAR, filterTag);
                break;
            case R.id.rg_diqu:
                maps.put(ConstantKey.ROAD_TYPE_ORIGIN, filterTag);
                break;
            default:
        }
        if(mActivity!=null){
            mActivity.filterList(maps);
        }


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            rgPaixu.requestFocus();
        } else {
            // 不知道为什么隐藏筛选菜单时调用此方法，先注释掉
//            if(mActivity!=null){
//                mActivity.filterList(maps);
//            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rgPaixu.requestFocus();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // 筛选菜单显示时，最左边按左方向键  不弹出左侧 菜单
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            return true;
        }

        return false;
    }
}
