package com.ccdt.ottclient.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;


/**
 * 数据库操作方法
 * @author hezb
 */
public class SQLDataOperationMethod {
	
	/**
	 * 增/改
	 */
	public static void saveData(Context context, SQLDataItemModel sqlDataItemModel){
		ContentValues values = new ContentValues();
		values.put(SQLDataBase.Enum.TYPE, sqlDataItemModel.getType());
		values.put(SQLDataBase.Enum.MZID, sqlDataItemModel.getMzId());
		values.put(SQLDataBase.Enum.TITLE, sqlDataItemModel.getTitle());
		values.put(SQLDataBase.Enum.POSTERURL, sqlDataItemModel.getPosterUrl());
		values.put(SQLDataBase.Enum.PLAYURL, sqlDataItemModel.getPlayUrl());
		values.put(SQLDataBase.Enum.PLAYPOSITION, sqlDataItemModel.getPlayPosition());
		values.put(SQLDataBase.Enum.SERIESPOSITION, sqlDataItemModel.getSeriesPosition());
		values.put(SQLDataBase.Enum.TIME, System.currentTimeMillis());
		if (!TextUtils.isEmpty(sqlDataItemModel.getMzId())) {
		    deleteData(context, sqlDataItemModel.getType(), sqlDataItemModel.getMzId());
        }
		context.getContentResolver().insert(SQLDataBase.Enum.CONTENT_URI_DIR, values);
	}
	
	/**
	 * 删
	 * 某一类中的某个ID
	 */
	public static int deleteData(Context context, int type, String mzId){
		return context.getContentResolver().delete(SQLDataBase.Enum.CONTENT_URI_DIR,
				SQLDataBase.Enum.TYPE + "=?" + " and " +
				SQLDataBase.Enum.MZID + "=?",
				new String[] { ""+type, mzId } );
	}

	/**
	 * 删
	 * 某一类
	 */
	public static int deleteData(Context context, int type){
		return context.getContentResolver().delete(SQLDataBase.Enum.CONTENT_URI_DIR,
				SQLDataBase.Enum.TYPE + "=?", new String[] { ""+type} );
	}
// 删除 拍客  视频
	public static int deleteData(Context context){
		return context.getContentResolver().delete(SQLDataBase.Enum.CONTENT_URI_DIR,
				SQLDataBase.Enum.TYPE + "=?" +" or " + SQLDataBase.Enum.TYPE + "=?" , new String[] { ""+SQLDataItemModel.TYPE_MOVE,""+SQLDataItemModel.TYPE_MAKING});
	}

	/**
	 * 查
	 * 某类的某个ID，返回结果 播放位置
	 */
	public static int searchPosition(Context context, int type, String mzId){
	    int playPosition = 0;
	    Cursor mCursor = context.getContentResolver().query(
	            SQLDataBase.Enum.CONTENT_URI_DIR, SQLDataBase.enumName,
	            SQLDataBase.Enum.TYPE + "=?" + " and " +
	            SQLDataBase.Enum.MZID + "=?", 
	            new String[]{ ""+type, mzId },
	            null);
	    if (mCursor != null) {
	        while (mCursor.moveToNext()) {
	            playPosition = mCursor.getInt(
	                    mCursor.getColumnIndex(SQLDataBase.Enum.PLAYPOSITION));
	        }
	        mCursor.close();
	    }
	    return playPosition;
	}
	/**
	 * 查
	 * 点播某个ID，返回结果 电视剧剧集位置
	 */
	public static int searchSeriesPosition(Context context, String mzId){
	    int seriesPosition = 0;
	    Cursor mCursor = context.getContentResolver().query(
	            SQLDataBase.Enum.CONTENT_URI_DIR, SQLDataBase.enumName,
	            SQLDataBase.Enum.TYPE + "=?" + " and " +
	                    SQLDataBase.Enum.MZID + "=?", 
	                    new String[]{ ""+SQLDataItemModel.TYPE_MOVE, mzId },
	                    null);
	    if (mCursor != null) {
	        while (mCursor.moveToNext()) {
	            seriesPosition = mCursor.getInt(
	                    mCursor.getColumnIndex(SQLDataBase.Enum.SERIESPOSITION));
	        }
	        mCursor.close();
	    }
	    return seriesPosition;
	}
}
