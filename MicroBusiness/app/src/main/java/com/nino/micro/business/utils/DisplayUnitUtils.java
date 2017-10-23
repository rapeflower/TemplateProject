package com.nino.micro.business.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.nino.micro.business.R;
import com.nino.micro.business.application.MicroApplication;
import com.nino.micro.business.utils.logUtils.LogUtils;

/***********
 * @Author rape flower
 * @Date 2017-03-16 14:37
 * @Describe 用来进行dp、sp转换成px 的工具类
 */
public class DisplayUnitUtils {

    public static final int IOS_WIDTH = 640;

    private DisplayUnitUtils() {
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue //@param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue //@param scale    （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue //@param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue //@param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, Context context) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕的scaledDensity
     *
     * @return
     */
    public static float getDisplayScaleDensity() {
        float fontScale = MicroApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return fontScale;
    }

    /**
     * 获取屏幕的densityDpi
     *
     * @return
     */
    public static float getDisplayDensityDpi() {
        float fontScale = MicroApplication.getInstance().getResources().getDisplayMetrics().densityDpi;
        return fontScale;
    }


    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getDisplayWidth(Context context) {
        int width = Math.min(context.getResources().getDisplayMetrics().widthPixels
                , context.getResources().getDisplayMetrics().heightPixels);
        // LogUtils.d("widthPixels ="+context.getResources().getDisplayMetrics().widthPixels);
        // LogUtils.d("heightPixels ="+context.getResources().getDisplayMetrics().heightPixels);
        return width;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getDisplayWidth() {
        return getDisplayWidth(MicroApplication.getInstance());// 获取屏幕分辨率宽度
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getDisplayHeight(Context context) {
        int height = //context.getResources().getDisplayMetrics().heightPixels;
                Math.max(context.getResources().getDisplayMetrics().widthPixels
                        , context.getResources().getDisplayMetrics().heightPixels);
        return height;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getDisplayHeight() {
        return getDisplayHeight(MicroApplication.getInstance());// 获取屏幕分辨率宽度
    }

    /**
     * 根据传入的值，计算出
     *
     * @param uiHeight UI设计图给出的尺寸
     */
    public static int scaleHeightByDisplayWidth(int uiHeight, Context context) {
        int width = getDisplayWidth(context);
        int result = width * uiHeight / IOS_WIDTH;
        if (result == 0) {
            return uiHeight;
        }
        return result;
    }

    /**
     * 根据传入的值，计算出
     *
     * @param uiHeight UI设计图给出的尺寸
     * @param iosStand IOS的标准宽度
     */

    public static int scaleHeightByDisplayWidth(int uiHeight, int iosStand, Context context) {
        final int iosWidthStand = iosStand;
        int width = getDisplayWidth(context);
        //LogUtils.d("width = "+width);
        if (iosWidthStand == 0) {
            return scaleHeightByDisplayWidth(uiHeight, context);
        }
        int result = width * uiHeight / iosWidthStand;
        return result;
    }

    /**
     * 获取系统默认的actionbar的高度
     */
    public static int getActionBarHeight(Context context) {
        int defHeight = context.getResources().getDimensionPixelOffset(R.dimen.actionbar_height);
        int height = defHeight;
        try {
            TypedArray actionBarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            height = actionBarSizeTypedArray.getDimensionPixelOffset(0, defHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (height <= 48) {
            height = defHeight;
        }
        return height;
    }

    /**
     * 获取宽度的比例值
     *
     * @param scale      需要计算的比例
     * @param subtractor 在计算比例之前需要减去的值
     */
    public static int getDisplayScaleWidth(float scale, int subtractor, Context context) {
        float result = (getDisplayWidth(context) - subtractor) * scale;
        return (int) result;
    }

    /**
     * 获取高度的比例值
     *
     * @param scale      需要计算的比例
     * @param subtractor 在计算比例之前需要减去的值
     */
    public static int getDisplayScaleHeight(float scale, int subtractor, Context context) {
        float result = (getDisplayHeight(context) - subtractor) * scale;
        return (int) result;
    }

    /**
     * 获取系统状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取系统状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int resourceId = MicroApplication.getInstance().getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return MicroApplication.getInstance().getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取View相对于屏幕的绝对位置
     *
     * @param view 注意这个值是要从屏幕顶端算起，也就是索包括了通知栏的高度
     * @return
     */
    public static int[] getLocationXY(View view) {
        int[] location = new int[2];
        //view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        //System.out.println("view--->x坐标:"+location [0]+"view--->y坐标:"+location [1]);
        return location;
    }


    /**
     * 获取View计算该视图在它所在的window的坐标x，y值
     *
     * @param view
     * @return
     */
    public static int[] getLocationWindowXY(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        return location;
    }

    /**
     * 判断View是不是即将进入到另外一个View的底部
     *
     * @param childView
     * @param otherView
     * @return
     */
    public static boolean isChildViewGoingToBelowOtherView(View childView, View otherView) {
        Rect currentViewRect = new Rect();
        boolean isVisible = childView.getGlobalVisibleRect(currentViewRect);

        Rect barRect = new Rect();
        otherView.getGlobalVisibleRect(barRect);

        LogUtils.e("===child.top:" + currentViewRect.top + "===barRect.bottom:" + barRect.bottom);
        if (currentViewRect.top <= barRect.bottom || !isVisible) {
            LogUtils.e("==isVisbile:" + !isVisible);
            LogUtils.e("====top<=?bottom:" + (currentViewRect.top <= barRect.bottom));
            return true;
        }

        ViewParent parent = childView.getParent();
        if (parent == null) {
            LogUtils.e("===parent" + parent);
            return false;
        }
        //已经为根View，并不是子View
        boolean isChildView = parent instanceof ViewGroup;
        if (!isChildView) {
            LogUtils.e("===已经为根View");
            return false;
        }
        //已经完全遮挡
        if (currentViewRect.bottom == ((View) parent).getMeasuredHeight()) {
            LogUtils.e("===已经完全遮挡");
            return true;
        }

        return false;
    }

}
