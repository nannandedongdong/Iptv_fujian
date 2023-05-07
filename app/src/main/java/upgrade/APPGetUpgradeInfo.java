package upgrade;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetLastVersionTask;
import com.ccdt.ottclient.utils.SPUtils;
import com.ccdt.ottclient.utils.SharedPrefsConfig;
import com.ccdt.ottclient.utils.ToastUtil;

import java.util.Map;

/**
 * @Package com.ccdt.news.utils
 * @ClassName: APPUpgrade
 * @Description: 获取升级接口数据
 * @author hezb
 * @date 2015年5月9日 下午6:38:32
 */

public class APPGetUpgradeInfo implements TaskCallback {

    private Context context;
    private Boolean ifAuto;

    /**
     * ifAuto 是否是自动检测
     */
    public APPGetUpgradeInfo(Context context, Boolean ifAuto) {
        this.context = context;
        this.ifAuto = ifAuto;
    }
    /**
     * 执行获取
     */
    public void run() {
        if (!ifAuto) {
            ToastUtil.toast("正在连接升级服务器");
        }
//        requestManager.execute(request, this);
        new GetLastVersionTask(this).execute();
    }




    
    private void onRequestError() {
        if (!ifAuto) {
            ToastUtil.toast("检测升级服务器地址为空!");
        }
    }
    
    
    private void checkVersionUpdate(Map<String, String> map) {
        String packageName = map.get(UPConstants.UPGRADE_APPPACKAGE);
        PackageInfo packageInfo = getPackageInfo(packageName);
        if (packageInfo == null) {
            return;
        }
        
        int currentVersion = packageInfo.versionCode;// 客户端版本
        int serverVersion = 0;
        try {
            serverVersion = Integer.parseInt(
                    map.get(UPConstants.UPGRADE_VERSIONCODE));// 服务器版本
        } catch (Exception e) {
            
        }

        if (currentVersion >= serverVersion) {
            SPUtils.put(context,
                    SharedPrefsConfig.APP_IS_LATEST_VERSION,
                    true);
            return;
        } else {
            SPUtils.put(context,
                    SharedPrefsConfig.APP_IS_LATEST_VERSION,
                    false);
        }

        if (map.containsKey(UPConstants.UPGRADE_APKURL)) {
            Intent intent = new Intent(context, UpgradeDialogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(UPConstants.UPGRADE_APKURL, map.get(UPConstants.UPGRADE_APKURL));
            intent.putExtra(UPConstants.UPGRADE_APPNAME, map.get(UPConstants.UPGRADE_APPNAME));
            intent.putExtra(UPConstants.UPGRADE_REMARK, map.get(UPConstants.UPGRADE_REMARK));
            context.startActivity(intent);
        }
        
    }
    
    private PackageInfo getPackageInfo(String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if(result != null){
            // 成功
            if(result.code == TaskResult.SUCCESS && result.data != null){
                Map<String, String> map = (Map<String, String>) result.data;
                checkVersionUpdate(map);
            } else {
                // 失败
                onRequestError();
            }
        } else {
            // 失败
            onRequestError();
        }
    }
}
