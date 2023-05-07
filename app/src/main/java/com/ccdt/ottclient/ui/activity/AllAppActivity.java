package com.ccdt.ottclient.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.AppObj;
import com.ccdt.ottclient.ui.adapter.AllAppAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllAppActivity extends BaseActivity implements View.OnClickListener {

    public static void actionStart(Context context){
        Intent intent = new Intent(context, AllAppActivity.class);
        context.startActivity(intent);
    }


    private AllAppAdapter adapter;
    private int selectionPosition = 0;
    private Dialog contentDia;
    private AppReceiver receiver;
    private TextView appCount;
    private int filter = FILTER_ALL_APP;
    private GridView appGrid;

    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
    private PackageManager pm;
    private List<AppObj> appListInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new AppReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_all_app;
    }

    @Override
    protected void initWidget() {

    }

    private void initView() {
        appGrid = (GridView) findViewById(R.id.app_grid_new);
        appListInfos = queryFilterAppInfo(filter);
        adapter = new AllAppAdapter(this, appListInfos);
        appGrid.setAdapter(adapter);
        appCount = (TextView) findViewById(R.id.app_count);
        appCount.setText("共" + adapter.getCount() + "个");
        appCount.setVisibility(View.VISIBLE);
    }

    private void initListener() {

        appGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectionPosition = position;
                bootAnApp(position);
            }
        });

        appGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                selectionPosition = position;
                showContentDia();
                return false;
            }
        });
    }

    private void refrushView() {
        appListInfos = queryFilterAppInfo(filter);
        adapter.changeData(appListInfos);
        appCount.setText("共" + adapter.getCount() + "个");
    }

    private void showContentDia() {
        appGrid.clearFocus();
        if (contentDia == null) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.app_dia_contentmenu, null);
            Button open = (Button) view.findViewById(R.id.app_boot);
            open.setOnClickListener(this);
            Button top = (Button) view.findViewById(R.id.app_top);
            top.setOnClickListener(this);
            Button detail = (Button) view.findViewById(R.id.app_detail);
            detail.setOnClickListener(this);
            Button delete = (Button) view.findViewById(R.id.app_delete);
            delete.setOnClickListener(this);

            contentDia = new Dialog(this);
            contentDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            contentDia.setContentView(view);
            Window dialogWindow = contentDia.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = 180;
            dialogWindow.setAttributes(lp);
        }
        contentDia.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_boot:
                bootAnApp(selectionPosition);
                break;
            case R.id.app_detail:
                showAppDetail(selectionPosition);
                break;
            case R.id.app_delete:
                unInstallApp(selectionPosition);
                break;
        }
        contentDia.dismiss();
        appGrid.requestFocus();
    }

    private void unInstallApp(int position) {
        Uri packageURI = Uri.parse("package:"
                + appListInfos.get(position).getPackageName());
        Intent unIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(unIntent);

    }

    private void bootAnApp(int position) {
        if (appListInfos.get(position) != null) {
            PackageManager pm = this.getPackageManager();
            String packageName = appListInfos.get(position).getPackageName();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            try {
                this.startActivity(intent);
            } catch (Exception e) {
                appListInfos.remove(position);
                adapter.changeData(appListInfos);
            }
        }
    }

    private void showAppDetail(int position) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", appListInfos.get(position)
                .getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        appListInfos = queryFilterAppInfo(item.getItemId());
        adapter.changeData(appListInfos);
        return super.onOptionsItemSelected(item);
    }

    // 根据查询条件，查询特定的ApplicationInfo
    private List<AppObj> queryFilterAppInfo(int filter) {
        pm = this.getPackageManager();
        // 查询所有已经安装的应用程序

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 本地的所有应用
        final List<ResolveInfo> localApp = pm
                .queryIntentActivities(mainIntent, 0);
        Collections.sort(localApp,
                new ResolveInfo.DisplayNameComparator(pm));// 排序
        List<AppObj> appInfos = new ArrayList<AppObj>(); // 保存过滤查到的AppInfo
        // 根据条件来过滤
        switch (filter) {
            case FILTER_ALL_APP: // 所有应用程序
                appInfos.clear();
                for (ResolveInfo app : localApp) {
                    if (getPackageName().equals(app.activityInfo.packageName)) {
                        continue;
                    }
                    appInfos.add(getAppInfo(app));
                }
                return appInfos;
            default:
                return null;
        }
    }

    // 构造一个AppInfo对象 ，并赋值
    private AppObj getAppInfo(ResolveInfo app) {
        AppObj appInfo = new AppObj();
        appInfo.setName((String) app.loadLabel(pm));
        appInfo.setIcon(app.loadIcon(pm));
        appInfo.setPackageName(app.activityInfo.packageName);
        return appInfo;
    }

    private class AppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("info", action);
            String packageName = intent.getDataString().substring(8);
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                refrushView();
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                refrushView();
            }
        }
    }
}
