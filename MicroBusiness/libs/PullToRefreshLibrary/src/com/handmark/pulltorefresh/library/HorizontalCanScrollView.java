package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 可以水平滑动的scrollview
 */
public class HorizontalCanScrollView extends ScrollView {
    int mLastAct = -1;
    boolean mIntercept = false;
    private float mDX, mDY, mLX, mLY;
    private OnScrollChangedListener scrollListener;

    public HorizontalCanScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HorizontalCanScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalCanScrollView(Context context) {
        super(context);
    }

    //public void setOnScrollListener{};

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (scrollListener != null) {
            scrollListener.onScrollChanged(HorizontalCanScrollView.this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(HorizontalCanScrollView view, int x, int y, int oldx, int oldy);
    }

    public void setScrollChangedListener(OnScrollChangedListener listener) {
        scrollListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = mDY = 0f;
                mLX = ev.getX();
                mLY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                mDX += Math.abs(curX - mLX);
                mDY += Math.abs(curY - mLY);
                mLX = curX;
                mLY = curY;

                if (mIntercept && mLastAct == MotionEvent.ACTION_MOVE) {
                    return false;
                }

                if (mDX > mDY) {

                    mIntercept = true;
                    mLastAct = MotionEvent.ACTION_MOVE;
                    return false;
                }

        }
        mLastAct = ev.getAction();
        mIntercept = false;
        return super.onInterceptTouchEvent(ev);
    }

}