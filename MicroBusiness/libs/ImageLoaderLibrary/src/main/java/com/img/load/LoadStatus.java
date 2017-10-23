package com.img.load;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


/***********
 *
 * @Author rape flower
 * @Date 2016-10-10 15:08
 * @Describe 请求加载网络图片的状态
 *
 */
public interface LoadStatus {
    /*开始加载图*/
    void onJ1LoadStarted(Drawable placeholder);
    /*加载图完成*/
    void onJ1LoadCompleted(Bitmap bitmap);
    /*加载图失败*/
    void onJ1LoadFailed(Drawable errorDrawable);
}
