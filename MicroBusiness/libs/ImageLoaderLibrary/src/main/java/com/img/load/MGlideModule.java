package com.img.load;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/***********
 * @Author rape flower
 * @Date 2016-10-24 16:06
 * @Describe 从Glide的3.6.0之后，新添加了全局设置的方法。
 * 具体方法如下：先实现GlideModule接口，全局设置ViewTaget的tagId
 */
public class MGlideModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
