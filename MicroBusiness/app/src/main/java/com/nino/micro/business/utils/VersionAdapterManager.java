package com.nino.micro.business.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.nino.micro.business.provider.MicroFileProvider;
import com.nino.micro.business.provider.ProviderHelper;

import java.io.File;
import java.io.InputStream;

/***********
 * @Author rape flower
 * @Date 2017-10-20 17:03
 * @Describe Android系统版本适配
 */
public class VersionAdapterManager {

    public static int getColor(Context context, int color) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return context.getResources().getColor(color);
        } else {
            return context.getResources().getColor(color, null);
        }
    }

    public static Drawable getDrawable(Context context, int resId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(resId, null);
        } else {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                InputStream is = context.getResources().openRawResource(resId);
                drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is, null, opt));
            } catch (OutOfMemoryError e) {
                drawable = null;
            }
        }
        return drawable;
    }

    public static void setTextAppearance(TextView textView, Context context, @StyleRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(resId);
            return;
        }
        textView.setTextAppearance(context, resId);
    }

    public static void setBackground(View view, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        }
        else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Uri getUri(Context context, String filePath) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(new File(filePath));
        } else {
            /**
             * Android 7.0调用系统相机拍照不再允许使用uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            uri = MicroFileProvider.getUriForFile(context,
                    ProviderHelper.getFileProviderName(context), new File(filePath));
        }

        return uri;
    }
}
