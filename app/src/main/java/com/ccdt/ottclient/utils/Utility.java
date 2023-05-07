package com.ccdt.ottclient.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.JuJiInfo;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.provider.SQLDataOperationMethod;
import com.ccdt.ottclient.ui.dialog.SeriesDialogActivity;
import com.ccdt.ottclient.ui.activity.VODPlayerActivity;
import com.ccdt.ottclient.ui.activity.VodDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package com.ccdt.stb.vision.util
 * @ClassName: Utility
 * @Description: 工具类
 * @author hezb
 * @date 2015年6月3日 下午6:23:40
 */

public class Utility {

    private Utility() {
        
    }
    
    /**
     * 获取系统的当前版本名称
     */
    public static String getCurrentVersionName(Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return "";
        }

        return packageInfo.versionName;
    }
    
    /**
     * 获取当前网络环境是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean netSataus = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null) {
            netSataus = netinfo.isAvailable();
        }
        return netSataus;
    }
    
    /**
     * 获取当前有线网络环境是否可用
     */
    public static boolean isEthernetNetworkAvailable(Context context) {
        boolean netSataus = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null
                && netinfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            netSataus = netinfo.isAvailable();
        }
        return netSataus;
    }
    
    /**
     * 获取当前WIFI网络是否可用
     */
    public static boolean isWiFiNetworkAvailable(Context context) {
        boolean netSataus = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null
                && netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
            netSataus = netinfo.isAvailable();
        }
        return netSataus;
    }
    
    /**
     * 获取当前WIFI信号等级
     */
    public static int getWiFiLevel(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        int level = 0;
        if (netinfo != null
                && netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info.getBSSID() != null) {
                // 链接信号强度
                level = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            }
        }
        return level;
    }
    
    private static Toast toast;
    /**
     * @Description: TODO全局系统toast
     * @author hezb
     * @date 2015年2月6日下午6:04:24
     */
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            View v = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            ((TextView) v.findViewById(R.id.toast_msg)).setText(content);
            toast.setView(v);
        } else {
            ((TextView) toast.getView().findViewById(R.id.toast_msg)).setText(content);
        }
        toast.show();
    }
    /**
     * @Description: TODO取消toast  
     * @author hezb
     * @date 2015年5月25日上午11:08:42
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
    
    /** 图片加载器 */
    private static ImageLoader mImageLoader = ImageLoader.getInstance();
    private static DisplayImageOptions mOptions;
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, DisplayImageOptions> mOptionsMap = new HashMap<Integer, DisplayImageOptions>();
    private static DisplayImageOptions getDisplayImageOptionsFactory(int resId) {
        if (mOptionsMap.containsKey(resId)) {
            return mOptionsMap.get(resId);
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(true)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300));

        if (resId != 0) {
            builder.showImageOnFail(resId).showImageForEmptyUri(resId)
                    .showImageOnLoading(resId);
        }
        mOptions = builder.build();
        mOptionsMap.put(resId, mOptions);
        return mOptions;
    }
    /**
     * 异步加载网络图片
     */
    public static void displayImage(String url, ImageView view,
            ImageLoadingListener listener, int defaultImgRes) {
        if (view == null) {
            return;
        }
        mOptions = getDisplayImageOptionsFactory(defaultImgRes);
        mImageLoader.displayImage(url, view, mOptions, listener);
    }

    /**
     * 异步加载网络图片
     */
    public static void displayImage(String url, ImageView view,
                                    ImageLoadingListener listener) {
        displayImage(url, view, listener, R.drawable.img_default);
    }

    /**
     * 异步加载网络图片
     */
    public static void displayImage(String url, ImageView view, int resId) {
        displayImage(url, view, null, resId);
    }

    /**
     * 异步加载网络图片
     */
    public static void displayImage(String url, ImageView view) {
        displayImage(url, view, null, R.drawable.img_default);
    }
    
    /**
     * 格式化时间
     */
    public static String generateTime(int time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * 位移动画
     */
    public static void translateAnimationmethod(View view, float xFrom, float xTo,
            float yFrom, float yTo,long duration) {

        TranslateAnimation translateAnimation = new TranslateAnimation(

        Animation.RELATIVE_TO_SELF, xFrom, Animation.RELATIVE_TO_SELF, xTo,
                Animation.RELATIVE_TO_SELF, yFrom, Animation.RELATIVE_TO_SELF,
                yTo);

        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setFillBefore(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
        translateAnimation.setDuration(duration);
        view.startAnimation(translateAnimation);
        translateAnimation.startNow();
    }
    
    /**
     * 回看 进入播放页
     * @author hezb
     * @date 2015年6月4日下午8:59:32
     */
    public static void intoPlayerActivity(Context context, String title,
            String playUrl) {
        Intent intent = new Intent(context, VODPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("playUrl", playUrl);
        bundle.putString("title", title);
        intent.putExtra("videoInfo", bundle);
        context.startActivity(intent);
    }
    /**
     * 电视剧进入播放页
     * 直接点播放 seriesPosition == -1
     * @author hezb
     * @date 2015年6月10日下午5:24:25
     */
    public static void intoPlayerActivity(Context context, String title, String posterUrl,
            String mzId, int seriesPosition, ArrayList<JuJiInfo> juJiInfos) {
        Intent intent = new Intent(context, VODPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("JujiInfos", juJiInfos);
        bundle.putString("title", title);
        bundle.putString("posterUrl", posterUrl);
        bundle.putString("mzId", mzId);
        bundle.putInt("seriesPosition", seriesPosition);
        intent.putExtra("isSeriesInfo", bundle);
        context.startActivity(intent);
    }


    /**
     * 历史 进入播放页
     * @author hezb
     * @date 2015年6月6日下午6:09:20
     */
    public static void intoPlayerActivity(Context context, SQLDataItemModel sqlDataItemModel) {
        Intent intent = new Intent(context, VODPlayerActivity.class);
        intent.putExtra("SQLDataItemModel", sqlDataItemModel);
        context.startActivity(intent);
    }

    /**
     * 拍客 点播 进入播放页
     * @author hezb
     * @date 2015年6月4日下午8:59:32
     */
    public static void intoPlayerActivity(Context context, String title,
                                          String playUrl, String posterUrl, String mzId, int type) {
        Intent intent = new Intent(context, VODPlayerActivity.class);
        SQLDataItemModel sqlDataItemModel = new SQLDataItemModel();
        sqlDataItemModel.setTitle(title);
        sqlDataItemModel.setPlayUrl(playUrl);
        sqlDataItemModel.setPosterUrl(posterUrl);
        sqlDataItemModel.setMzId(mzId);
        sqlDataItemModel.setType(type);
        if (!TextUtils.isEmpty(mzId)) {
            sqlDataItemModel.setPlayPosition(SQLDataOperationMethod
                    .searchPosition(context, type, mzId));
        }
        intent.putExtra("SQLDataItemModel", sqlDataItemModel);
        context.startActivity(intent);
    }



    /**
     * 展示选集
     * @author hezb
     * @date 2015年6月10日下午9:34:31
     */
    public static void showSeriesDialog(Context context, Bundle seriesBundle) {
        Intent intent = new Intent(context, SeriesDialogActivity.class);
        intent.putExtra("isSeriesInfo", seriesBundle);
        context.startActivity(intent);
    }
//
    /**
     * 进入点播详情页
     * @author hezb
     * @date 2015年6月8日下午3:21:09
     */
    public static void intoVODDetailActivity(Context context, String mzId) {
        VodDetailActivity.actionStart(context, mzId);
    }
    
    private static int pos = -1;
//    /**
//     * 获取随机的背景图片
//     */
//    public static int getRandomBackgroud() {
//        return bgSelector[createRandom(bgSelector.length)];
//    }
//    private static int createRandom(int size) {
//
//        if (pos == size - 1) {
//            pos = -1;
//        }
//        pos++;
//        return pos;
//    }

    /**
     * 获取MAC地址
     */
    private static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取设备当前MAC地址
     */
    public static String getMACAddress() {
        // 优先获取有线的mac
        String mac = getMACAddress("wlan0");
        if (TextUtils.isEmpty(mac)) {
            mac = getMACAddress("eth0");
        }
        return mac;
    }
    
    public static final int IMAGE_ZOOM_RATIO_4X3  = 1;
    public static final int IMAGE_ZOOM_RATIO_16X9 = 2;
    public static final int IMAGE_ZOOM_RATIO_3X4  = 3;
    public static final int IMAGE_ZOOM_RATIO_9X16 = 4;
    public static final int IMAGE_ZOOM_RATIO_5X6  = 5;
    public static final int IMAGE_ZOOM_RATIO_7X8  = 6;
    public static final int IMAGE_ZOOM_RATIO_15X7 = 7;
    public static final int IMAGE_ZOOM_RATIO_1X1  = 8;
    public static final int IMAGE_ZOOM_RATIO_2X1  = 9;
    public static int windowsWidthPixels = 1280;
    public static int windowsHeightPixels = 720;
    private static DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    /**
     * 初始化屏幕大小
     */
    public static void initializationDisplayMetrics(WindowManager manager) {
        if (manager == null) {
            return;
        }
        manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        windowsWidthPixels = mDisplayMetrics.widthPixels;
        windowsHeightPixels = mDisplayMetrics.heightPixels;
    }
    /**
     * 根据屏幕大小按比例算海报规格
     * numColumns 列数
     * horizontalSpacing 间距
     * imageZoomRatio 宽高比
     */
    public static void resizeImageViewForGridViewDependentOnScreenSize(
            View view, int numColumns, int horizontalSpacing, int imageZoomRatio) {
        if (view == null) {
            return;
        }
        android.view.ViewGroup.LayoutParams layoutParams = view
                .getLayoutParams();
        layoutParams.width = windowsWidthPixels / numColumns
                - horizontalSpacing * (numColumns - 1);
        switch (imageZoomRatio) {
            case IMAGE_ZOOM_RATIO_4X3:
                layoutParams.height = layoutParams.width * 3 / 4;
                break;
            case IMAGE_ZOOM_RATIO_16X9:
                layoutParams.height = layoutParams.width * 9 / 16;
                break;
            case IMAGE_ZOOM_RATIO_3X4:
                layoutParams.height = layoutParams.width * 4 / 3;
                break;
            case IMAGE_ZOOM_RATIO_9X16:
                layoutParams.height = layoutParams.width * 16 / 9;
                break;
            case IMAGE_ZOOM_RATIO_5X6:
                layoutParams.height = layoutParams.width * 6 / 5;
                break;
            case IMAGE_ZOOM_RATIO_7X8:
                layoutParams.height = layoutParams.width * 8 / 7;
                break;
            case IMAGE_ZOOM_RATIO_15X7:
                layoutParams.height = layoutParams.width * 7 / 15;
                break;
            case IMAGE_ZOOM_RATIO_1X1:
                layoutParams.height = layoutParams.width;
                break;
            default:
                break;
        }
        view.setLayoutParams(layoutParams);
    }
    
//    /**
//     * 获取用户ID
//     */
//    public static String getUserId() {
//        return ITVApplication.sharedPreferences
//                .getString(SharedPrefsConfig.AUTH_USER_ID, "");
//    }
}
