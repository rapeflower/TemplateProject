package com.nino.micro.business.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nino.micro.business.R;
import com.nino.micro.business.utils.StringUtils;
import com.nino.micro.business.utils.VersionAdapterManager;

/**
 * @Date 2017-10-21 14:50
 * 1：圆角边框
 * 2：下面的按钮为一个，且背景为红色，
 * 2.1 设置取消按钮时恢复成2个，右边有红色
 * 3：增加了右上方的close按钮，且close按钮
 * 4：删除了input
 */
public class RedCommonDialog extends Dialog {

    public RedCommonDialog(Context context) {
        super(context);
    }

    public RedCommonDialog(Context context, int theme) {
        super(context, theme);
    }

    protected RedCommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private String bottomRemind;
        private int color;
        private int remindColor;
        private int remindBackgroundColor;
        private int closeVisibility = View.GONE;//关闭按钮，默认是gone
        private int messageGravity = Gravity.CENTER_HORIZONTAL;//message内容显示位置，默认水平居中

        private String mPositiveButtonText;//底部按钮的文字显示
        private String mNegativeButtonText;

        private View contentView;
        private RedCommonDialog dialog;

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private View.OnClickListener onCloseClickListener;

        private LinearLayout topLayout;
        private TextView titleView;
        private LinearLayout messageLayout;
        private TextView messageView;
        private TextView tvBottomRemind;//提示放在内容底部时，可使用该控件

        private ImageView ivClose;

        private MButton okBtn;

        //包含取消和确认两个按钮的layout
        private LinearLayout buttonslayout;
        private Button cancelBtn;
        private Button okBtn_right;
        private View dividerView;

