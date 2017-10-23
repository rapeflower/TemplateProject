package com.nino.micro.business.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import android.widget.EditText;

import com.nino.micro.business.R;
import com.nino.micro.business.utils.StringUtils;

/**
 * <p/>
 * 自定义带是删除功能的EditText，仿IOS效果，失去焦点时，删除icon消失
 * <p/>
 * 暂时不支持默认的textCursorDrawable
 * 默认的为textColor为：c03_2c2c2c TextColorHint为：c05_999999：bg：shape_solid_color_f2f2f2_corners_10px
 */
public class MEditText extends EditText {
    private final int DRAWABLE_LEFT = 0;
    private final int DRAWABLE_TOP = 1;
    private final int DRAWABLE_RIGHT = 2;
    private final int DRAWABLE_BOTTOM = 3;

    private final int INDEX = 0;

    private final int PADDING = 0;
    private final int PADDING_LEFT = 1;
    private final int PADDING_TOP = 2;
    private final int PADDING_RIGHT = 3;
    private final int PADDING_BOTTOM = 4;

    private Drawable leftDrawable = null;
    private Drawable topDrawable = null;
    private Drawable rightDrawable = null;
    private Drawable bottomDrawable = null;

    /***
     * 取消button对应的几种模式
     */
    public static final int CLEAR_BUTTON_MODE_NEVER = 0x00000000;
    public static final int CLEAR_BUTTON_MODE_WHILE_EDITING = 0x00000001;
    public static final int CLEAR_BUTTON_MODE_UNLESS_EDITING = 0x00000002;
    public static final int CLEAR_BUTTON_MODE_ALWAYS = 0x00000003;
    private int clearButtonMode = CLEAR_BUTTON_MODE_WHILE_EDITING;

    private DrawableLeftClickListener drawableLeftClickListener;
    private TextWatcher textWatcherListener;
    private TextWatcher defaultTextWatcherListener;
    private OnFocusChangeListener focusChangeListener;
    private OnFocusChangeListener defaultFocusChangeListener;
    private CancelButtonClickListener defaultCancelButtonClickListener;
    private CancelButtonClickListener cancelButtonClickListener;

    private boolean isClearTextWhenMemberPhone = false;
    private int textSize = 0;


    public MEditText(Context context) {
        super(context);
        initLayout(null);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs) {
        initLayoutByAttr(attrs);
        initTextChangedListener();
        initFocusChangeListener();
        initCancelButtonClickListener();
    }

