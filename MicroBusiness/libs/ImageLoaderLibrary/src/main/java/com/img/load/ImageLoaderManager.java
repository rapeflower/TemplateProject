package com.img.load;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;


/***********
 * @Author rape flower
 * @Date 2016-09-20 09:43
 * @Describe 对外提供的加载图片的类
 */
public class ImageLoaderManager {

    //default instance class GlideImageLoader
    private static BaseImageLoader imageLoader = GlideImageLoader.getInstance();

    public ImageLoaderManager() {
    }

    /**
     * 构建请求参数
     *
     * @param imageView   显示图片的控件
     * @param url         图片地址
     * @param placeholder 加载中的显示图片
     * @param errorHolder 加载失败显示的图片
     * @param asGif       是否是要显示的Gif图片
     * @param target      根据Bitmap重新设置图片大小的接口
     * @return
     */
    private static LoaderParameter createParameter(ImageView imageView, String url, Drawable placeholder, Drawable errorHolder,
                                                   boolean asGif, boolean asResource, int resourceId, BaseProxy target) {
        LoaderParameter.Builder builder = LoaderParameter.newBuilder();
        builder.setUrl(url);
        builder.setPlaceHolder(placeholder);
        builder.setErrorHolder(errorHolder);
        builder.setImageView(imageView);
        builder.setAsGif(asGif);
        builder.setAsResource(asResource);
        builder.setResourceId(resourceId);
        builder.setTarget(target);
        return builder.build();
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param parameter 加载网络图片的参数对象
     */
    private static void loadImage(Context context, LoaderParameter parameter) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.loadImage(context, parameter);
    }

    /**
     * 加载资源图片
     */
    public static void loadImage(Context context, ImageView imageView, Integer resourceId) {
        LoaderParameter parameter = createParameter(imageView
                , ""
                , null
                , null
                , false
                , true
                , resourceId
                , (BaseProxy) null);
        loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     */
    public static void loadImage(Context context, ImageView imageView, String url) {
        loadImage(context, imageView, url, (BaseProxy) null);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     * @param target    重新设置图片大小的接口
     */
    public static void loadImage(Context context, ImageView imageView, String url, BaseProxy target) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , null
                , null
                , false
                , false
                , 0
                , target);
        loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView      显示图片的控件
     * @param url            图片的地址
     * @param placeholderRes 加载中的图片的资源ID
     * @param errorHolderRes 加载失败的图片的资源ID
     */
    public static void loadImage(Context context, ImageView imageView, String url, int placeholderRes, int errorHolderRes) {
        loadImage(context, imageView, url, placeholderRes, errorHolderRes, (BaseProxy) null);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView      显示图片的控件
     * @param url            图片的地址
     * @param placeholderRes 加载中的图片的资源ID
     * @param errorHolderRes 加载失败的图片的资源ID
     * @param target         重新设置图片大小的接口
     */
    public static void loadImage(Context context, ImageView imageView, String url, int placeholderRes, int errorHolderRes, BaseProxy target) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , null
                , null
                , false
                , false
                , 0
                , target);
        loadImage(context, parameter);
    }

    /**
     * 加载Gif网络图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     */
    public static void loadGifImage(Context context, ImageView imageView, String url) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , null
                , null
                , true
                , false
                , 0
                , (BaseProxy) null);
        loadImage(context, parameter);
    }

    /**
     * 加载圆角的图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     */
    public static void loadRoundImageByGlide(Context context, ImageView imageView, Integer resourceId, String url, int radius, RoundedCornersTransformation.CornerType cornerType) {
        RequestManager requestManager = Glide.with(context);
        DrawableTypeRequest request = null;
        if (resourceId > 0) {
            request = requestManager.load(resourceId);
        } else {
            request = requestManager.load(url);
        }

        if (request == null) return;
        request.bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, radius, 0, cornerType))
                .into(imageView);
    }

    /**
     * 释放资源
     *
     * @param context
     */
    public static void clear(Context context) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.clear(context);
    }

    /**
     * 清除掉所有的图片加载请求
     *
     * @param object 传入的值可以是这些类型或是其子类：
     *               View、Target<?>，FutureTarget<?>
     */
    public static void cancel(Object object) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.cancel(object);
    }
}
