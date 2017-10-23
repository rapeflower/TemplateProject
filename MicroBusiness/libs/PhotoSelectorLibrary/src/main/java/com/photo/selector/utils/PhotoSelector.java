package com.photo.selector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 通用工具类
 */
public class PhotoSelector {

	/**
	 * 启动activity
	 *
	 * @param context
	 * @param activity
	 */
	public static void launchActivity(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	/**
	 * 启动activity(带参数：Bundle)
	 *
	 * @param context
	 * @param activity
	 * @param bundle
	 */
	public static void launchActivity(Context context, Class<?> activity, Bundle bundle) {
		Intent intent = new Intent(context, activity);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	/**
     * 开启activity(带参数: key-value)
	 *
	 * @param context
     * @param activity
     * @param key
     * @param value
	 */
	public static void launchActivity(Context context, Class<?> activity, String key, int value) {
		Bundle bundle = new Bundle();
		bundle.putInt(key, value);
		launchActivity(context, activity, bundle);
	}

	/**
	 * 开启activity(带参数: key-value)
	 *
	 * @param context
	 * @param activity
	 * @param key
	 * @param value
	 */
	public static void launchActivity(Context context, Class<?> activity, String key, String value) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		launchActivity(context, activity, bundle);
	}

	/**
	 * 开启activity(需要返回结果的)
	 *
	 * @param context
	 * @param activity
	 */
	public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}

	/**
     * 开启activity(需要返回结果的)
	 *
	 * @param activity
     * @param intent
     * @param requestCode
	 */
	public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}
}