    private void initLayoutByAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        setDefaultBackground(attrs);
        //Padding
        setDefaultPadding(attrs);
        setDefaultTextColor(attrs);
        setDefaultTextColorHint(attrs);
        setDefaultTextSize(attrs);
        initLayoutBySelfAttr(attrs);
    }

    private void initLayoutBySelfAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MEditText);
        if (typedArray == null) {
            return;
        }
        int clearButtonMode = typedArray.getInt(R.styleable.MEditText_clear_button_mode, CLEAR_BUTTON_MODE_WHILE_EDITING);
        setCancelButtonMode(clearButtonMode);
        typedArray.recycle();
    }


    private void initTextChangedListener() {
        defaultTextWatcherListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (textWatcherListener == null) {
                    return;
                }
                textWatcherListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (textWatcherListener == null) {
                    return;
                }
                textWatcherListener.onTextChanged(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //在编辑状态时显示
                if (getCancelButtonMode() == CLEAR_BUTTON_MODE_WHILE_EDITING && isFocused()) {
                    setRightDrawableVisibility(getText().toString().length() > 0 ? VISIBLE : GONE);
                }

                //若引用控件的时候设置了textWatcherListener
                if (textWatcherListener == null) {
                    return;
                }
                textWatcherListener.afterTextChanged(s);

            }
        };

        addTextChangedListener(defaultTextWatcherListener);
    }

    private void initFocusChangeListener() {
        defaultFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                //当输入框里面的是用户的加 *** 的手机号码时，获取焦点的时候清空
                if (hasFocus && isClearTextWhenMemberPhone) {
                    if (isClearMobileMode())
                        setText("");
                }

                if (getCancelButtonMode() == CLEAR_BUTTON_MODE_UNLESS_EDITING) {
                    //获取焦点的时候就不显示了。除了编辑外都出现
                    setRightDrawableVisibility(hasFocus ? GONE : VISIBLE);
                } else if (getCancelButtonMode() == CLEAR_BUTTON_MODE_WHILE_EDITING) {
                    //编辑状态 编辑时出现
                    setRightDrawableVisibility((!hasFocus || getText().toString().length() == 0) ? GONE : VISIBLE);
                }
                //若引用控件的时候设置了focusChangeListener
                if (focusChangeListener == null) {
                    return;
                }
                focusChangeListener.onFocusChange(v, hasFocus);
            }
        };
        setOnFocusChangeListener(defaultFocusChangeListener);
    }

    private void initCancelButtonClickListener() {
        defaultCancelButtonClickListener = new CancelButtonClickListener() {

            @Override
            public void drawableRightClickListener(View view) {
                setText("");
            }
        };
        setCancelButtonClickListener(defaultCancelButtonClickListener);
    }


    /***
     * 设置左右drawable的点击事件
     *
     * @param listener
     */
    public void setDrawableClickListener(DrawableLeftClickListener listener) {
        drawableLeftClickListener = listener;
    }

    /***
     * 设置取消button的点击事件
     *
     * @param listener
     */
    public void setCancelButtonClickListener(CancelButtonClickListener listener) {
        //默认的忽略
        if (listener == defaultCancelButtonClickListener) {
            return;
        }
        cancelButtonClickListener = listener;
    }

    /***
     * 当输入框里面的是用户的加 *** 的手机号码时，获取焦点的时候清空
     *
     * @param isClearText
     */
    public void setClearTextForMemberMobileWhenFocus(boolean isClearText) {
        isClearTextWhenMemberPhone = isClearText;
    }

    /****
     * 是需要清除的手机号码
     *
     * @return
     */
    private boolean isClearMobileMode() {
        boolean isClear;
        String mobile = "";
        if (StringUtils.isBlank(getText().toString()) || StringUtils.isBlank(mobile)) {
            return false;
        }
        String mobileFormat = StringUtils.mobileFormat(mobile);
        isClear = mobileFormat.equals(getText().toString());
        return isClear;
    }


    /***
     * 设置为密码样式
     */
    public void setNormalPassword(int maxLength) {
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setMaxLength(maxLength);
    }

    /***
     * 设置支付密码
     */
    public void setPayPassword(int maxLength) {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        setMaxLength(maxLength);
    }

    /***
     * 设置最大长度
     *
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }




    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
        //默认的忽略
        if (l == defaultFocusChangeListener) {
            return;
        }
        this.focusChangeListener = l;

    }

    /****
     * 设置最右边的icon是否可见
     *
     * @param visibility
     */

    private void setRightDrawableVisibility(int visibility) {

        if (visibility != VISIBLE) {
            setCompoundDrawables(leftDrawable, topDrawable, null, bottomDrawable);
            return;
        }
        if (rightDrawable == null) {
            rightDrawable = getResources().getDrawable(R.drawable.enter_cancel);
            int measureHeight = textSize; //always的时候，高度为0，UI没有渲染出来，所以没有高度
            int height = measureHeight > 0 ? measureHeight : rightDrawable.getIntrinsicHeight();
            int width = measureHeight > 0 ? measureHeight : rightDrawable.getIntrinsicWidth();
            rightDrawable.setBounds(0, 0, width, height);
        }
        setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    /***
     * 设置取消button的样式
     *
     * @param mode
     */
    public void setCancelButtonMode(int mode) {
        clearButtonMode = mode;
        //一直都不显示
        if (clearButtonMode == CLEAR_BUTTON_MODE_NEVER) {
            setRightDrawableVisibility(GONE);
        } else if (clearButtonMode == CLEAR_BUTTON_MODE_ALWAYS ||
                clearButtonMode == CLEAR_BUTTON_MODE_UNLESS_EDITING) {
            //一直显示
            setRightDrawableVisibility(VISIBLE);
        }
    }

    /***
     * 获取取消button的样式
     *
     * @return
     */
    public int getCancelButtonMode() {
        return clearButtonMode;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        //默认的忽略
        if (watcher == defaultTextWatcherListener) {
            return;
        }
        this.textWatcherListener = watcher;

    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        leftDrawable = left;
        topDrawable = top;
        rightDrawable = right;
        bottomDrawable = bottom;
    }

    /***
     * 设置文字大小，默认的30
     *
     * @param attributeSet
     */
    private void setDefaultTextSize(AttributeSet attributeSet) {
        int[] attrs = new int[]{android.R.attr.textSize};
        //style
        setTextSizeByTypedArray(getTypedArrayByStyle(attrs));
        //android:textSize
        setTextSizeByTypedArray(getTypeArrayByAttributes(attrs, attributeSet));
    }

    private void setTextSizeByTypedArray(TypedArray typed) {
        int defaultSize = getResources().getDimensionPixelSize(R.dimen.fs_30);
        if (typed == null) {
            textSize = defaultSize;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize);
            return;
        }
        textSize = typed.getDimensionPixelSize(INDEX, defaultSize);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        typed.recycle();
    }

    /**
     * 设置文字颜色，默认的为c03_2c2c2c
     *
     * @param attributeSet
     */
    private void setDefaultTextColor(AttributeSet attributeSet) {
        int[] attrs = new int[]{android.R.attr.textColor};
        //style
        setTextColorByTypedArray(getTypedArrayByStyle(attrs));
        //android:textColor
        setTextColorByTypedArray(getTypeArrayByAttributes(attrs, attributeSet));
    }

    private void setTextColorByTypedArray(TypedArray typed) {
        if (typed == null) {
            setTextColor(getResources().getColor(R.color.c07_212630));
            return;
        }
        setTextColor(typed.getColor(INDEX, getResources().getColor(R.color.c07_212630)));
        typed.recycle();
    }

    /***
     * 设置TextColorHint，默认的为c05_999999
     *
     * @param attributeSet
     */
    private void setDefaultTextColorHint(AttributeSet attributeSet) {
        int[] attrs = new int[]{android.R.attr.textColorHint};
        //style
        setTextColorHintByTypedArray(getTypedArrayByStyle(attrs));
        //android:textColorHint
        setTextColorHintByTypedArray(getTypeArrayByAttributes(attrs, attributeSet));
    }

    /***
     * 设置TextColorHint
     *
     * @param typed
     */
    private void setTextColorHintByTypedArray(TypedArray typed) {
        if (typed == null) {
            setHintTextColor(getResources().getColor(R.color.c09_979ca8));
            return;
        }
        setHintTextColor(typed.getColor(INDEX, getResources().getColor(R.color.c09_979ca8)));
        typed.recycle();
    }


    /***
     * 设置背景，默认的为shape_solid_f2f2f2_corners_10px样式
     *
     * @param attributeSet
     */
    private void setDefaultBackground(AttributeSet attributeSet) {
        int[] attrs = new int[]{android.R.attr.background};
        //style
        setBackgroundByTypedArray(getTypedArrayByStyle(attrs));
        //android:background
        setBackgroundByTypedArray(getTypeArrayByAttributes(attrs, attributeSet));
    }

    private void setBackgroundByTypedArray(TypedArray typed) {
        if (typed == null) {
            return;
        }
        setBackgroundResource(typed.getResourceId(INDEX, R.drawable.shape_solid_color_f2f2f2_corners_10px));
        typed.recycle();
    }

    /***
     * 设置默认的padding,默认左右为20px
     *
     * @param attrs
     */
    private void setDefaultPadding(AttributeSet attrs) {
        TypedArray typedPadding = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop,
                android.R.attr.paddingRight, android.R.attr.paddingBottom});
        int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_20px);

        if (typedPadding == null) {
            setPadding(defaultPadding, 0, defaultPadding, 0);
            return;
        }
        int padding = typedPadding.getDimensionPixelOffset(PADDING, 0);
        int paddingLeft = padding > 0 ? padding : typedPadding.getDimensionPixelOffset(PADDING_LEFT, defaultPadding);
        int paddingTop = padding > 0 ? padding : typedPadding.getDimensionPixelOffset(PADDING_TOP, 0);
        int paddingRight = padding > 0 ? padding : typedPadding.getDimensionPixelOffset(PADDING_RIGHT, defaultPadding);
        int paddingBottom = padding > 0 ? padding : typedPadding.getDimensionPixelOffset(PADDING_BOTTOM, 0);

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        typedPadding.recycle();
    }


    private TypedArray getTypedArrayByStyle(int[] attrs) {
        TypedValue textAppearanceValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.textAppearance, textAppearanceValue, true);
        TypedArray appearance = getContext().obtainStyledAttributes(textAppearanceValue.resourceId, attrs);
        return appearance;
    }

    private TypedArray getTypeArrayByAttributes(int[] attrs, AttributeSet attrSet) {
        TypedArray typed = getContext().obtainStyledAttributes(attrSet, attrs);
        return typed;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (isClickDrawableLeft(event)) {
                    return onTouchDrawableLeftEventUp();
                }
                if (isClickDrawableRight(event)) {
                    return onTouchDrawableRightEventUp();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean onTouchDrawableLeftEventUp() {
        if (drawableLeftClickListener != null) {
            drawableLeftClickListener.drawableLeftClickListener(this);
            return true;
        }
        return false;
    }

    private boolean onTouchDrawableRightEventUp() {
        if (cancelButtonClickListener != null) {
            cancelButtonClickListener.drawableRightClickListener(this);
            return true;
        }
        if (defaultCancelButtonClickListener != null) {
            defaultCancelButtonClickListener.drawableRightClickListener(this);
            return true;
        }
        return false;
    }

    private boolean isClickDrawableLeft(MotionEvent event) {
        Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT];
        if (drawableLeft == null) {
            return false;
        }
        boolean isLeft = event.getRawX() <= (getLeft() + drawableLeft.getBounds().width());
        return isLeft;
    }

    private boolean isClickDrawableRight(MotionEvent event) {
        Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (drawableRight == null) {
            return false;
        }
        boolean isRight = event.getRawX() >= (getRight() - drawableRight.getBounds().width());
        return isRight;
    }

    /***
     * 左边图片的点击事件
     */
    public interface DrawableLeftClickListener {
        void drawableLeftClickListener(View view);


    }

    /**
     * 右边取消图片的点击事件
     */
    public interface CancelButtonClickListener {
        void drawableRightClickListener(View view);
    }

}
