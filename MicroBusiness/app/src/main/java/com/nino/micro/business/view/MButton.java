package com.nino.micro.business.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;

import com.nino.micro.business.R;

public class MButton extends Button {

    /**
     * 按钮样式模式
     */
    public static final int MODEL_RED_SQUARE = 1;//添加购物车按钮样式
    public static final int MODEL_RED_CIRCULAR = 2;//圆角按钮颜色
    public static final int MODEL_YELLOW_SQUARE = 3;//收藏按钮样式
    public static final int MODEL_GRAY_CIRCULAR = 4;//普通按钮
    public static final int MODEL_RED_UP_SQUARE_DOWN_CIRCULAR = 5;//上直角下圆角
    public static final int MODEL_GRAY_CIRCULAR_STROKE_TRANSPARENT = 6;//普通灰色边框圆角填充透明
    public static final int MODEL_RED_CIRCULAR_STROKE_TRANSPARENT = 7;//红色边框圆角填充透明
    public static final int MODEL_YELLOW_CIRCULAR = 8;//
    private final int TEXT_SIZE_IN_TEXT_ATTRS = 0;//textAttrs数组中默认给点的字体大小
    private final int TEXT_COLOR_SIZE_IN_TEXT_ATTRS = 1;//textAttrs数组中默认给点的字体颜色
    private final int BACKGROUND_IN_TEXT_ATTRS = 2;


    private Context mContext;
    private int model_style = -1;//样式值

    public MButton(Context context) {
        super(context);
        initLayout(context);
    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs, defStyleAttr);
    }

    private void initLayout(Context context) {
        initLayout(context, null);

    }

    private void initLayout(Context context, AttributeSet attrs) {
        initLayout(context, attrs, 0);

    }

    private void initLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;

        // 根据xml配置的属性设置UI
        initAttributeFromXml(attrs);
        //设置文字默认居中
        setGravity(Gravity.CENTER);

    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttributeFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.MButton);
        TypedArray textAttrs = this.mContext.obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize, android.R.attr.textColor, android.R.attr.background});
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.MButton_show_model)) {
            model_style = typed.getInt(R.styleable.MButton_show_model, -1);
            if (model_style < 0) {
                return;
            }
            setAttributeByStyleModel(model_style, textAttrs);
        }

        typed.recycle();
        textAttrs.recycle();
        setPaddingByGravityCenter(attrs);
    }

    /***
     * 如果没有设置padding的话，强制设置为0，可以让控件的内容居中
     *
     * @param attrs
     */
    private void setPaddingByGravityCenter(AttributeSet attrs) {
        TypedArray paddingAttrs = mContext.obtainStyledAttributes(attrs, new int[]{android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom});
        int padding = paddingAttrs.getDimensionPixelSize(0, 0);
        int paddingLeft = paddingAttrs.getDimensionPixelSize(1, padding);
        int paddingTop = paddingAttrs.getDimensionPixelSize(2, padding);
        int paddingRight = paddingAttrs.getDimensionPixelSize(3, padding);
        int paddingBottom = paddingAttrs.getDimensionPixelSize(4, padding);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        paddingAttrs.recycle();
    }

    /**
     * 根据属性设置样式
     *
     * @param model_style
     */
    public void setAttributeByStyleModel(int model_style, TypedArray textTyped) {
        switch (model_style) {
            case MODEL_RED_CIRCULAR://全红色圆角
                //设置基于默认的主体样式可微调的样式
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_30, R.style.mbtn_text_red_circular, R.drawable.btn_c01_fa4532_corners_6px);
                invalidate();
                break;
            case MODEL_RED_SQUARE://全红色直角
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_34, R.style.mbtn_text_red_square, R.drawable.btn_c01_fa4532);
                invalidate();
                break;
            case MODEL_YELLOW_SQUARE://黄色直角
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_34, R.style.mbtn_text_yellow_square, R.drawable.btn_c02_ff7e00);
                invalidate();
                break;
            case MODEL_GRAY_CIRCULAR://灰色圆角
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_30, R.style.mbtn_text_gray_circular, R.drawable.btn_white);
                invalidate();
                break;
            case MODEL_RED_UP_SQUARE_DOWN_CIRCULAR://红色上直角下圆角
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_34, R.style.mbtn_text_red_square, R.drawable.btn_c01_fa4532_corners_bottom_6px);
                invalidate();
                break;
            case MODEL_GRAY_CIRCULAR_STROKE_TRANSPARENT://灰色边框圆角填充透明
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_30, R.style.mbtn_text_gray_circular, R.drawable.btn_c10_dddddd_corners_stroke_6px);
                invalidate();
                break;
            case MODEL_RED_CIRCULAR_STROKE_TRANSPARENT://红色边框圆角填充透明
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_30, R.style.mbtn_text_gray_circular, R.drawable.btn_c01_fa4532_corners_stroke_6px);
                invalidate();
                break;
            case MODEL_YELLOW_CIRCULAR://黄色圆角
                adjustStyleBaseOnModel(textTyped, R.dimen.fs_34, R.style.mbtn_text_yellow_square, R.drawable.btn_c02_ff7e00_corners_6px);
                invalidate();
                break;
        }
    }

    /**
     * 基于基本的主体样式微调样式
     *
     * @param textTyped             主体样式 传入的是属性的数组 new int[]{android.R.attr.textSize, android.R.attr.textColor, android.R.attr.background}
     * @param defaultTextSize       主体样式的默认字体大小
     * @param defaultTextColorStyle 主体样式的默认字体颜色样式
     * @param defaultBtnBackGround  主体样式的btn的背景
     */
    private void adjustStyleBaseOnModel(TypedArray textTyped, int defaultTextSize, int defaultTextColorStyle, int defaultBtnBackGround) {
        //设置字体
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textTyped.getDimensionPixelSize(TEXT_SIZE_IN_TEXT_ATTRS, getResources().getDimensionPixelSize(defaultTextSize)));
        if (textTyped.hasValue(TEXT_COLOR_SIZE_IN_TEXT_ATTRS)) {
            //这样设置只是单纯的设置字体颜色，按压和不按压，字体的颜色是defaultTextcolorstyle的drawable中的normal下的单一颜色
            int textColor = textTyped.getColor(TEXT_COLOR_SIZE_IN_TEXT_ATTRS, defaultTextColorStyle);
            setTextColor(textColor);
        } else {
            //默认的字体颜色样式,这种设置方式可以展现字体在按压和不按压情况下的颜色变化
            setTextAppearance(mContext, defaultTextColorStyle);
        }
        setBackgroundResource(textTyped.getResourceId(BACKGROUND_IN_TEXT_ATTRS, defaultBtnBackGround));
    }
}
