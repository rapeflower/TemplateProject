package com.photo.selector.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;

/**
 * 通用工具类
 */
public class CommonUtil {

	/**
	 * 判断字符串是否为空
	 *
	 * @param text
	 * @return true null false !null
	 */
	public static boolean isNull(CharSequence text) {
		if (text == null || "".equals(text.toString().trim()) || "null".equals(text))
			return true;
		return false;
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @param activity
	 * @return
	 */
	public static int getWidthPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 *
	 * @param activity
	 * @return
	 */
	public static int getHeightPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 通过Uri获取图片路径
	 *
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String query(Context context, Uri uri) {
		String path = "";
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
			cursor.moveToNext();
			path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return path;
	}

}
