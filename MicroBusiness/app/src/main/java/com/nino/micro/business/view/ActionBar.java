package com.nino.micro.business.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nino.micro.business.R;
import com.nino.micro.business.utils.DisplayUnitUtils;
import com.nino.micro.business.utils.StringUtils;


/***********
 * @Author rape flower
 * @Date 2017-03-16 14:58
 * @Describe 标题栏，可以通过ActionBar_title_style属性来设置为是通常模式还是带搜索框
 */
public class ActionBar extends LinearLayout {


    /**
     * 同ActionBar_title_style属性
     * 用来设置为是通常模式还是带搜索框
     */
    public static final int NORMAL = 0;
    public static final int SEARCH = 1;
    public static final int SELF_MIDDLE_LAYOUT = 2;

    /**
     * 同ActionBar_search_left_style属性
     * 用来设置为是搜索模式下左边显示的是第一级还是第二级目录；
     */
    public static final int SEARCH_LEFT_FIRST = 0;
    public static final int SEARCH_LEFT_SECOND = 1;
    private int currentStyle = NORMAL;
    private int currentSearchLeftStyle = SEARCH_LEFT_FIRST;

    private RelativeLayout normalLayout;
    private TextView tvTitle;
    private Button btnRight;
    private Button btnLeft;
    private TextView tvSecondLeft;
    private RelativeLayout searchLayout;
    private MEditText etSearch;
    private ImageView imgSearchLeft;
    private Button btnSearchRight;
    private ImageView ivRight; //for H5 添加图片
    private View dividerView;
    private FrameLayout flCart;
    private Button btnCart;
    private TextView tvCount;

    private View barView;
    private Context mContext;
    private LayoutInflater inflater;
    private String titleInfo = "";
    private String secondLeftInfo;
    private String rightInfo;
    private String leftInfo;
    private String searchHint;
    private String searchRightText;
    private int sysDefHeight;//系统默认的标题栏高度
    private int searchHeight; //设置搜索框的高度
    private boolean searchRightImageAdjust = false;
    OnBarClickListener barClickListener;

    public ActionBar(Context context) {
        super(context);
        initLayout(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs, defStyle);
    }

    private void initLayout(Context context) {
        this.initLayout(context, null);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        initLayout(context, attrs, 0);
    }

    private void initLayout(Context context, AttributeSet attrs, int defStyle) {
        this.mContext = context;

        sysDefHeight = DisplayUnitUtils.getActionBarHeight(mContext);
        searchHeight = 8 * sysDefHeight / 11;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置父布局的LinearLayout方向
        setOrientation(VERTICAL);
        barView = inflater.inflate(R.layout.actionbar, this, true);
        setBackgroundResource(R.color.c12_f8f8f8);

        initControl();

        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        editTextParams.height = searchHeight;

        // 根据xml配置的属性设置UI
        initAttributeFromXml(attrs);
        updateSearchLayoutParam();
    }


    private void initControl() {
        normalLayout = (RelativeLayout) barView.findViewById(R.id.rl_actionbar);
        tvTitle = (TextView) barView.findViewById(R.id.tv_title);
        btnRight = (Button) barView.findViewById(R.id.btn_right);
        btnLeft = (Button) barView.findViewById(R.id.btn_left);
        tvSecondLeft = (TextView) barView.findViewById(R.id.tv_second_left);
        searchLayout = (RelativeLayout) barView.findViewById(R.id.rl_search_layout);
        etSearch = (MEditText) barView.findViewById(R.id.et_search);
        imgSearchLeft = (ImageView) barView.findViewById(R.id.iv_search_left);
        btnSearchRight = (Button) barView.findViewById(R.id.btn_search_right);
        ivRight = (ImageView) barView.findViewById(R.id.iv_right); //for H5 添加图片
        dividerView = barView.findViewById(R.id.vw_divider);
        flCart = (FrameLayout) barView.findViewById(R.id.fl_right_cart);
        btnCart = (Button) barView.findViewById(R.id.btn_right_cart);
        tvCount = (TextView) barView.findViewById(R.id.tv_count);

        //设置点击监听
        setVoTaBarClickListener();
    }

