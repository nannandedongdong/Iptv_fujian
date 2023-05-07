package com.ccdt.ottclient.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.ccdt.ottclient.MyApp;


/**
 * 跟App相关的辅助类
 * 
 * 
 * 
 */
public class AppUtil {

	private AppUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName() {
		try {
			PackageManager packageManager = MyApp.getAppContext().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(MyApp.getAppContext().getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return MyApp.getAppContext().getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName() {
		try {
			PackageManager packageManager = MyApp.getAppContext().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(MyApp.getAppContext().getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本号]
	 * 
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode() {
		try {
			PackageManager packageManager = MyApp.getAppContext().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(MyApp.getAppContext().getPackageName(), 0);
			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
