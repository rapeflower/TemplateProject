package com.img.load;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-19 18:00
 * @Describe
 *
 */
public class GlideImageLoader implements BaseImageLoader{

    private static final String TAG = GlideImageLoader.class.getSimpleName();
    private static GlideImageLoader mInstance;

    private GlideImageLoader() {
    }

    /**
     * 单例
     * @return
     */
    public static GlideImageLoader getInstance() {
        if(mInstance ==null){
            synchronized (GlideImageLoader.class){
                if(mInstance == null){
                    mInstance = new GlideImageLoader();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    @Override
    public void loadImage(Context context, LoaderParameter loaderParameter) {
        load(context, loaderParameter);
    }

    /**
     * Glide直接从网络端加载图片
     *
     * @param context
     * @param parameter
     * <p>
     *  图片加载背景会变成绿色，加载jpg图片会出现的BUG，gitHub上给出解决方案
     *  Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
     *  或者
     *  Glide.with(this).fromResource().asBitmap().encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
     *  .load(R.drawable.default).into(imageView);
     * </p>
     */
    private void load(Context context, LoaderParameter parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("Your LoaderParameter not null object");
        }

        //加载的是资源图片
        if (parameter.isAsResource()) {
            if (parameter.getResourceId() <= 0) {
                return;
            }
            Glide.with(context).load(parameter.getResourceId()).into(parameter.getImageView());
        } else {//从网络端加载图片
            DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(parameter.getUrl());
            drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(parameter.getPlaceHolder())
                    .error(parameter.getErrorHolder())
                    .crossFade();
            if (parameter.getTarget() == null) {
                if (parameter.getImageView() instanceof CircleImageView) {
                    throw new IllegalArgumentException("When use CircleImageView show image, Must Override CustomTargetProxy(T container, int width, int height) " +
                            "and Width and height must both be > 0");
                } else {
                    if (parameter.isAsGif()) {
                        if (parameter.getUrl().endsWith(".gif")) {
                            drawableTypeRequest.asGif().into(parameter.getImageView());
                        } else {
                            throw new IllegalArgumentException("Your load not a Gif Image");
                        }
                    } else {
                        drawableTypeRequest.into(parameter.getImageView());
                    }
                }
            } else {
                //new CustomTargetProxy<>(parameter.getImageView(), 100, 100)
                CustomTargetProxy target = (CustomTargetProxy) parameter.getTarget();
                drawableTypeRequest.asBitmap().into(target);
            }
        }
    }


    /**
     * Glide加载缓存中的图片（磁盘或内存）
     */
    private void loadCache(Context context, LoaderParameter parameter) {
        Glide.with(context).using(new StreamModelLoader<String>() {
            @Override
            public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
                return new DataFetcher<InputStream>() {
                    @Override
                    public InputStream loadData(Priority priority) throws Exception {
                        throw new IOException();
                    }

                    @Override
                    public void cleanup() {

                    }

                    @Override
                    public String getId() {
                        return model;
                    }

                    @Override
                    public void cancel() {

                    }
                };
            }
        }).load(parameter.getUrl())
                .placeholder(parameter.getPlaceHolder())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(parameter.getImageView());
    }

    /**
     * 释放资源
     * @param context
     */
    @Override
    public void clear(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 取消请求或正在执行的操作
     */
    @Override
    public void cancel(Object object) {
      if (object instanceof View) {
          Glide.clear((View) object);
      } else if (object instanceof Target<?>) {
          Glide.clear((Target<?>) object);
      } else if (object instanceof FutureTarget<?>) {
          Glide.clear((FutureTarget<?>) object);
      } else {
          android.util.Log.w(TAG, "You pass a effective object by Glide support");
      }
    }
}