    private void updateSearchLayoutParam() {
        if (imgSearchLeft == null) {
            return;
        }
        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        if (imgSearchLeft.getVisibility() == View.GONE) {
            editTextParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        } else {
            editTextParams.leftMargin = 0;
        }
    }

    private void initAttributeFromXml(AttributeSet attrs) {

        if (attrs == null) {
            return;
        }
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.ActionBar_background)) {
            int abBackGround = typed.getResourceId(R.styleable.ActionBar_background, -1);
            setBackgroundResource(abBackGround);
        }
        if (typed.hasValue(R.styleable.ActionBar_title_style)) {
            setStyle(typed.getInt(R.styleable.ActionBar_title_style, 0));
        }

        //设置不同分辨率下的带搜索框样式

        if (typed.hasValue(R.styleable.ActionBar_search_right_image_adjust)) {
            searchRightImageAdjust = typed.getBoolean(R.styleable.ActionBar_search_right_image_adjust, false);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_style)) {
            setSearchLeftLayout(typed.getInt(R.styleable.ActionBar_search_left_style, 0));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_image)) {
            setSearchLeftImage(typed.getResourceId(R.styleable.ActionBar_search_left_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_right_image)) {
            setSearchRightImage(typed.getResourceId(R.styleable.ActionBar_search_right_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_right_text)) {
            setSearchRightText(typed.getString(R.styleable.ActionBar_search_right_text));
        }

        if (typed.hasValue(R.styleable.ActionBar_search_hint)) {
            setSearchEditHint(typed.getString(R.styleable.ActionBar_search_hint));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_image_visibility)) {
            setSearchLeftImageVisibility(typed.getInt(R.styleable.ActionBar_search_left_image_visibility, View.GONE));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_background)) {
            setSearchEditTextBackground(typed.getResourceId(R.styleable.ActionBar_search_edittext_background, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_hint_color)) {
            setSearchEditHintColor(typed.getColor(R.styleable.ActionBar_search_hint_color, getResources().getColor(R.color.c09_979ca8)));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_left)) {
            setSearchEditTextDrawable(typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_left, R.drawable.home_search_gray), 0, 0, 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_top)) {
            setSearchEditTextDrawable(0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_top, R.drawable.home_search_gray), 0, 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_right)) {
            setSearchEditTextDrawable(0, 0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_right, R.drawable.home_search_gray), 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_bottom)) {
            setSearchEditTextDrawable(0, 0, 0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_bottom, R.drawable.home_search_gray));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_style)) {
            setSearchEditTextStyle(typed.getResourceId(R.styleable.ActionBar_search_edittext_style, R.style.text_f05_28_c07_212630));
        }

        //设置不同分辨率下的带标题样式
        if (typed.hasValue(R.styleable.ActionBar_title)) {
            setTitle(typed.getString(R.styleable.ActionBar_title));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_text)) {
            setLeftFirstText(typed.getString(R.styleable.ActionBar_left_first_text));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_first_image)) {
            setLeftFirstImage(typed.getResourceId(R.styleable.ActionBar_left_first_image, -1));
        } else {
            setLeftFirstImage(R.drawable.btn_head_back);
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_visibility)) {
            setLeftFirstVisible(typed.getInt(R.styleable.ActionBar_left_first_visibility, View.VISIBLE));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_second_visibility)) {
            setLeftSecondVisible(typed.getInt(R.styleable.ActionBar_left_second_visibility, View.GONE));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_second_text)) {
            setLeftSecondText(typed.getString(R.styleable.ActionBar_left_second_text));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_text)) {
            setRightText(typed.getString(R.styleable.ActionBar_right_text));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_image)) {
            setRightImage(typed.getResourceId(R.styleable.ActionBar_right_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_visibility)) {
            setRightVisible(typed.getInt(R.styleable.ActionBar_right_visibility, View.GONE));
        }

        if (typed.hasValue(R.styleable.ActionBar_bottom_divider_visibility)) {
            setBottomDividerVisible(typed.getInt(R.styleable.ActionBar_bottom_divider_visibility, View.VISIBLE));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_cart_visibility)) {
            setRightCartVisible(typed.getInt(R.styleable.ActionBar_right_cart_visibility, View.VISIBLE));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_cart_image)) {
            setRightCartImage(typed.getResourceId(R.styleable.ActionBar_right_cart_image, -1));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_text_style)) {
            setRightTextStyle(typed.getResourceId(R.styleable.ActionBar_right_text_style, R.style.text_fs_30_c07_212630));
        }

        typed.recycle();
    }

    /**
     * 获取购物车的btn
     *
     * @return
     */
    public Button getBtnCart() {
        return btnCart;
    }

    /**
     * 获取左边按钮对象
     * @return
     */
    public Button getBtnLeft() {
        return btnLeft;
    }

    /**
     * 设置是通常模式还是搜索框模式
     *
     * @param style NORMAL、SEARCH
     */
    public void setStyle(int style) {
        this.currentStyle = style;
        switch (currentStyle) {
            case NORMAL: {
                normalLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH: {
                normalLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                break;
            }
            case SELF_MIDDLE_LAYOUT:
                normalLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                tvTitle.setVisibility(GONE);
                break;
        }
    }

    /***
     * 在自定义标题栏中间添加UI
     *
     * @param view 自定义的view,不要直接添加TextView
     */
    public void addSelfMiddleLayoutView(View view) {
        if (view instanceof TextView) {
            throw new IllegalArgumentException("if you only add TextView, you can call method setTitle");
        }
        int count = normalLayout.getChildCount();
        View firstChildView = null;
        //没有add view之前第1个view是TextView
        if (count >= 1) {
            firstChildView = normalLayout.getChildAt(1);
        }
        if (firstChildView != null && !(firstChildView instanceof TextView)) {
            //说明已经添加了一个view
            normalLayout.removeViewAt(1);
        }
        //在第一个布局后面添加控件
        normalLayout.addView(view, 1);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tvTitle.setVisibility(GONE);
    }

    /**
     * 设置为是搜索模式下左边显示的是第一级还是第二级目录
     *
     * @param style SEARCH_LEFT_FIRST、SEARCH_LEFT_SECOND
     */
    public void setSearchLeftLayout(int style) {
        this.currentSearchLeftStyle = style;
        // setSearchLayout();
    }

    /**
     * 设置搜索模式下左边的图片是否可见
     *
     * @param visibility View.GONE,VISIBLE...
     */

    public void setSearchLeftImageVisibility(int visibility) {

        imgSearchLeft.setVisibility(visibility);
        if (visibility == View.GONE) {
            updateSearchLayoutParam();
        }

    }

    /**
     * 设置搜索框的hint
     *
     * @param hint 字符串
     */
    public void setSearchEditHint(String hint) {
        this.searchHint = hint;
        if (etSearch == null || searchHint == null) {
            return;
        }

        etSearch.setHint(searchHint);
    }

    /**
     * 设置搜索框的hint颜色
     *
     * @param color 颜色id
     */
    public void setSearchEditHintColor(int color) {
        if (etSearch == null) {
            return;
        }

        etSearch.setHintTextColor(color);
    }

    /**
     * 设置搜索模式下搜索框的左边图片
     *
     * @param drawableIdLeft   左边图片Id
     * @param drawableIdTop    上边图片Id
     * @param drawableIdRight  右边图片Id
     * @param drawableIdBottom 下边图片Id
     */

    public void setSearchEditTextDrawable(int drawableIdLeft, int drawableIdTop, int drawableIdRight, int drawableIdBottom) {
        if (drawableIdLeft > 0) {
            Drawable drawable = mContext.getResources().getDrawable(drawableIdLeft);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            etSearch.setCompoundDrawables(drawable, null, null, null);
        } else if (drawableIdTop > 0) {
            Drawable drawable = mContext.getResources().getDrawable(drawableIdTop);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            etSearch.setCompoundDrawables(null, drawable, null, null);
        } else if (drawableIdRight > 0) {
            Drawable drawable = mContext.getResources().getDrawable(drawableIdRight);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            etSearch.setCompoundDrawables(null, null, drawable, null);
        } else if (drawableIdBottom > 0) {
            Drawable drawable = mContext.getResources().getDrawable(drawableIdBottom);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            etSearch.setCompoundDrawables(null, null, null, drawable);
        }

    }

    /**
     * 设置搜索模式下搜索框的字体样式
     *
     * @param style 图片Id
     */

    public void setSearchEditTextStyle(int style) {
        if (style <= 0) {
            return;
        }
        etSearch.setTextAppearance(getContext(), style);
    }

    /**
     * 设置搜索模式下搜索框的背景
     *
     * @param drawableId 图片Id
     */

    public void setSearchEditTextBackground(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        etSearch.setBackgroundResource(drawableId);
    }

    /**
     * 得到搜索框
     */
    public EditText getSearchEditText() {
        return etSearch;
    }

    /**
     * 得到搜索框右边的button
     */
    public Button getBtnSearchRight() {
        return btnSearchRight;
    }

    /**
     * 得到右边button
     *
     * @return
     */
    public Button getBtnRight() {
        return btnRight;
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param text 字符串
     */

    public void setSearchRightText(String text) {
        if (StringUtils.isBlank(text) && btnSearchRight != null) {
            btnSearchRight.setVisibility(View.GONE);
            return;
        }
        btnSearchRight.setVisibility(View.VISIBLE);
        this.searchRightText = text;
        btnSearchRight.setText(searchRightText);
        btnSearchRight.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 设置搜索模式下的右边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        btnSearchRight.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(drawableId);

        if (searchRightImageAdjust) {
            drawable.setBounds(0, 0, sysDefHeight / 2, sysDefHeight / 2);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        btnSearchRight.setCompoundDrawables(drawable, null, null, null);

    }

    /**
     * 设置搜索模式下的左边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchLeftImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        imgSearchLeft.setVisibility(View.VISIBLE);
        imgSearchLeft.setImageResource(drawableId);
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param textId 字符串Id
     */

    public void setSearchRightText(int textId) {
        this.searchRightText = mContext.getString(textId);
        setSearchRightText(searchRightText);
    }


    /**
     * 设置最右边的文字
     *
     * @param confirm
     */
    public void setRightText(String confirm) {
        if (StringUtils.isBlank(confirm)) {
            return;
        }
        btnRight.setVisibility(View.VISIBLE);
        this.rightInfo = confirm;
        btnRight.setText(rightInfo);
        btnRight.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 获取最右边的文字
     *
     * @return 返回最右边的文字
     */
    public String getRightText() {
        return rightInfo;
    }

    /**
     * 设置最右边的文字
     *
     * @param confirmId 字符串的ID
     */
    public void setRightText(int confirmId) {
        this.rightInfo = mContext.getString(confirmId);
        setRightText(rightInfo);
    }

    /**
     * 设置最右边的控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightVisible(int visible) {
        btnRight.setVisibility(visible);
        ivRight.setVisibility(View.GONE);
    }

    /**
     * 设置最右边的图片
     *
     * @param drawableId 图片资源ID
     */

    public void setRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        btnRight.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnRight.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置最右边的图片，just for H5添加图片
     *
     * @param url 图片资源url
     */
    public void setRightImage(String url) {
        btnRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.VISIBLE);
//        ImageLoaderManager.loadImage(mContext,ivRight, url);
    }

    /**
     * 设置最右边的控件imageview是否可见 just for h5
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightH5ImageVisible(int visible) {
        ivRight.setVisibility(visible);
        btnRight.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param titleId 字符串对应的Id
     */
    public void setTitle(int titleId) {
        if (titleId <= 0) {
            return;
        }
        this.titleInfo = mContext.getString(titleId);
        setTitle(titleInfo);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return;
        }
        this.titleInfo = title;
        tvTitle.setText(titleInfo);
    }

    public String getTitle() {
        return titleInfo;
    }

    /**
     * 设置左边的文字
     *
     * @param leftText 字符串
     */
    public void setLeftFirstText(String leftText) {
        if (StringUtils.isBlank(leftText)) {
            return;
        }
        this.leftInfo = leftText;
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText(leftText);
    }

    /**
     * 设置最左边的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        btnLeft.setVisibility(visible);
    }


    /**
     * 设置最左边的第一个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftFirstVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        btnLeft.setVisibility(visible);
    }


    /**
     * 设置左边的是否可见
     *
     * @param drawableId 图片的资源Id
     */
    public void setLeftFirstImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnLeft.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param leftTextId 字符串
     */
    public void setLeftSecondText(int leftTextId) {
        this.secondLeftInfo = mContext.getString(leftTextId);
        setLeftSecondText(secondLeftInfo);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param leftText 字符串
     */
    public void setLeftSecondText(String leftText) {
        tvSecondLeft.setText(leftText);
        setLeftSecondVisible(View.VISIBLE);
    }

    /**
     * 设置最左边的第二个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftSecondVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        tvSecondLeft.setVisibility(visible);
        if (tvSecondLeft.getVisibility() == View.VISIBLE) {
            tvTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else {
            tvTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        }
    }

    /**
     * 设置底部分割线的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setBottomDividerVisible(int visible) {
        dividerView.setVisibility(visible);
    }

    /**
     * 在满赠、满减页中，有在右上角的购物车图标上显示当前购物车中的
     * 商品数量，当前action bar无法满足次需求，所以又新增了控件和功能
     *
     * @param visible
     */
    public void setRightCartVisible(int visible) {
        flCart.setVisibility(visible);
    }

    public void setRightCartImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
        btnCart.setCompoundDrawables(drawable, null, null, null);
    }

    public TextView getTvCount() {
        return tvCount;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        setNormalOnClickListener(l);
        setSearchOnClickListener(l);
    }

    /***
     * normal样式下的点击事件
     *
     * @param listener
     */
    public void setNormalOnClickListener(OnClickListener listener) {
        if (tvSecondLeft != null) tvSecondLeft.setOnClickListener(listener);
        if (btnRight != null) btnRight.setOnClickListener(listener);
        if (btnLeft != null) btnLeft.setOnClickListener(listener);
        if (ivRight != null) ivRight.setOnClickListener(listener);
        if (btnCart != null) btnCart.setOnClickListener(listener);
    }

    /**
     * search样式下的点击事件
     *
     * @param listener
     */
    public void setSearchOnClickListener(OnClickListener listener) {
        if (btnSearchRight != null) btnSearchRight.setOnClickListener(listener);
        if (imgSearchLeft != null) imgSearchLeft.setOnClickListener(listener);
        if (etSearch != null) etSearch.setOnClickListener(listener);
    }

    /**
     * 设置标题栏点击监听
     * @author rape flower 20161019
     */
    private void setVoTaBarClickListener() {
        if (tvSecondLeft != null) tvSecondLeft.setOnClickListener(clickListener);
        if (btnRight != null) btnRight.setOnClickListener(clickListener);
        if (btnLeft != null) btnLeft.setOnClickListener(clickListener);
        if (ivRight != null) ivRight.setOnClickListener(clickListener);
        if (btnCart != null) btnCart.setOnClickListener(clickListener);

        if (btnSearchRight != null) btnSearchRight.setOnClickListener(clickListener);
        if (imgSearchLeft != null) imgSearchLeft.setOnClickListener(clickListener);
        if (etSearch != null) etSearch.setOnClickListener(clickListener);
    }

    /**
     * 标题栏点击监听
     * <p>
     *    onBarLeftBackClick btnLeft/imgSearchLeft
     *    onBarRightClick  btnRight/ivRight/btnCart/btnSearchRight
     *    onBarSecondClick tvSecondLeft
     *    onBarSearchEditTextClick  etSearch
     * </p>
     * @author rape flower 20161019
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_left:
                case R.id.iv_search_left:
                    //onBarLeftBackClick
                    if (barClickListener != null) {
                        barClickListener.onBarLeftBackClick(v.getId());
                    }
                    break;
                case R.id.btn_right:
                case R.id.iv_right:
                case R.id.btn_right_cart:
                case R.id.btn_search_right:
                    //onBarRightClick
                    if (barClickListener != null) {
                        barClickListener.onBarRightClick(v.getId());
                    }
                    break;
                case R.id.tv_second_left:
                    //onBarSecondClick
                    if (barClickListener != null) {
                        barClickListener.onBarSecondClick(v.getId());
                    }
                    break;
                case R.id.et_search:
                    //onBarSearchEditTextClick
                    if (barClickListener != null) {
                        barClickListener.onBarSearchEditTextClick(v.getId());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置右边文字的样式
     *
     * @param styleId
     */
    public void setRightTextStyle(int styleId) {
        if (styleId <= 0) {
            return;
        }
        btnRight.setTextAppearance(getContext(), styleId);
    }


    /**
     * z针对首页上面action
     *
     * @param position
     */
    public void setSearchPosition(int position) {
        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        editTextParams.addRule(position);
        editTextParams.bottomMargin = sysDefHeight / 11 * 3 / 2 / 2;

        RelativeLayout.LayoutParams leftimageParams = (RelativeLayout.LayoutParams) imgSearchLeft.getLayoutParams();
        leftimageParams.addRule(RelativeLayout.ALIGN_BOTTOM, etSearch.getId());
        imgSearchLeft.setPadding(imgSearchLeft.getPaddingLeft(), imgSearchLeft.getPaddingTop(),
                imgSearchLeft.getPaddingRight(), sysDefHeight / 11 * 3 / 2);

        RelativeLayout.LayoutParams btnRightLayoutParams = (RelativeLayout.LayoutParams) btnSearchRight.getLayoutParams();
        btnRightLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, etSearch.getId());
        btnSearchRight.setPadding(btnSearchRight.getPaddingLeft(), btnSearchRight.getPaddingTop(),
                btnSearchRight.getPaddingRight(), sysDefHeight / 11 * 3 / 2);

        //imgSearchLeft.setY(etSearch.getY() + sysDefHeight / 2 + imgSearchLeft.getHeight()/2) ;
    }

    /**
     * 设置ActionBar的点击回调监听
     * @param barClickListener
     */
    public void setOnBarClickListener(OnBarClickListener barClickListener) {
        this.barClickListener = barClickListener;
    }

    /**
     * ActionBar的点击事件
     * <p>
     *    OnBarClickListener
     *
     *    onBarLeftBackClick btnLeft/imgSearchLeft
     *    onBarRightClick  btnRight/ivRight/btnCart/btnSearchRight
     *    onBarSecondClick tvSecondLeft
     *    onBarSearchEditTextClick  etSearch
     * </p>
     * @author rape flower 20161019
     */
    public interface OnBarClickListener{
       void onBarLeftBackClick(int id);
       void onBarRightClick(int id);
       void onBarSecondClick(int id);
       void onBarSearchEditTextClick(int id);
    }
}
