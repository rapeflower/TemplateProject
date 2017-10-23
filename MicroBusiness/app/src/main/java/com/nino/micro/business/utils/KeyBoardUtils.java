package com.nino.micro.business.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.nino.micro.business.application.MicroApplication;

/**
 * 打开或关闭软键盘
 */
public class KeyBoardUtils {


    /**
     * 打卡软键盘
     *
     * @param view view
     */
    public static void openKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) MicroApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view.getVisibility() == View.VISIBLE) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param view view
     */
    public static void closeKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) MicroApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && view.getVisibility() == View.VISIBLE) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /***
     * 默认进入界面弹出软键盘,并且调整界面UI
     *
     * @param activity
     */
    public static void openKeyboardAdjustWhenEnterActivity(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * 默认进入界面弹出软键盘,不调整界面UI
     *
     * @param activity
     */
    public static void openKeyboardWhenEnterActivity(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 默认进入界面弹出软键盘为隐藏状态
     *
     * @param activity
     */
    public static void closeKeyboardWhenEnterActivity(Activity activity) {
        // 默认进入界面弹出软键盘为隐藏状态
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 默认进入界面弹出软键盘为隐藏状态
     *
     * @param activity
     */
    public static void closeKeyboardAdjustWhenEnterActivity(Activity activity) {
        // 默认进入界面弹出软键盘为隐藏状态
        try {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
