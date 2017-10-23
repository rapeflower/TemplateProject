package com.img.load;

import android.content.Context;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-19 15:38
 * @Describe 加载图片的公共接口
 *
 */
public interface BaseImageLoader {
    /*加载图片*/
    void loadImage(Context context, LoaderParameter loaderParameter);
    /*清除、释放资源*/
    void clear(Context context);
    /*取消请求*/
    void cancel(Object object);
}
