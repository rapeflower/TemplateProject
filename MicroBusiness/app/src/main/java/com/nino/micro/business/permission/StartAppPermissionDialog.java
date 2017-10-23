package com.nino.micro.business.permission;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nino.micro.business.R;
import com.nino.micro.business.view.MButton;


/**
 * @Date 2017-10-20 17:30
 * <p>
 * app启动的第一次校验的权限
 */
public class StartAppPermissionDialog extends Dialog {

    private Context context;
    private View.OnClickListener confirmClickLister;
    private String[] permissions;
    private MButton btnConfirm;

    public StartAppPermissionDialog(Context context) {
        this(context, R.style.CommonDialog, null);
    }

    public StartAppPermissionDialog(Context context, String[] pers) {
        this(context, R.style.CommonDialog, pers);

    }

    public StartAppPermissionDialog(Context context, int theme, String[] pers) {
        super(context, theme);
        this.permissions = pers;
        initLayout(context, theme);
    }


    private void initLayout(Context context, int theme) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_start_app_permission, null);
        View vwTelephone = rootView.findViewById(R.id.vw_telephone);
        View vwStorage = rootView.findViewById(R.id.vw_storage);
        View vwLocation = rootView.findViewById(R.id.vw_location);
        btnConfirm = (MButton) rootView.findViewById(R.id.btn_confirm);
        ImageView ivStorage = (ImageView) vwStorage.findViewById(R.id.iv_item_icon);
        TextView tvStorageTitle = (TextView) vwStorage.findViewById(R.id.tv_item_title);
        TextView tvStorageSubTitle = (TextView) vwStorage.findViewById(R.id.tv_item_sub_title);
        ivStorage.setImageResource(R.drawable.permission_sdcard);
        tvStorageTitle.setText(R.string.perm_start_app_storage_title);
        tvStorageSubTitle.setText(R.string.perm_start_app_storage_subtitle);

        ImageView ivLocation = (ImageView) vwLocation.findViewById(R.id.iv_item_icon);
        TextView tvLocationTitle = (TextView) vwLocation.findViewById(R.id.tv_item_title);
        TextView tvLocationSubTitle = (TextView) vwLocation.findViewById(R.id.tv_item_sub_title);
        ivLocation.setImageResource(R.drawable.permission_location);
        tvLocationTitle.setText(R.string.perm_start_app_location_title);
        tvLocationSubTitle.setText(R.string.perm_start_app_location_subtitle);

        vwTelephone.setVisibility(isTelephoneVisibility() ? View.VISIBLE : View.GONE);
        vwStorage.setVisibility(isStorageVisibility() ? View.VISIBLE : View.GONE);
        vwLocation.setVisibility(isLocationVisibility() ? View.VISIBLE : View.GONE);

        setContentView(rootView);
    }

    public void setConfirmClickListener(View.OnClickListener listener) {
        confirmClickLister = listener;
        btnConfirm.setOnClickListener(confirmClickLister);
    }

    private boolean isLocationVisibility() {
        if (permissions == null || permissions.length == 0)
            return false;
        for (String per : permissions) {
            if (per.equals(CheckPermissionManager.PERMISSION_ACCESS_FINE_LOCATION)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStorageVisibility() {
        if (permissions == null || permissions.length == 0)
            return false;
        for (String per : permissions) {
            if (per.equals(CheckPermissionManager.PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTelephoneVisibility() {
        if (permissions == null || permissions.length == 0)
            return false;
        for (String per : permissions) {
            if (per.equals(CheckPermissionManager.PERMISSION_READ_PHONE_STATE)) {
                return true;
            }
        }
        return false;
    }

}
