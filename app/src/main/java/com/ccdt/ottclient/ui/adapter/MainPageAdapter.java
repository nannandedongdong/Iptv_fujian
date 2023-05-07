package com.ccdt.ottclient.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.ui.activity.IIndicator;
import com.ccdt.ottclient.ui.fragment.AppstorePageFragment;
import com.ccdt.ottclient.ui.fragment.BaseIndicatorFragment;
import com.ccdt.ottclient.ui.fragment.LivePageFragment;
import com.ccdt.ottclient.ui.fragment.MyPageFragment;
import com.ccdt.ottclient.ui.fragment.NewsPageFragment;
import com.ccdt.ottclient.ui.fragment.PaikePageFragment;
import com.ccdt.ottclient.ui.fragment.VodPageFragment;

import java.util.ArrayList;
import java.util.List;

import viewpagerindicator.TabPageIndicator;

public class MainPageAdapter extends FragmentStatePagerAdapter implements TabPageIndicator.AdapterWithTabId {
    private static final String TAG = MainPageAdapter.class.getSimpleName();
    private List<Fragment> fragmentList;
    private int[] mIdArr;
    private List<LmInfoObj> oneLevelColumnList;
    private static final int LM_NUM_MAX = 6;
    private static final int LM_NUM_MIN = 2;

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public MainPageAdapter(FragmentManager fm, List<LmInfoObj> oneLevelColumnList, int[] idArr) {
        super(fm);
        this.mIdArr = idArr;
        this.oneLevelColumnList = oneLevelColumnList;
        fragmentList = new ArrayList<>();
        int customSize = 0;
        if (oneLevelColumnList != null && oneLevelColumnList.size() > 0) {
            customSize = oneLevelColumnList.size();
            if (customSize > LM_NUM_MAX - LM_NUM_MIN) {
                customSize = LM_NUM_MAX - LM_NUM_MIN;
            }
            BaseIndicatorFragment temp = null;
            for (int i = 0; i < customSize; i++) {
                LmInfoObj lmInfo = oneLevelColumnList.get(i);
                temp = getFragmentByType(lmInfo.getType());
                if (temp != null) {
                    if (i < mIdArr.length) {
                        temp.setLmInfo(lmInfo);
                        fragmentList.add(temp);
                        temp.setIndicatorTabId(mIdArr[i]);
                    }
                }
            }
        }

        Fragment appFragment = getFragmentByType(Constants.ONELEVEL_LM_TYPE_APP);
        ((IIndicator) appFragment).setIndicatorTabId(mIdArr[customSize]);
        fragmentList.add(appFragment);

        Fragment mineFragment = getFragmentByType(Constants.ONELEVEL_LM_TYPE_MINE);
        ((IIndicator) mineFragment).setIndicatorTabId(mIdArr[customSize + 1]);
        fragmentList.add(mineFragment);

    }

    @Override
    public CharSequence getPageTitle(int position) {

        int size = 0;
        if (oneLevelColumnList != null) {
            size = oneLevelColumnList.size();
        }
        if (size > LM_NUM_MAX - LM_NUM_MIN) {
            size = LM_NUM_MAX - LM_NUM_MIN;
        }

        String ret = "";
        if (position < size) {
            ret = oneLevelColumnList.get(position).getLmName();
        } else if (position == size) {
            ret = Constants.ONELEVEL_LM_TYPE_APP;
        } else if (position == size + 1) {
            ret = Constants.ONELEVEL_LM_TYPE_MINE;
        }
        return ret;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (fragmentList != null) {
            ret = fragmentList.size();
        }
        if (ret > LM_NUM_MAX) {
            ret = LM_NUM_MAX;
        }
        return ret;
    }

    @Override
    public int getTabId(int position) {
        int ret = 0;
        if (mIdArr != null) {
            ret = mIdArr[position];
        }
        return ret;
    }

    /**
     * 根据类型返回Fragment
     *
     * @param type 栏目类型
     * @return
     */

    private BaseIndicatorFragment getFragmentByType(String type) {
        BaseIndicatorFragment ret = null;
        if (Constants.ONELEVEL_LM_TYPE_LIVE.equals(type)) {
            ret = new LivePageFragment();
        } else if (Constants.ONELEVEL_LM_TYPE_VOD.equals(type)) {
            ret = new VodPageFragment();
        } else if (Constants.ONELEVEL_LM_TYPE_NEWS.equals(type)) {
            ret = new NewsPageFragment();
        } else if (Constants.ONELEVEL_LM_TYPE_PAIKE.equals(type)) {
            ret = new PaikePageFragment();
        } else if (Constants.ONELEVEL_LM_TYPE_APP.equals(type)) {
            ret = new AppstorePageFragment();
        } else if (Constants.ONELEVEL_LM_TYPE_MINE.equals(type)) {
            ret = new MyPageFragment();
        }

        return ret;
    }


}
