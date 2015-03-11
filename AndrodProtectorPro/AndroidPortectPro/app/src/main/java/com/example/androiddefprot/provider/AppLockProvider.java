package com.example.androiddefprot.provider;

import com.example.androiddefprot.db.dao.AppLockDao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 *
 * @Title: AppLockProvider.java
 * @Package com.example.androiddefprot.provider
 * @Description: 程序锁Provider，
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppLockProvider extends ContentProvider {

	
	private static final int INSERT = 10;
	private static final int DELETE = 11;
	private AppLockDao dao = null;
	private static Uri changeUri = Uri.parse("content://com.text.androiddefprot.provider"); //每次这个路径出现改变就通知内容提供者
	private static UriMatcher  matcher = new UriMatcher(UriMatcher.NO_MATCH);
    //匹配规则
	static{
		matcher.addURI("com.text.androiddefprot.provider", "insert", INSERT);
		matcher.addURI("com.text.androiddefprot.provider", "delete", DELETE);
	}
	
	@Override
	public boolean onCreate() {
		this.dao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int result = this.matcher.match(uri);
		if(INSERT == result){
			String packagename = (String) values.get("packagename");
			dao.add(packagename);
			getContext().getContentResolver().notifyChange(changeUri, null);//���һ�����ݹ۲���
		}else{
			throw new IllegalArgumentException("uri��ַ����ȷ");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = this.matcher.match(uri);
		if(DELETE == result){
			String packagename = selectionArgs[0];
			dao.delete(packagename);
			getContext().getContentResolver().notifyChange(changeUri, null);//���һ�����ݹ۲���
		}else{
			throw new IllegalArgumentException("uri��ַ����ȷ");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
