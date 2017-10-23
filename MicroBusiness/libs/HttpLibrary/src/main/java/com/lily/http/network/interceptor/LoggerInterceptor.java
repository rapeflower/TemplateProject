package com.lily.http.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-10-20 14:06
 * @Describe 日志显示拦截器
 */
public class LoggerInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String log = String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers());
        Log.w("LoggerInterceptor", "log = " + log);
        return chain.proceed(request);
    }
}
