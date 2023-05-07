package com.ccdt.ottclient;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.ccdt.ottclient.api.Api;
import com.ccdt.ottclient.api.OTTApi;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.ExitMsg;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.SPUtils;
import com.ccdt.ottclient.utils.StreamUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import de.greenrobot.event.EventBus;

/**
 * Created by hanyue on 2015/10/22.
 */
public class MyApp extends Application {

    private static MyApp instance;
    private static Context context;
    public Api api;
    public OTTApi ottApi;
    public static int width;
    public static int height;
    public static boolean isScale;

    /**
     * 图片框架配置项
     */
//    public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.icon).showImageForEmptyUri(R.drawable.logo)
//            .showImageOnFail(R.drawable.logo).cacheInMemory(true).considerExifParams(true)
//                    // 缓存本地磁盘
//            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(0)).build();
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
//    .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//    .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
//    .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//    .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
//    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
//    .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//    //                                    .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//    //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//    //设置图片加入缓存前，对bitmap进行设置
//    //.preProcessor(BitmapProcessor preProcessor)
//    .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//    .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
//            .build();//构建完成
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader(this);
        context = getApplicationContext();
        api = new Api(context);
        ottApi = new OTTApi();
        // 加载配置文件
        initConfig();
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        if (width > 1280){
            isScale = true;
        }else {
            isScale = false;
        }
    }


    /**
     * 加载配置文件
     * config.properties
     */
    private void initConfig() {
        AssetManager assetManager = context.getAssets();
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = assetManager.open("config.properties");
            props.load(is);
            if (!SPUtils.contains(context, "AUTH_IP")) {
                SPUtils.put(context, "AUTH_IP", props.getProperty("AUTH_IP"));
            }
            if (!SPUtils.contains(context, "SITE_ID")) {
                SPUtils.put(context, "SITE_ID", props.getProperty("SITE_ID"));
            }
            if(!SPUtils.contains(context, "COMM_SERVER_URL")){
                SPUtils.put(context, "COMM_SERVER_URL", props.getProperty("COMM_SERVER_URL"));
            }
            if(!SPUtils.contains(context, "COMM_SERVER_PORTAL")){
                SPUtils.put(context, "COMM_SERVER_PORTAL", Integer.valueOf(props.getProperty("COMM_SERVER_PORTAL")));
            }
            if(!SPUtils.contains(context, "COMM_SERVER_SEARCH_URL")){
                SPUtils.put(context, "COMM_SERVER_SEARCH_URL", props.getProperty("COMM_SERVER_SEARCH_URL"));
            }

            if(!SPUtils.contains(context, "COMM_SERVER_SEARCH_PORTAL")){
                SPUtils.put(context, "COMM_SERVER_SEARCH_PORTAL", Integer.valueOf(props.getProperty("COMM_SERVER_SEARCH_PORTAL")));
            }

            if(!SPUtils.contains(context, "APP_SHOP_URL")){
                SPUtils.put(context, "APP_SHOP_URL", props.getProperty("APP_SHOP_URL"));
            }

            props = null;
        } catch (IOException e) {
            LogUtil.e("加载配置文件异常,请检查 config.properties");
            e.printStackTrace();
        } finally {
            if (is != null) {
                StreamUtil.close(is);
            }
        }
    }

    private void exit() {
        EventBus.getDefault().post(new ExitMsg());
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static Api getApi() {
        return instance.api;
    }

    public static OTTApi getOTTApi() {
        return instance.ottApi;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static Context getAppContext() {
        return context;
    }

    private List<LmInfoObj> oneLevelLmList;

    public static final String LM_ONELEVEL_TYPE_ZB = "zb";
    public static final String LM_ONELEVEL_TYPE_VOD = "vod";
    public static final String LM_ONELEVEL_TYPE_NEWS = "news";
    public static final String LM_ONELEVEL_TYPE_PK = "pk";
    public static final String LM_ONELEVEL_TYPE_RM = "RM";

    public void setOneLevelLmList(List<LmInfoObj> dataSet) {
        this.oneLevelLmList = dataSet;
    }

    public List<LmInfoObj> getOneLevelLmList() {
        return oneLevelLmList;
    }

    public LmInfoObj getOneLevelLmInfoByType(String type) {
        LmInfoObj ret = null;
        if (!TextUtils.isEmpty(type) && oneLevelLmList != null) {
            for (LmInfoObj obj : oneLevelLmList) {
                if (obj != null) {
                    if (type.equals(obj.getType())) {
                        ret = obj;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public LmInfoObj getOneLevelLmInfoByLmId(String lmId) {
        LmInfoObj ret = null;
        if (!TextUtils.isEmpty(lmId) && oneLevelLmList != null) {
            for (LmInfoObj obj : oneLevelLmList) {
                if (obj != null) {
                    if (lmId.equals(obj.getLmId())) {
                        ret = obj;
                        break;
                    }
                }
            }
        }
        return ret;
    }


}
