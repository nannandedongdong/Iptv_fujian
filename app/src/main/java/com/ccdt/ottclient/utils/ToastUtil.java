package com.ccdt.ottclient.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.ccdt.ottclient.MyApp;


public class ToastUtil {

	public static Toast toast;
	public static int NOTIFICATION_ID = 8633170;

	public static void toast(String notice) {
		if (notice != null && notice.length() > 0) {
			if (toast == null) {
				toast = Toast.makeText(MyApp.getInstance(), notice,
						Toast.LENGTH_SHORT);
			} else {
				toast.setText(notice);
			}
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}



	public static void toastLong(String notice) {
		if (notice != null && notice.length() > 0) {
			if (toast == null) {
				toast = Toast.makeText(MyApp.getInstance(), notice,
						Toast.LENGTH_SHORT);
			} else {
				toast.setText(notice);
			}
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}


	public static void toast(int rId) {
		String message = "";
		message = MyApp.getInstance().getString(rId);
		toast(message);
	}
	
	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}



}
