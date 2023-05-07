package com.ccdt.ottclient.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {
	private String TAG = "hzb";
	
	public SQLHelper(Context context) {
		super(context, SQLDataBase.DATABASE_NAME, null, SQLDataBase.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "CCDTSQLHelper onCreate------------------");
		db.execSQL(SQLDataBase.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "CCDTSQLHelper onUpgrade------------------");
		if (oldVersion != newVersion) {
			db.execSQL("drop table if exists "+SQLDataBase.TABLE_NAME);
			onCreate(db);
		}
	}

}
