package com.nino.micro.business.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lily.http.network.client.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***********
 * @Author rape flower
 * @Date 2017-10-20 09:19
 * @Describe 自定义Application
 */
public class MicroApplication extends Application{

    private static MicroApplication application;
    public static Context appContext;
    public List<Activity> activityManager; // 管理Activity栈
    private Context currentActivity = null;//当前activity
    public String baiDuPushChannelId = "";//存储百度推：注册绑定后接收服务端返回的channelID

    public static synchronized MicroApplication getInstance() {
        if (application == null || appContext == null) {
            application = new MicroApplication();
            appContext = application.getApplicationContext();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appContext = getApplicationContext();
        //管理Activity栈
        activityManager = new ArrayList<Activity>();
        //初始化OkHttp配置
        initOkHttp();
    }

    /**
     * 初始化OkHttp
     */
    private void initOkHttp() {
        //Add by rape flower 2017/10/20
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("client", "android");
        headers.put("version", "1.0.0");
        RequestManager.init(getApplicationContext(), headers);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 退出应用程序
     */
    public void exit() {
        try {
            setCurrentActivity(null);
            while (activityManager != null && activityManager.size() > 0) {
                Activity activity = activityManager.get(activityManager.size() - 1);
                if (activity != null) {
                    activity.finish();
                }
                if (activityManager.contains(activity)) {
                    activityManager.remove(activity);
                }
            }

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Context context) {
        currentActivity = context;
    }

    public void addActivity(Activity activity) {
        if (activityManager != null && activity != null) {
            activityManager.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityManager != null && activityManager.size() > 0 && activityManager.contains(activity)) {
            activityManager.remove(activity);
        }
    }
}
