package com.handmark.pulltorefresh.library.animation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/***********
 *
 * @Author rape flower
 * @Date 2016-08-15 14:51
 * @Describe 用于ImageView播放动画的类
 *
 *
 * <p>Here is the code to load and play this animation.</p>
 * <P> Class Name: J1AnimationDrawable </P>
 * <pre>
 * //Init class
 * J1AnimationDrawable frameAnimation = new J1AnimationDrawable();
 * // Load the ImageView that will host the animation and
 * // set drawable id resource.
 * ImageView iv = (ImageView)findViewById(R.id.iv_animation);
 * img.setBackgroundResource(R.drawable.footer_loading_list);
 *
 * <P> One way </P>
 * //Image Resource ID List
 * List<Integer> ids = new ArrayList<Integer>();
 * ids.add(R.drawable.footer_loading_710000);ids.add(R.drawable.footer_loading_710001);
 * ids.add(R.drawable.footer_loading_710002);ids.add(R.drawable.footer_loading_710003);
 * ids.add(R.drawable.footer_loading_710004);ids.add(R.drawable.footer_loading_710005);
 * ids.add(R.drawable.footer_loading_710006);ids.add(R.drawable.footer_loading_710007);
 * ids.add(R.drawable.footer_loading_710008);ids.add(R.drawable.footer_loading_710009);
 * ids.add(R.drawable.footer_loading_710010);ids.add(R.drawable.footer_loading_710011);
 * ids.add(R.drawable.footer_loading_710012);ids.add(R.drawable.footer_loading_710013);
 * ids.add(R.drawable.footer_loading_710014);ids.add(R.drawable.footer_loading_710015);
 * ids.add(R.drawable.footer_loading_710016);
 *
 * //Set play animation resource
 * frameAnimation.setAnimation(iv, ids);
 *
 * // Start the animation (looped playback by default).
 * frameAnimation.start(true, 80);
 *
 * <P> Way two </P>
 * <p>
 * An way defined in XML consists of a single <code>&lt;animation-list></code> element,
 * and a series of nested <code>&lt;item></code> tags. Each item defines a frame of the animation.
 * See the example below.
 * </p>
 * <p>header_loading_list.xml file in res/drawable/ folder:</p>
 * <pre>&lt;!-- Animation frames are pull_loading_10001.png -- pull_loading_10075.png files inside the
 * res/drawable/ folder --&gt;
 * //Defined image resource xml
 * &lt;animation-list android:id=&quot;@+id/selected&quot; android:oneshot=&quot;false&quot;&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10001&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10002&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10003&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10004&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10005&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/pull_loading_10006&quot; android:duration=&quot;50&quot; /&gt;
 *    ......
 * &lt;/animation-list&gt;</pre>
 *
 * //Set play animation resource
 * frameAnimation.setAnimation(iv, ids);
 *
 * // Start the animation (looped playback by default).
 * frameAnimation.setAnimation(context, R.drawable.header_loading_list, mHeaderImage);
 * </pre>
 */
public class J1AnimationDrawable {

    private static final int MSG_START = 0xf1;
    private static final int MSG_STOP = 0xf2;
    private static final int STATE_STOP = 0xf3;
    private static final int STATE_RUNNING = 0xf4;

    //运行状态
    private int mState = STATE_RUNNING;
    //显示图片的View
    private ImageView mImageView = null;
    //图片资源的ID列表
    private List<Integer> mResourceIdList = null;
    //定时任务器
    private Timer mTimer = null;
    //定时任务
    private AnimTimerTask mTimeTask = null;
    //记录播放位置
    private int mFrameIndex = 0;
    //播放形式
    private boolean isLooping = false;

    public J1AnimationDrawable() {
        mTimer = new Timer();
    }

    /**
     * 设置动画播放资源
     */
    public void setAnimation(ImageView imageview, List<Integer> resourceIdList){
        mImageView = imageview;
        mResourceIdList = new ArrayList<Integer>();
        mResourceIdList.clear();
        mResourceIdList.addAll(resourceIdList);
    }

    /**
     * 设置动画播放资源
     */
    public void setAnimation(Context context, int resourceId, ImageView imageview){
        this.mImageView = imageview;
        mResourceIdList = new ArrayList<Integer>();
        mResourceIdList.clear();

        loadFromXml(context, resourceId, new OnParseListener() {
            @Override
            public void onParse(List<Integer> res) {
                mResourceIdList.addAll(res);
            }
        });
    }

    /**
     * 解析xml
     *
     * @param context
     * @param resourceId 资源id
     */
    private void loadFromXml(final Context context, final int resourceId,
                             final OnParseListener onParseListener) {
        if (context == null) {
            return;
        }

        final List<Integer> res = new ArrayList<Integer>();
        XmlResourceParser parser = context.getResources().getXml(resourceId);

        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("item")) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals("drawable")) {
                                int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                                res.add(resId);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                } else if (eventType == XmlPullParser.TEXT) {
                }

                eventType = parser.next();
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            // TODO: handle exception
            e2.printStackTrace();
        } finally {
            parser.close();
        }

        if (onParseListener != null) {
            onParseListener.onParse(res);
        }
    }

    /**
     * 开始播放动画
     * @param loop 是否循环播放
     * @param duration 动画播放时间间隔
     */
    public void start(boolean loop, int duration){
        stop();
        if (mResourceIdList == null || mResourceIdList.size() == 0) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
        }
        isLooping = loop;
        mFrameIndex = 0;
        mState = STATE_RUNNING;
        mTimeTask = new AnimTimerTask( );
        mTimer.schedule(mTimeTask, 0, duration);
    }

    /**
     * 停止动画播放
     */
    public void stop(){
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimeTask != null) {
            mFrameIndex = 0;
            mState = STATE_STOP;
            mTimeTask.cancel();
            mTimeTask = null;
        }
        //移除Handler消息
        if (AnimHandler != null) {
            AnimHandler.removeMessages(MSG_START);
            AnimHandler.removeMessages(MSG_STOP);
            AnimHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 定时器任务
     */
    class AnimTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mFrameIndex < 0 || mState == STATE_STOP) {
                return;
            }

            if (mFrameIndex < mResourceIdList.size()) {
                Message msg = AnimHandler.obtainMessage(MSG_START, 0, 0, null);
                msg.sendToTarget();
            } else {
                mFrameIndex = 0;
                if(!isLooping){
                    Message msg = AnimHandler.obtainMessage(MSG_STOP, 0, 0, null);
                    msg.sendToTarget();
                }
            }
        }
    }

    /**
     * Handler
     */
    private Handler AnimHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:{
                    if(mFrameIndex >= 0 && mFrameIndex < mResourceIdList.size() && mState == STATE_RUNNING){
                        mImageView.setImageResource(mResourceIdList.get(mFrameIndex));
                        mFrameIndex++;
                    }
                }
                break;
                case MSG_STOP:{
                    if (mTimeTask != null) {
                        mFrameIndex = 0;
                        mTimer.purge();
                        mTimeTask.cancel();
                        mState = STATE_STOP;
                        mTimeTask = null;
                        if (isLooping) {
                            mImageView.setImageResource(0);
                        }
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    public interface OnParseListener {
        void onParse(List<Integer> res);
    }
}
