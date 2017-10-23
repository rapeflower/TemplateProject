package com.lily.http.network.client;

import android.content.Context;

import com.lily.http.network.listener.DisposeDataHandle;
import com.lily.http.network.listener.DisposeDataListener;
import com.lily.http.network.request.BuildRequest;
import com.lily.http.network.request.RequestParams;
import com.lily.http.network.response.JsonCallback;

import java.util.Map;

/***********
 * @Author rape flower
 * @Date 2017-03-15 17:00
 * @Describe 处理网络请求的业务
 */
public class RequestManager {

    private static boolean isInit = false;//表示是否执行过初始化方法（init），true: 执行过，false: 没执行过

    /**
     * 初始化
     *
     * @param context
     * @param headers
     */
    public static void init(Context context, Map<String, String> headers) {
        if (context == null) {
            throw new NullPointerException("The context not null");
        }
        XOkHttpClient.initConfig(context, headers);
        isInit = true;
    }

    /**
     * Get请求
     *
     * @param url
     * @param params
     * @param dataListener
     * @param clazz
     */
    public static void get(String url, RequestParams params, DisposeDataListener dataListener, Class<?> clazz) {
        if (!isInit) {
            throw new RuntimeException("Please call method: init() in your application start");
        }
        XOkHttpClient.sendRequest(BuildRequest.createGetRequest(url, params),
                new JsonCallback(new DisposeDataHandle(dataListener, clazz)));
    }

    /**
     * Post请求
     *
     * @param url
     * @param params
     * @param dataListener
     */
    public static void post(String url, RequestParams params, DisposeDataListener dataListener, Class<?> clazz) {
        if (!isInit) {
            throw new RuntimeException("Please call method: init() in your application start");
        }
        XOkHttpClient.sendRequest(BuildRequest.createPostRequest(url, params),
                new JsonCallback(new DisposeDataHandle(dataListener, clazz)));
    }

}
