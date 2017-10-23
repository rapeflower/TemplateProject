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
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {


    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    private FrameLayout bottomLayout;
    private FrameLayout topLayout;

    protected final ImageView mFooterImage;
    protected final ImageView mHeaderImage;
    protected final LinearLayout mHeaderLayout;

    private boolean mUseIntrinsicAnimation;

    private final TextView mFooterText;
    private final TextView mSubHeaderText;

    protected final Mode mMode;
    protected final Orientation mScrollDirection;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context);

        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(R.layout.j1_pull_to_refresh_header_vertical, this);
                break;
        }
        topLayout = (FrameLayout) findViewById(R.id.fl_top_layout); //顶部
        bottomLayout = (FrameLayout) findViewById(R.id.fl_bottom_layout); //底部
        mFooterText = (TextView) bottomLayout.findViewById(R.id.pull_to_refresh_text);
        mSubHeaderText = (TextView) bottomLayout.findViewById(R.id.pull_to_refresh_sub_text);
        mFooterImage = (ImageView) bottomLayout.findViewById(R.id.pull_to_refresh_image);
        mHeaderImage = (ImageView) topLayout.findViewById(R.id.iv_pull_loading);
        mHeaderLayout = (LinearLayout) topLayout.findViewById(R.id.ll_header_layout);

        initLayout(context, mode, scrollDirection);
        initAttrsFromXml(context, attrs);
        reset();
    }

    /**
     * 初始化布局
     *
     * @param context
     * @param mode
     * @param scrollDirection
     */
    private void initLayout(Context context, final Mode mode, final Orientation scrollDirection) {

        LayoutParams lp = (LayoutParams) bottomLayout.getLayoutParams();
        switch (mode) {
            case PULL_FROM_END:
                //上拉加载更多显示之前的
                bottomLayout.setVisibility(View.VISIBLE);
                topLayout.setVisibility(View.GONE);
                lp.gravity = isVerticalScrollDirection() ? Gravity.TOP : Gravity.LEFT;

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
                break;

            case PULL_FROM_START:
            default:
                //下拉刷新显示现在的
                topLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.GONE);
                lp = (LayoutParams) topLayout.getLayoutParams();
                lp.gravity = isVerticalScrollDirection() ? Gravity.BOTTOM : Gravity.RIGHT;
                // Load in labels
                break;
        }
    }

    /**
     * 从自定义属性中设置布局文件
     *
     * @param context
     * @param attrs
     */
    private void initAttrsFromXml(Context context, TypedArray attrs) {

        if (attrs == null) {
            return;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
            setTextAppearance(styleID.data);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
            setSubTextAppearance(styleID.data);
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
            if (null != colors) {
                setTextColor(colors);
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
            if (null != colors) {
                setSubTextColor(colors);
            }
        }

        // Try and get defined drawable from Attrs
        Drawable imageDrawable = null;
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
            imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
        }

        // Check Specific Drawable from Attrs, these overrite the generic
        // drawable attr above
        switch (mMode) {
            case PULL_FROM_START:
            default:
                break;

            case PULL_FROM_END:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
                    Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
                }
                break;
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        }
        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable);

    }


    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    private boolean isVerticalScrollDirection() {
        return mScrollDirection == Orientation.VERTICAL;
    }

    private boolean isVerticalPullFromStartScroll() {
        return mScrollDirection == Orientation.VERTICAL && mMode.equals(Mode.PULL_FROM_START);
    }

    public final int getContentSize() {
        switch (mScrollDirection) {
            case HORIZONTAL:
                return bottomLayout.getWidth();
            case VERTICAL:
            default:
                switch (mMode) {
                    case PULL_FROM_START:
                        return topLayout.getHeight();
                    case PULL_FROM_END:
                    default:
                        return bottomLayout.getHeight();
                }
        }
    }

    public final void hideAllViews() {
        if (isVerticalPullFromStartScroll()) {
            if (View.VISIBLE == topLayout.getVisibility()) {
                topLayout.setVisibility(View.INVISIBLE);
            }
            return;
        }
        if (View.VISIBLE == mFooterText.getVisibility()) {
            mFooterText.setVisibility(View.INVISIBLE);
        }

        if (View.VISIBLE == mFooterImage.getVisibility()) {
            mFooterImage.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.INVISIBLE);
        }


    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public final void pullToRefresh() {
        if (null != mFooterText) {
            mFooterText.setText(mPullLabel);
        }

        // Now call the callback
        pullToRefreshImpl();
    }

    public final void refreshing() {
        if (null != mFooterText) {
            mFooterText.setText(mRefreshingLabel);
        }

        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mFooterImage.getDrawable()).start();
        } else {
            // Now call the callback
            refreshingImpl();
        }

        if (null != mSubHeaderText) {
            mSubHeaderText.setVisibility(View.GONE);
        }
    }

    public final void releaseToRefresh() {
        if (null != mFooterText) {
            mFooterText.setText(mReleaseLabel);
        }

        // Now call the callback
        releaseToRefreshImpl();
    }

    public final void reset() {
        //如果是下拉加载更多，直接调用resetImpl即可
        if (isVerticalPullFromStartScroll()) {
            resetImpl();
            return;
        }
        resetFootLayout();
    }

    private void resetFootLayout() {
        if (null != mFooterText) {
            mFooterText.setText(mPullLabel);
        }
        mFooterImage.setVisibility(View.VISIBLE);

        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mFooterImage.getDrawable()).stop();
        } else {
            // Now call the callback
            resetImpl();
        }

        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        setSubHeaderText(label);
    }

    public final void setLoadingDrawable(Drawable imageDrawable) {
        // Set Drawable
        mFooterImage.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        // Now call the callback
        onLoadingDrawableSet(imageDrawable);
    }

    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    public void setTextTypeface(Typeface tf) {
        mFooterText.setTypeface(tf);
    }

    public final void showInvisibleViews() {
        if (isVerticalPullFromStartScroll()) {
            if (View.INVISIBLE == topLayout.getVisibility()) {
                topLayout.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (View.INVISIBLE == mFooterText.getVisibility()) {
            mFooterText.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mFooterImage.getVisibility()) {
            mFooterImage.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.VISIBLE);
        }


    }

    /**
     * Callbacks for derivative Layouts
     */

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();


    private void setSubHeaderText(CharSequence label) {
        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(label)) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setText(label);

                // Only set it to Visible if we're GONE, otherwise VISIBLE will
                // be set soon
                if (View.GONE == mSubHeaderText.getVisibility()) {
                    mSubHeaderText.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setSubTextAppearance(int value) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setSubTextColor(ColorStateList color) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    private void setTextAppearance(int value) {
        if (null != mFooterText) {
            mFooterText.setTextAppearance(getContext(), value);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setTextColor(ColorStateList color) {
        if (null != mFooterText) {
            mFooterText.setTextColor(color);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

}
