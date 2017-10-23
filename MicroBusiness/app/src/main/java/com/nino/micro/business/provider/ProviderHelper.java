package com.nino.micro.business.provider;

import android.content.Context;

/***********
 * @Author rape flower
 * @Date 2017-09-22 11:27
 * @Describe FileProvider相关的工具类
 */
public class ProviderHelper {

    public static String getFileProviderName(Context context) {
        if (context == null) return "";
        return context.getPackageName() + ".provider";
    }
}
