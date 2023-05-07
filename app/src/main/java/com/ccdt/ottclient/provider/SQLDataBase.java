package com.ccdt.ottclient.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 此类用于数据库存储　定义
 * 
 * @author hezb
 */
public final class SQLDataBase {
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_NAME = "CCDTProvider.db";
	
	public static final String AUTHORITY = "com.ccdt.ottclient.provider.public";
	
	public static final int CODE_DIR = 1;
	public static final int CODE_ITEM = 2;
	
	public static final String TYPE_DIR = "vnd.android.cursor.dir/ccdt-data";
	public static final String TYPE_ITEM = "vnd.android.cursor.item/ccdt-data";
	
	public static final String TABLE_NAME = "ccdtDataBase";
	
	public static final String CREATE_TABLE = "create table ";
	public static final String AUTOINCREMENT = " integer primary key autoincrement";
	
	public static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + "(" +
			Enum._ID + AUTOINCREMENT + "," + 
			Enum.TYPE + " integer," + 
			Enum.MZID + " text," + 
			Enum.TITLE + " text," + 
			Enum.POSTERURL + " text," + 
			Enum.PLAYURL + " text," +
			Enum.PLAYPOSITION + " long," +
			Enum.SERIESPOSITION + " integer," +
        	Enum.TIME + " long)";
	
	
	
	public static final class Enum implements BaseColumns {
		public static final String TYPE = "type";//类型 拍客，视频
		public static final String MZID = "mzId";
		public static final String TITLE = "title";
		public static final String POSTERURL = "posterUrl";
		public static final String PLAYURL = "playUrl";
		public static final String PLAYPOSITION = "playPosition";//播放位置
		public static final String SERIESPOSITION = "seriesPosition";//剧集位置
		public static final String TIME = "time";//存储时间
		
		public static final Uri CONTENT_URI_DIR = Uri.parse("content://"+ AUTHORITY  + "/items");//查找所有
		public static final Uri CONTENT_URI_ITEM = Uri.parse("content://"+ AUTHORITY  + "/item/２");//查找其一 根据_id (没用)
	}
	
	
	public static final String[] enumName = new String[]{
		Enum._ID,
		Enum.TYPE,
		Enum.MZID,
		Enum.TITLE,
		Enum.POSTERURL,
		Enum.PLAYURL,
		Enum.PLAYPOSITION,
		Enum.SERIESPOSITION,
		Enum.TIME
	};
	
	
	public static String addSortOrder(String item, boolean isAscendant){
		String stortOrder = isAscendant ? " ASC" : " DESC";
		return item + stortOrder;
	}
}
