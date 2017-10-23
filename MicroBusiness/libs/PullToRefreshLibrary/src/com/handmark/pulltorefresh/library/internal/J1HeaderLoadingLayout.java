/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.animation.J1AnimationDrawable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class J1HeaderLoadingLayout extends LoadingLayout {

    private Context context;
    private J1AnimationDrawable headerAnimationDrawable;
    private List<Integer> header_loading_list = new ArrayList<Integer>(){
     {
         add(R.drawable.pull_loading_10001);
         add(R.drawable.pull_loading_10003);
         add(R.drawable.pull_loading_10005);
         add(R.drawable.pull_loading_10007);
         add(R.drawable.pull_loading_10009);
         add(R.drawable.pull_loading_10011);
         add(R.drawable.pull_loading_10013);
         add(R.drawable.pull_loading_10015);
         add(R.drawable.pull_loading_10017);
         add(R.drawable.pull_loading_10019);
         add(R.drawable.pull_loading_10021);
         add(R.drawable.pull_loading_10023);
         add(R.drawable.pull_loading_10025);
         add(R.drawable.pull_loading_10027);
         add(R.drawable.pull_loading_10029);
         add(R.drawable.pull_loading_10031);
         add(R.drawable.pull_loading_10033);
         add(R.drawable.pull_loading_10035);
         add(R.drawable.pull_loading_10037);
         add(R.drawable.pull_loading_10039);
         add(R.drawable.pull_loading_10041);
         add(R.drawable.pull_loading_10043);
         add(R.drawable.pull_loading_10045);
         add(R.drawable.pull_loading_10047);
         add(R.drawable.pull_loading_10049);
         add(R.drawable.pull_loading_10051);
         add(R.drawable.pull_loading_10053);
         add(R.drawable.pull_loading_10055);
         add(R.drawable.pull_loading_10057);
         add(R.drawable.pull_loading_10059);
         add(R.drawable.pull_loading_10061);
         add(R.drawable.pull_loading_10063);
         add(R.drawable.pull_loading_10065);
         add(R.drawable.pull_loading_10067);
         add(R.drawable.pull_loading_10069);
         add(R.drawable.pull_loading_10071);
         //add(R.drawable.pull_loading_10073);
         //add(R.drawable.pull_loading_10075);
     }
    };

    public J1HeaderLoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        this.context = context;
        if (headerAnimationDrawable == null) {
            headerAnimationDrawable = new J1AnimationDrawable();
        }
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }

    @Override
    protected void pullToRefreshImpl() {
        // Only start reset Animation, we've previously show the rotate anim
    }

    @Override
    protected void refreshingImpl() {
        //
        if (headerAnimationDrawable != null) {
            headerAnimationDrawable.setAnimation(mHeaderImage, header_loading_list);
            headerAnimationDrawable.start(true, 80);
        }
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
        if (headerAnimationDrawable != null) {
            headerAnimationDrawable.stop();
        }
        mHeaderImage.setImageResource(R.drawable.pull_loading_10001);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.pull_loading_10001;
    }
}
