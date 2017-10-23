package com.nino.micro.business.view.window;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.nino.micro.business.application.MicroApplication;

/***********
 * @Author rape flower
 * @Date 2017-03-20 10:55
 * @Describe Toast
 */
public class MToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MToast(Context context) {
        super(context);
    }

    /**
     * 默认弹出框
     *
     * @param content
     */
    public static void makeText(String content) {
        makeText(MicroApplication.getInstance().getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }
}
