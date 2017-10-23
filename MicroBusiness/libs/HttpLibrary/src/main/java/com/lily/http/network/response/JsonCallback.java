package com.lily.http.network.response;

import android.os.Handler;
import android.os.Looper;

import com.lily.http.network.exception.OkHttpException;
import com.lily.http.network.listener.DisposeDataHandle;
import com.lily.http.network.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:38
 * @Describe 处理请求响应的Json数据
 */
public class JsonCallback implements Callback{

    protected final String RESULT_CODE = "error_code"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "error_msg";
    protected final String EMPTY_MSG = "";

    protected final int NETWORK_ERROR = -1; //网络错误
    protected final int JSON_ERROR = -2; //JSON数据相关的错误
    protected final int OTHER_ERROR = -3; //未知的错误

    private Handler mHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public JsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.listener;
        this.mClass = handle.cls;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        //此时还是在非UI线程，需要通过Handler转发
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
        }

        try {
            String result = responseObj.toString();
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                //将json转换成实体类对象 TODO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