        public Builder(Context context) {
            this.mContext = context;
        }


        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) mContext.getText(title);
            setTitle(this.title);
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) mContext.getText(message);
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
            this.message = message;
            this.color = color;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message      内容
         * @param color        内容文本颜色
         * @param textLocation 内容文本显示位置
         * @return
         */
        public Builder setMessage(String message, int color, int textLocation) {
            this.message = message;
            this.color = color;
            this.messageGravity = textLocation;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param textLocation 内容文本显示位置
         * @param message      内容
         * @return
         */
        public Builder setMessage(int textLocation, String message) {
            this.message = message;
            this.messageGravity = textLocation;
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
            this.message = (String) mContext.getText(message);
            this.color = color;
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @return
         */
        public Builder setBottomRemindMessage(int bottomRemindMessage) {
            this.bottomRemind = (String) mContext.getText(bottomRemindMessage);
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @param textColor           提示信息的字体颜色
         * @param backgroundColor     提示信息的背景颜色
         * @return
         */
        public Builder setBottomRemindMessage(String bottomRemindMessage, int textColor, int backgroundColor) {
            this.bottomRemind = bottomRemindMessage;
            this.remindColor = textColor;
            this.remindBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @param textColor           提示信息的字体颜色
         * @param backgroundColor     提示信息的背景颜色
         * @return
         */
        public Builder setBottomRemindMessage(int bottomRemindMessage, int textColor, int backgroundColor) {
            this.bottomRemind = (String) mContext.getText(bottomRemindMessage);
            this.remindColor = textColor;
            this.remindBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置整个背景
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setCloseVisibility(int visibility) {
            closeVisibility = visibility;
            return this;
        }

        /**
         * 设置按钮和其点击事件
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.mPositiveButtonText = (String) mContext
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
            this.onCloseClickListener = onCloseClickListener;
            return this;
        }


        //设置取消按钮和其事件
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.mNegativeButtonText = (String) mContext
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        private void setTitleLayout() {
            if (StringUtils.isBlank(this.title)) {
                titleView.setVisibility(View.GONE);
                return;
            }
            titleView.setText(this.title);
            titleView.setVisibility(View.VISIBLE);
        }

        private void setMessageLayout() {

            if (StringUtils.isBlank(this.message)) {
                messageLayout.setVisibility(View.GONE);
                return;
            }
            messageLayout.setVisibility(View.VISIBLE);
            messageView.setText(this.message);
            LinearLayout.LayoutParams messageLayoutParams = (LinearLayout.LayoutParams) messageLayout.getLayoutParams();
            if (titleView.getVisibility() == View.GONE) {//如果只显示message的情况下，设置message的上边距
                messageLayoutParams.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
            }
            if (this.messageGravity == Gravity.LEFT) {
                messageView.setGravity(Gravity.LEFT);
            }

            messageLayoutParams.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
            messageLayout.setLayoutParams(messageLayoutParams);

            if (color != 0) {
                messageView.setTextColor(VersionAdapterManager.getColor(mContext, this.color));
            }
        }

        private void setBottomReminderLayout() {
            if (StringUtils.isBlank(this.bottomRemind)) {
                tvBottomRemind.setVisibility(View.GONE);
                return;
            }

            tvBottomRemind.setVisibility(View.VISIBLE);
            tvBottomRemind.setText(bottomRemind);
            if (remindColor != 0) {
                tvBottomRemind.setTextColor(VersionAdapterManager.getColor(mContext, this.remindColor));
            }

            if (remindBackgroundColor != 0) {
                tvBottomRemind.setBackgroundColor(VersionAdapterManager.getColor(mContext, this.remindBackgroundColor));
            }

        }

        private void setContentViewLayout() {
            //如果设置了contentView，而不是设置message
            if (contentView == null) {
                return;
            }
            // if no message set add the contentView to the dialog body
            messageLayout.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
            topLayout.setBackgroundColor(Color.TRANSPARENT);

            int topPadding = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
            contentView.setPadding(0, topPadding, 0, 0);
            // messageLayout.removeAllViews();
            messageLayout.addView(contentView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        private void setButtonCloseLayout() {
            ivClose.setVisibility(closeVisibility);
            if (onCloseClickListener == null) {
                //设置右上角的关闭按钮点击事件
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return;
            }
            ivClose.setOnClickListener(onCloseClickListener);

        }

        public RedCommonDialog create() {
            dialog = new RedCommonDialog(mContext, R.style.CommonDialog);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.dialog_common_red, null);
            initControls(layoutView);

            dialog.addContentView(layoutView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            setTitleLayout();
            setMessageLayout();
            setBottomReminderLayout();
            setContentViewLayout();
            setButtonLayout();
            setButtonCloseLayout();
            dialog.setContentView(layoutView);

            return dialog;
        }


        private void initControls(View rootView) {
            topLayout = (LinearLayout) rootView.findViewById(R.id.ll_top_layout);
            titleView = (TextView) rootView.findViewById(R.id.tv_title);
            messageLayout = (LinearLayout) rootView.findViewById(R.id.ll_content_layout);
            messageView = (TextView) rootView.findViewById(R.id.tv_message);
            tvBottomRemind = (TextView) rootView.findViewById(R.id.bottom_remind);
            ivClose = (ImageView) rootView.findViewById(R.id.iv_redcommon_dialog_close);
            okBtn = (MButton) rootView.findViewById(R.id.btn_ok);
            buttonslayout = (LinearLayout) rootView.findViewById(R.id.ll_button_layout);
            cancelBtn = (Button) rootView.findViewById(R.id.btn_cancel);
            okBtn_right = (Button) rootView.findViewById(R.id.btn_ok_right);
            dividerView = rootView.findViewById(R.id.vw_divider);
        }

        /**
         * 根据设置是不是设置Button，来确定是显示几个button
         */
        private void setButtonLayout() {

            //没有任何两个Button
            if (StringUtils.isBlank(mNegativeButtonText) && StringUtils.isBlank(mPositiveButtonText)) {
                buttonslayout.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                return;
            }

            //取消
            if (StringUtils.isNotBlank(mNegativeButtonText) && StringUtils.isBlank(mPositiveButtonText)) {
                okBtn.setVisibility(View.GONE);
                buttonslayout.setVisibility(View.VISIBLE);

                cancelBtn.setVisibility(View.VISIBLE);
                okBtn_right.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonslayout.setBackgroundColor(Color.TRANSPARENT);
                cancelBtn.setBackgroundResource(R.drawable.bg_common_dialog_bottom);
                //设置监听事件
                if (StringUtils.isNotBlank(mNegativeButtonText)) {
                    cancelBtn.setText(mNegativeButtonText);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (negativeButtonClickListener == null) {
                                dialog.dismiss();
                                return;
                            }
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                    cancelBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
            //Ok
            if (StringUtils.isNotBlank(mPositiveButtonText) && StringUtils.isBlank(mNegativeButtonText)) {

                okBtn.setVisibility(View.VISIBLE);
                buttonslayout.setVisibility(View.GONE);

                if (StringUtils.isNotBlank(mPositiveButtonText)) {
                    okBtn.setText(mPositiveButtonText);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (positiveButtonClickListener == null) {
                                dialog.dismiss();
                                return;
                            }
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                    okBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
            //含有两个button
            okBtn.setVisibility(View.GONE);
            buttonslayout.setVisibility(View.VISIBLE);
            buttonslayout.setBackgroundColor(Color.TRANSPARENT);

            //设置监听事件
            if (StringUtils.isNotBlank(mNegativeButtonText)) {
                cancelBtn.setText(mNegativeButtonText);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (negativeButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
                cancelBtn.setVisibility(View.VISIBLE);
            }

            if (StringUtils.isNotBlank(mPositiveButtonText)) {
                okBtn_right.setText(mPositiveButtonText);
                okBtn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (positiveButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
                okBtn_right.setVisibility(View.VISIBLE);
            }
        }
    }

}
