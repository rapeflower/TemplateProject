package com.nino.micro.business.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nino.micro.business.R;
import com.nino.micro.business.utils.KeyBoardUtils;
import com.nino.micro.business.utils.StringUtils;

/**
 * 自定义对话框
 */
public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        this(context, R.style.CommonDialog);

    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        //在dismiss对话框的时候，它所在的activity因为一些原因已经先退出了，所以会出现java.lang.IllegalArgumentException: View not attached to window manager
        try {
            super.dismiss();
        } catch (Exception e) {

        }

    }

    /**
     * 实现该接口，即可改变Message内容
     */
    public interface onChangeMessage {
        void updateMessageContent();
    }


    /**
     * 所有的方法执行完都会返回一个Builder使得后面可以直接create和show
     */
    public static class Builder implements onChangeMessage {
        private final CommonDialogController.CommonDialogParams P;
        private Context mContext;

        private LinearLayout topLayout;
        private TextView titleView;
        private LinearLayout messageLayout;
        private TextView messageView;
        private LinearLayout buttonLayout;
        private Button okBtn;
        private Button cancelBtn;
        private View dividerView;
        private MEditText userInputView;
        private View vwVerDivider;

        private View layoutView;

        /**
         * Constructor using a context for this builder and the {@link AlertDialog} it creates.
         */
        public Builder(Context context) {
            this(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        }

        /**
         * Constructor using a context and theme for this builder and
         * the {@link AlertDialog} it creates.  The actual theme
         * that an AlertDialog uses is a private implementation, however you can
         * here supply either the name of an attribute in the theme from which
         * to get the dialog's style (such as {@link android.R.attr#alertDialogTheme}
         * or one of the constants
         * {@link AlertDialog#THEME_TRADITIONAL AlertDialog.THEME_TRADITIONAL},
         * {@link AlertDialog#THEME_HOLO_DARK AlertDialog.THEME_HOLO_DARK}, or
         * {@link AlertDialog#THEME_HOLO_LIGHT AlertDialog.THEME_HOLO_LIGHT}.
         */
        public Builder(Context context, int theme) {
            this.mContext = context;
            P = new CommonDialogController.CommonDialogParams(new ContextThemeWrapper(
                    context, R.style.CommonDialog));
        }


        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        //@Override
        public Builder setTitle(int title) {
            P.mTitle = P.mContext.getText(title).toString();
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(String message) {
            P.mMessage = message;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        //@Override
        public Builder setMessage(int message) {
            P.mMessage = P.mContext.getText(message).toString();
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(String message, int color) {
            P.mMessage = message;
            P.messageColor = color;
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(int message, int color) {
            P.mMessage = P.mContext.getText(message).toString();
            P.messageColor = color;
            return this;
        }

        /**
         * 设置整个背景
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            P.mConvertView = v;
            return this;
        }

        /***
         * 是否显示编辑框
         *
         * @param isShowInputView
         */
        public void setIsShowEditText(boolean isShowInputView) {
            P.isShowInputView = isShowInputView;
            updateShowInputView();

        }


        /**
         * 得到输入框的内容，必须create()之后才能取到userInputView
         */
        public String getJ1EditTextContent() {
            if (userInputView == null) {
                return "";
            }
            return userInputView.getText().toString();

        }


        /**
         * 取得输入框 ，必须create()之后才能取到userInputView
         */
        public MEditText getJ1EditText() {
            if (!isInputViewShowing()) {
                setIsShowEditText(true);
            }
            return userInputView;
        }

        /**
         * EditText是否显示
         *
         * @return true 显示 false 不显示
         */
        private boolean isInputViewShowing() {
            return (P.isShowInputView) && userInputView != null;
        }

        /**
         * 是否显示可输入的EditText
         */
        private void updateShowInputView() {
            if (userInputView == null) {
                return;
            }
            if (isInputViewShowing()) {
                userInputView.setVisibility(View.VISIBLE);
            } else {
                userInputView.setVisibility(View.GONE);
            }
            resetDividerMarginTop();
        }

        /**
         * 当有密码输入框的时候，需要将id-> @+id/vw_ver_divider的
         * 控件的layout_marginTop设置为0
         */
        private void resetDividerMarginTop() {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vwVerDivider.getLayoutParams();
            if (isInputViewShowing()) {
                lp.topMargin = 0;
            } else {
                lp.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
            }
        }

        /**
         * 设置确定按钮和其点击事件
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(positiveButtonText).toString();
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            P.mPositiveButtonText = positiveButtonText;
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * 设置取消按钮和其事件
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(negativeButtonText).toString();
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            P.mNegativeButtonText = negativeButtonText;
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * 设置标题栏的UI
         */
        private void setTitleLayout() {
            if (StringUtils.isBlank(P.mTitle)) {
                titleView.setVisibility(View.GONE);
                return;
            }

            titleView.setText(P.mTitle);
            titleView.setVisibility(View.VISIBLE);
        }

        /**
         * 设置message的UI
         */
        private void setMessageLayout() {
            if (StringUtils.isBlank(P.mMessage)) {
                messageLayout.setVisibility(View.GONE);
                return;
            }
            messageView.setText(P.mMessage);
            messageLayout.setVisibility(View.VISIBLE);
            if (P.messageColor != 0) {
                messageView.setTextColor(mContext.getResources().getColor(P.messageColor));
            }
        }

        private void setContentLayout() {

            if (P.mConvertView == null) {
                topLayout.setBackgroundResource(R.drawable.bg_common_dialog_top);
                return;
            }
            //如果设置了contentView，而不是设置message
            // if no message set add the contentView to the dialog body
            messageLayout.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
            topLayout.setBackgroundColor(Color.TRANSPARENT);
            // messageLayout.removeAllViews();
            messageLayout.addView(P.mConvertView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        /**
         * 根据设置是不是设置Button，来确定是显示几个button
         */
        private void setButtonLayout() {

            //没有任何两个Button
            if (StringUtils.isBlank(P.mNegativeButtonText) && StringUtils.isBlank(P.mPositiveButtonText)) {
                buttonLayout.setVisibility(View.GONE);
                return;
            }
            buttonLayout.setVisibility(View.VISIBLE);

            //取消
            if (StringUtils.isNotBlank(P.mNegativeButtonText) && StringUtils.isBlank(P.mPositiveButtonText)) {
                cancelBtn.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonLayout.setBackgroundColor(Color.TRANSPARENT);
                cancelBtn.setBackgroundResource(R.drawable.bg_common_dialog_bottom);

            }
            //Ok
            if (StringUtils.isNotBlank(P.mPositiveButtonText) && StringUtils.isBlank(P.mNegativeButtonText)) {
                okBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonLayout.setBackgroundColor(Color.TRANSPARENT);
                okBtn.setBackgroundResource(R.drawable.bg_common_dialog_bottom);

            }
            //含有两个button

            //设置监听事件
            if (StringUtils.isNotBlank(P.mNegativeButtonText)) {
                cancelBtn.setText(P.mNegativeButtonText);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (P.mNegativeButtonListener == null) {
                            P.dialog.dismiss();
                            return;
                        }
                        P.mNegativeButtonListener.onClick(P.dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
                cancelBtn.setVisibility(View.VISIBLE);
            }

            if (StringUtils.isNotBlank(P.mPositiveButtonText)) {
                okBtn.setText(P.mPositiveButtonText);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (P.mPositiveButtonListener == null) {
                            P.dialog.dismiss();
                            return;
                        }
                        P.mPositiveButtonListener.onClick(P.dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
                okBtn.setVisibility(View.VISIBLE);
            }
        }


        //  @Override
        public CommonDialog create() {
            CommonDialog dialog = new CommonDialog(mContext, R.style.CommonDialog);
            dialog.addContentView(getCommonDialogView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (userInputView == null || userInputView.getVisibility() != View.VISIBLE) {
                        return;
                    }
                    KeyBoardUtils.closeKeyboard(userInputView);
                }
            });
            P.dialog = dialog;
            return dialog;
        }

        /**
         * 获取dialog的view
         *
         * @return
         */
        private View getCommonDialogView() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutView = inflater.inflate(R.layout.dialog_common, null);
            initControls();

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(params);

            updateCommonDialogLayout();

            return layoutView;
        }

        private void initControls() {
            topLayout = (LinearLayout) layoutView.findViewById(R.id.ll_top_layout);
            titleView = (TextView) layoutView.findViewById(R.id.tv_title);
            messageLayout = (LinearLayout) layoutView.findViewById(R.id.ll_message_layout);
            messageView = (TextView) layoutView.findViewById(R.id.tv_message);
            buttonLayout = (LinearLayout) layoutView.findViewById(R.id.ll_button_layout);
            okBtn = (Button) layoutView.findViewById(R.id.btn_ok);
            cancelBtn = (Button) layoutView.findViewById(R.id.btn_cancel);
            dividerView = layoutView.findViewById(R.id.vw_divider);
            userInputView = (MEditText) layoutView.findViewById(R.id.et_user_input);
            vwVerDivider = layoutView.findViewById(R.id.vw_ver_divider);
        }

        /**
         * 更新commonDialog的UI
         */
        private void updateCommonDialogLayout() {
            setTitleLayout();
            setMessageLayout();
            setContentLayout();
            setButtonLayout();
            //根据是否有Edittext，设置底部分割线的下间距
            updateShowInputView();
        }

        @Override
        public void updateMessageContent() {
            if (StringUtils.isBlank(P.mMessage)) {
                messageLayout.setVisibility(View.GONE);
                return;
            }
            messageView.setText(P.mMessage);
            if (P.messageColor != 0) {
                messageView.setTextColor(mContext.getResources().getColor(P.messageColor));
            } else {
                messageView.setTextColor(mContext.getResources().getColor(R.color.c07_212630));
            }
            messageLayout.setVisibility(View.VISIBLE);
        }
    }
}
