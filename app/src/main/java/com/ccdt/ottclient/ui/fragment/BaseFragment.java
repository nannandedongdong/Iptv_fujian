package com.ccdt.ottclient.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.ui.activity.ILmInfo;

/**
 * Fragment 基类
 */
public abstract class BaseFragment extends Fragment implements ILmInfo {

    protected Context mContext;
    private LmInfoObj mLmInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public LmInfoObj getLmInfo() {
        return mLmInfo;
    }

    @Override
    public void setLmInfo(LmInfoObj lmInfo) {
        this.mLmInfo = lmInfo;
    }

    @Override
    public String getLmId() {
        String ret = null;
        if (mLmInfo != null) {
            ret = mLmInfo.getLmId();
        }
        return ret;
    }
}

