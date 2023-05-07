package com.ccdt.ottclient.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.AppObj;
import com.ccdt.ottclient.ui.activity.AllAppActivity;
import com.ccdt.ottclient.ui.widget.AppPageItemLayout;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppstorePageFragment extends BaseIndicatorFragment {

    private View btn_app_all;
    private boolean hasAppStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_appstore_main, container, false);
        btn_app_all = root.findViewById(R.id.btn_app_all);
//        bigAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_big_common);
        return root;
    }

    /**
     * 设置点击和焦点事件
     */
    private void setClickAndFocusEvent() {
        for (int i = 0; i < btnArr.length; i++) {
            final int position = i;
            View v = root.findViewById(btnArr[position]);

            if (isFirstRow(position)) {
                LogUtil.d(TAG, "getIndicatorTabId() - " + getIndicatorTabId());
                v.setNextFocusUpId(getIndicatorTabId());
            }

            int dataPosition = position - 1;
            if (dataPosition >= 0) {
                if (mAppList.size() > dataPosition) {
                    AppObj appObj = mAppList.get(dataPosition);
                    if (appObj != null) {
                        ((AppPageItemLayout) v).setIconDrawable(appObj.getIcon());
                        ((AppPageItemLayout) v).setName(appObj.getName());
                    }
                } else {
                    v.setVisibility(View.GONE);
                }
            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener(position);
                }
            });
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppList.clear();
        getInstalledApp();
        setClickAndFocusEvent();
    }

    /**
     * 取所有app
     *
     * @return
     */
    public List<AppObj> getInstalledApp() {
        hasAppStore = false;
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = mContext.getPackageManager();
        final List<ResolveInfo> localApp = packageManager
                .queryIntentActivities(mainIntent, 0);
        Collections.sort(localApp, new ResolveInfo.DisplayNameComparator(
                packageManager));// 排序
        for (ResolveInfo app : localApp) {

            AppObj appObj = getAppInfo(app);

            if (mContext.getPackageName().equals(appObj.getPackageName())) {
                continue;
            }

            if ("com.ccdt.itvision.appstore".equals(appObj.getPackageName())) {
                mAppList.add(0, appObj);
                hasAppStore = true;
                continue;
            }

            mAppList.add(appObj);
        }

        if (!hasAppStore) {
            mAppList.add(0, null);
        }

        return mAppList;
    }

    /**
     * 判断是否是第一行，用于设置第一行向上的焦点
     *
     * @param position
     * @return
     */
    private boolean isFirstRow(int position) {
        boolean ret = false;
        switch (position) {
            case 0:
            case 3:
            case 5:
            case 7:
            case 9:
            case 13:
                ret = true;
                break;
            default:
                ret = false;
                break;
        }
        return ret;
    }

    /**
     * 点击事件
     *
     * @param position
     */
    public void onItemClickListener(int position) {
        switch (position) {
            case 0:
                AllAppActivity.actionStart(mContext);
                break;
//            case 1:
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                ComponentName componentName = new ComponentName(
//                        "com.ccdt.itvision.appstore",
//                        "com.ccdt.itvision.appstore.ui.home.StoreHomeActivity");
//                intent.setComponent(componentName);
//                try {
//                    if (getActivity().getPackageManager().getPackageInfo(
//                            "com.ccdt.itvision.appstore", 0) != null) {
//                        getActivity().startActivity(intent);
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    ToastUtil.ToastMessage(mContext, getString(R.string.have_not_install_app_shop));
//                    e.printStackTrace();
//                }
//                break;
            default:
                AppObj appObj = mAppList.get(position - 1);
                if (appObj == null) {
                    ToastUtil.toast("应用未安装");
                    return;
                } else {
                    Intent intent1 = packageManager
                            .getLaunchIntentForPackage(appObj.getPackageName());
                    startActivity(intent1);
                }
                break;
        }
    }

    private AppObj getAppInfo(ResolveInfo app) {
        AppObj appInfo = new AppObj();
        appInfo.setName((String) app.loadLabel(packageManager));
        appInfo.setIcon(app.loadIcon(packageManager));
        appInfo.setPackageName(app.activityInfo.packageName);
        return appInfo;
    }

    //    private static final int DEFAULT_APP_COUNT_MINI = 2;
    private static final String TAG = AppstorePageFragment.class.getSimpleName();
    private PackageManager packageManager;
    private List<AppObj> mAppList = new ArrayList<>();
    public View root;
    //    public Animation bigAnim;
    private int[] btnArr = {
            R.id.btn_app_all,
            R.id.btn_app_store,
            R.id.btn_app_0,
            R.id.btn_app_1,
            R.id.btn_app_2,
            R.id.btn_app_3,
            R.id.btn_app_4,
            R.id.btn_app_5,
            R.id.btn_app_6,
            R.id.btn_app_7,
            R.id.btn_app_8,
            R.id.btn_app_9,
            R.id.btn_app_10,
            R.id.btn_app_11,
            R.id.btn_app_12,
            R.id.btn_app_13,
            R.id.btn_app_14

    };

    @Override
    public void onGetFocus() {
        super.onGetFocus();
        btn_app_all.requestFocus();
    }
}

