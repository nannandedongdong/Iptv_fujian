package com.ccdt.ottclient.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 
 * @author hezb
 */
public class SQLContentProvider extends ContentProvider {

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static
	{
		uriMatcher.addURI(SQLDataBase.AUTHORITY, "items", SQLDataBase.CODE_DIR);
		uriMatcher.addURI(SQLDataBase.AUTHORITY, "item/#", SQLDataBase.CODE_ITEM);
	}

	private SQLHelper sqlHelper;

	@Override
	public boolean onCreate() {
		sqlHelper = new SQLHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case SQLDataBase.CODE_DIR:
				return SQLDataBase.TYPE_DIR;
			case SQLDataBase.CODE_ITEM:
				return SQLDataBase.TYPE_ITEM;
			default:
				throw new IllegalArgumentException("未知Uri:" + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs,
						String sortOrder) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		Cursor c = null;
		switch (uriMatcher.match(uri)) {
			case SQLDataBase.CODE_DIR:
				c = db.query(SQLDataBase.TABLE_NAME, projection, where, 
						whereArgs, null, null, sortOrder);
				break;
			case SQLDataBase.CODE_ITEM:
				String id = uri.getPathSegments().get(1);
				c = db.query(SQLDataBase.TABLE_NAME, projection, whereWithId(where), 
						addIdToSelectionArgs(id, whereArgs), null, null, sortOrder);
				break;
				
		default:
			break;
		}

		return c;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		Uri resultUri = null;
		switch (uriMatcher.match(uri)) {
			case SQLDataBase.CODE_DIR:
				long id = db.insert(SQLDataBase.TABLE_NAME, "foo", values);
				resultUri = id == -1 ? null : ContentUris.withAppendedId(uri, id);
				break;
				
			case SQLDataBase.CODE_ITEM:
				break;
	
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return resultUri;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		int result = -1;
		switch (uriMatcher.match(uri)) {
			case SQLDataBase.CODE_DIR:
				result = db.delete(SQLDataBase.TABLE_NAME, where, whereArgs);
				break;
	
			case SQLDataBase.CODE_ITEM:
				String id = uri.getPathSegments().get(1);
				result = db.delete(SQLDataBase.TABLE_NAME, whereWithId(where),
						addIdToSelectionArgs(id, whereArgs));
				break;
	
			default:
				return 0;
		}
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		int result = -1;
		switch (uriMatcher.match(uri)) {
			case SQLDataBase.CODE_DIR:
				result = db.update(SQLDataBase.TABLE_NAME, values, where, whereArgs);
				break;
	
			case SQLDataBase.CODE_ITEM:
				String id = uri.getPathSegments().get(1);
				result = db.update(SQLDataBase.TABLE_NAME, values, whereWithId(where),
						addIdToSelectionArgs(id, whereArgs));
				break;
	
			default:
				return 0;
		}
		return result;
	}

	
	private String whereWithId(String selection) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(BaseColumns._ID);
		sb.append(" = ?");
		if (selection != null) {
			sb.append(" AND (");
			sb.append(selection);
			sb.append(')');
		}
		return sb.toString();
	}

	private String[] addIdToSelectionArgs(String id, String[] selectionArgs) {

		if (selectionArgs == null) {
			return new String[] { id };
		}

		int length = selectionArgs.length;
		String[] newSelectionArgs = new String[length + 1];
		newSelectionArgs[0] = id;
		System.arraycopy(selectionArgs, 0, newSelectionArgs, 1, length);
		return newSelectionArgs;
	}
}
