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
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.animation.J1AnimationDrawable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class J1FooterLoadingLayout extends LoadingLayout {

    private J1AnimationDrawable footerAnimationDrawable;
    private List<Integer> footer_loading_list = new ArrayList<Integer>(){
        {
         add(R.drawable.footer_loading_710000);
         add(R.drawable.footer_loading_710001);
         add(R.drawable.footer_loading_710002);
         add(R.drawable.footer_loading_710003);
         add(R.drawable.footer_loading_710004);
         add(R.drawable.footer_loading_710005);
         add(R.drawable.footer_loading_710006);
         add(R.drawable.footer_loading_710007);
         add(R.drawable.footer_loading_710008);
         add(R.drawable.footer_loading_710009);
//         add(R.drawable.footer_loading_710010);
//         add(R.drawable.footer_loading_710011);
//         add(R.drawable.footer_loading_710012);
//         add(R.drawable.footer_loading_710013);
//         add(R.drawable.footer_loading_710014);
//         add(R.drawable.footer_loading_710015);
//         add(R.drawable.footer_loading_710016);
        }
    };

    public J1FooterLoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        if (footerAnimationDrawable == null) {
            footerAnimationDrawable = new J1AnimationDrawable();
        }
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            final int dHeight = imageDrawable.getIntrinsicHeight();
            final int dWidth = imageDrawable.getIntrinsicWidth();

            /**
             * We need to set the width/height of the ImageView so that it is
             * square with each side the size of the largest drawable dimension.
             * This is so that it doesn't clip when rotated.
             */
            ViewGroup.LayoutParams lp = mFooterImage.getLayoutParams();
            lp.width = lp.height = Math.max(dHeight, dWidth);
            mFooterImage.requestLayout();

            /**
             * We now rotate the Drawable so that is at the correct rotation,
             * and is centered.
             */
            mFooterImage.setScaleType(ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f);
            matrix.postRotate(0f, lp.width / 2f, lp.height / 2f);
            mFooterImage.setImageMatrix(matrix);
        }
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
    }

    @Override
    protected void releaseToRefreshImpl() {
        if (footerAnimationDrawable != null) {
            footerAnimationDrawable.setAnimation(mHeaderImage, footer_loading_list);
            footerAnimationDrawable.start(true, 60);
        }
    }

    @Override
    protected void resetImpl() {
        if (footerAnimationDrawable != null) {
            footerAnimationDrawable.stop();
        }
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.footer_loading_list;
    }
}
