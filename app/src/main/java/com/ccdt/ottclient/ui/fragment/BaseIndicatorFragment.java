package com.ccdt.ottclient.ui.fragment;

import com.ccdt.ottclient.ui.activity.IIndicator;
import com.ccdt.ottclient.utils.LogUtil;

public abstract class BaseIndicatorFragment extends BaseFragment implements IIndicator {
    private static final String TAG = BaseIndicatorFragment.class.getName();
    private int mIndicatorTabId;


    @Override
    public void setIndicatorTabId(int id) {
        this.mIndicatorTabId = id;
    }

    public int getIndicatorTabId() {
        return this.mIndicatorTabId;
    }

    @Override
    public void onGetFocus() {
        LogUtil.d("slf", TAG + "  从Indicator处获取焦点");
    }
}
