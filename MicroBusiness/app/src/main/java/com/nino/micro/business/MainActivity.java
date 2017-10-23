package com.nino.micro.business;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.img.load.ImageLoaderManager;
import com.lily.http.network.client.RequestManager;
import com.lily.http.network.exception.OkHttpException;
import com.lily.http.network.listener.DisposeDataListener;
import com.lily.http.network.request.RequestParams;
import com.nino.micro.business.application.MicroApplication;
import com.nino.micro.business.permission.PermissionManager;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView ivTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivTest = (ImageView) findViewById(R.id.iv_test);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendRequest();
                PermissionManager.checkPickPicture(MainActivity.this, 20, 9, 1);
            }
        });

        ImageLoaderManager.loadImage(MainActivity.this, ivTest, "https://img01.j1.com/upload/pic/homepage/1491553107975.jpg");
    }

    /**
     * 发送Http请求获取物流信息数据
     *
     * @author rape flower
     * @date 2017/5/5
     */
    public void sendRequest() {
        RequestParams params = new RequestParams();
        params.put("type", "yuantong");
        params.put("postid", "884379345343290656");

        // http://api.jisuapi.com/weather/query
        // http://music.163.com/api/search/get/
        RequestManager.get("http://www.kuaidi100.com/query", params ,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d(TAG, "onSuccess");

                Log.w(TAG, "result = " + responseObj.toString());
            }

            @Override
            public void onFailure(OkHttpException e) {
                Log.d(TAG, "onFailure = " + e.getEMsg());
            }
        }, null);
    }
}
