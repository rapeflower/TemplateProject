package com.img.load;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Util;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-20 15:22
 * @Describe BaseTarget获取bitmap根据bitmap设定图片显示尺寸：
 * (这里适配屏幕宽度，高度按比例拉伸)
 *
 */
public class CustomTargetProxy<T extends View> extends BaseProxy {

    private final String TAG = CustomTargetProxy.class.getSimpleName();
    private T view;
    private ViewGroup parent;
    private final int width;
    private final int height;
    private int mScreenWidth;

    public CustomTargetProxy(T container, int screenWidth) {
        this.view = container;
        this.width = SIZE_ORIGINAL;
        this.height = SIZE_ORIGINAL;
        this.mScreenWidth = screenWidth;
    }

    public CustomTargetProxy(Context context, T container, int width) {
        this(container, width, SIZE_ORIGINAL);
    }

    public CustomTargetProxy(T container, int width, int height) {
        this.view = container;
        this.width = width;
        this.height = height;
    }

    /**
     * 设置图片尺寸回调构造函数
     *
     * @param container 显示图片的View
     * @param parent    显示图片的View的父View
     * @param width     显示宽度
     */
    public CustomTargetProxy(T container, ViewGroup parent, int width) {
        this.view = container;
        this.parent = parent;
        this.width = width;
        this.height = SIZE_ORIGINAL;
    }

    @Override
    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        onJ1LoadCompleted(bitmap);
        if (view == null) {
            return;
        }
        if (!(view instanceof ImageView)) {
            return;
        }

        ImageView img = (ImageView) view;
        ViewGroup.LayoutParams lp = img.getLayoutParams();
        if (lp == null) {
            return;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (this.width == SIZE_ORIGINAL && this.height == SIZE_ORIGINAL) {
            lp.width = mScreenWidth;
            float tempHeight = height * ((float) lp.width / width);
            lp.height = (int) tempHeight;
        } else if (this.width != SIZE_ORIGINAL && this.height == SIZE_ORIGINAL) {
            lp.width = this.width;
            float tempHeight = height * ((float) lp.width / width);
            lp.height = (int) tempHeight;

            //Set this view's parent LayoutParams
            updateParentLayoutParams((int) tempHeight, false);

        } else if (this.width == SIZE_ORIGINAL && this.height != SIZE_ORIGINAL) {
            lp.height = this.height;
            float tempWidth = width * ((float) lp.height / height);
            lp.width = (int) tempWidth;

            //Set this view's parent LayoutParams
            updateParentLayoutParams((int) tempWidth, true);
        } else {
            lp.width = this.width;
            lp.height = this.height;
        }
        img.setLayoutParams(lp);
        img.setImageBitmap(bitmap);

    }

    /***
     * 更新Parent的布局参数
     * @param change
     * @param isWidth true修改的是宽度，false修改的是高度
     */
    private void updateParentLayoutParams(int change, boolean isWidth) {
        if (parent == null)
            return;

        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (isWidth) {
            layoutParams.width = change;
            return;
        }
        layoutParams.height = change;
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        if (!Util.isValidDimensions(width, height)) {
            Log.w(TAG, "Width and height must both be > 0 or Target#SIZE_ORIGINAL, "
                    + "but given width: " + width + " and height: " + height
                    + ", either provide dimensions in the constructor or call override()");
        }
        cb.onSizeReady(width, height);
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        super.onLoadStarted(placeholder);
        onJ1LoadStarted(placeholder);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        onJ1LoadFailed(errorDrawable);
    }

    @Override
    public void onJ1LoadStarted(Drawable placeholder) {
        // Do nothing
    }

    @Override
    public void onJ1LoadCompleted(Bitmap bitmap) {
        // Do nothing
    }

    @Override
    public void onJ1LoadFailed(Drawable errorDrawable) {
        // Do nothing
    }
}
