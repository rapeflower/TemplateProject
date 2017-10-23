package com.nino.micro.business.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.nino.micro.business.R;
import com.nino.micro.business.utils.Constants;
import com.nino.micro.business.utils.StringUtils;
import com.nino.micro.business.utils.VersionAdapterManager;
import com.nino.micro.business.view.CommonDialog;
import com.nino.micro.business.view.RedCommonDialog;
import com.photo.selector.activity.PhotoSelectorActivity;
import com.photo.selector.utils.PhotoSelector;

import java.io.File;

/**
 * @Date 2017-10-20 16:52
 * <p>
 * 供app调用
 */
public class PermissionManager {
    /**
     * 联系人
     *
     * @param activity
     * @param listener
     */
    public static void checkAccounts(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_READ_ACCOUNTS, listener);
    }

    /**
     * 拨打电话
     *
     * @param activity
     * @param phone
     */
    public static void checkCallPhone(final Activity activity, final String phone) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_CALL_PHONE, new PermissionResultDefaultListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
                callPhoneDialog(activity, phone);
            }

        });
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void checkCallPhone(Context context, final String phone) {
        Activity activity = getActivityByContext(context);
        if (activity == null)
            return;
        checkCallPhone(activity, phone);
    }

    public static void checkReadPhoneState(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_READ_PHONE_STATE, listener);
    }

    /***
     * 跳转到扫描页
     * @param activity
     * @param scanKey
     */
    public static void checkCamera(final Activity activity, final int scanKey) {
        checkCamera(activity, new PermissionResultDefaultListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
//                Intent intent = new Intent(activity, ScanActivity.class);
//                intent.putExtra(ScanActivity.KEY_SIGN, scanKey);
//                activity.startActivity(intent);
            }
        });

    }

    /***
     * 跳转到扫描页
     * @param context
     * @param scanKey
     */
    public static void checkCamera(Context context, int scanKey) {
        Activity activity = getActivityByContext(context);
        if (activity == null)
            return;
        checkCamera(activity, scanKey);

    }

    /***
     * 主动调用系统的图片选择的权限
     * @param context
     * @param listener
     */
    public static void checkPickPicture(Context context, PermissionResultDefaultListener listener) {
        Activity activity = getActivityByContext(context);
        if (activity == null)
            return;
        CheckPermissionManager.requestMultiPermissions(activity, new int[]{CheckPermissionManager.CODE_CAMERA,
                CheckPermissionManager.CODE_WRITE_EXTERNAL_STORAGE}, listener);
    }

    /***
     * 监听选取图片
     * @param context
     * @param selectPic
     * @param maxSize
     * @param activityRequestCode
     */
    public static void checkPickPicture(final Context context, final int selectPic, final int maxSize, final int activityRequestCode) {
        final Activity activity = getActivityByContext(context);
        if (activity == null) {
            return;
        }
        checkWriteStorage(activity, new PermissionResultDefaultListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
                PhotoSelector.launchActivityForResult(activity, PhotoSelectorActivity.class, 0);
            }
        });

    }

    /***
     * 拍照
     * @param activity
     * @param activityRequestCode
     */
    public static void checkTakePicture(final Activity activity, final String path, final int activityRequestCode) {
        int[] permissionCodes = new int[]{
                CheckPermissionManager.CODE_WRITE_EXTERNAL_STORAGE, CheckPermissionManager.CODE_CAMERA
        };
        CheckPermissionManager.requestMultiPermissions(activity, permissionCodes, new PermissionResultDefaultListener() {
            @Override
            public void onPermissionGranted(int requestCode) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(activity, "您还没有安装SD卡无法进行图片上传功能，请确保已经插入SD卡", Toast.LENGTH_LONG).show();
                    return;
                }

                File folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/"
                        + Constants.APP_PICTURE_FOLDER);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, VersionAdapterManager.getUri(activity, path));
                activity.startActivityForResult(intent, activityRequestCode);

            }
        });


    }

    /**
     * 检查相机
     *
     * @param activity
     * @param listener
     */
    public static void checkCamera(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_CAMERA, listener);
    }

    /***
     * App启动时的权限检验
     * @param activity
     */
    public static void checkLoadApp(Activity activity, PermissionResultReplaceDefaultListener checkPermissionDeniedListener) {
        int[] permissionCodes = new int[]{
                CheckPermissionManager.CODE_WRITE_EXTERNAL_STORAGE, CheckPermissionManager.CODE_READ_PHONE_STATE, CheckPermissionManager.CODE_ACCESS_FINE_LOCATION
        };
        CheckPermissionManager.requestMultiPermissions(activity, permissionCodes, null
                , checkPermissionDeniedListener);
    }

    /**
     * 主动检查权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissionsDirectly(@NonNull Activity activity,
                                                  @NonNull String[] permissions, int requestCode) {
        CheckPermissionManager.requestPermissionsDirectly(activity, permissions, requestCode);
    }

    public static void checkLocation(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_ACCESS_FINE_LOCATION, listener);
    }

    /**
     * 麦克风
     *
     * @param activity
     * @param listener
     */
    public static void checkRecordAudio(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_RECORD_AUDIO, listener);
    }

    /***
     * 存储空间
     * @param activity
     * @param listener
     */
    public static void checkWriteStorage(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_WRITE_EXTERNAL_STORAGE, listener);
    }

    public static void checkReadMsm(Activity activity, PermissionResultDefaultListener listener) {
        CheckPermissionManager.requestPermission(activity, CheckPermissionManager.CODE_READ_SMS, listener);
    }


    /***
     * 处理结果
     * TODO http://blog.csdn.net/hudashi/article/details/50775180
     * Android原生系统中，如果第二次弹出权限申请的对话框，会出现“以后不再弹出”的提示框，如果用户勾选了，你再申请权限，则shouldShowRequestPermissionRationale返回true，意思是说要给用户一个 解释，告诉用户为什么要这个权限。
     * 然而，在实际开发中，需要注意的是，很多手机对原生系统做了修改，比如小米，小米4的6.0的shouldShowRequestPermissionRationale 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了。。。。
     * 所以说这个地方有坑，我的解决方法是，在回调里面处理，如果用户拒绝了这个权限，则打开本应用信息界面，由用户自己手动开启这个权限
     * （解决方案如下：在onRequestPermissionsResult(系统弹出框里面的允许与拒绝回调的方法)函数中进行检测，如果返回PERMISSION_DENIED，则去调用shouldShowRequestPermissionRationale函数，如果返回false代表用户已经禁止该权限（上面的3和4两种情况），弹出dialog告诉用户你需要该权限的理由，让用户手动打开）
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param permissionResult
     */
    public static void onRequestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, PermissionResultDefaultListener permissionResult) {
        onRequestPermissionsResult(activity, requestCode, permissions, grantResults, permissionResult, null);
    }

    /***
     * 处理结果
     * TODO http://blog.csdn.net/hudashi/article/details/50775180
     * Android原生系统中，如果第二次弹出权限申请的对话框，会出现“以后不再弹出”的提示框，如果用户勾选了，你再申请权限，则shouldShowRequestPermissionRationale返回true，意思是说要给用户一个 解释，告诉用户为什么要这个权限。
     * 然而，在实际开发中，需要注意的是，很多手机对原生系统做了修改，比如小米，小米4的6.0的shouldShowRequestPermissionRationale 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了。。。。
     * 所以说这个地方有坑，我的解决方法是，在回调里面处理，如果用户拒绝了这个权限，则打开本应用信息界面，由用户自己手动开启这个权限
     * （解决方案如下：在onRequestPermissionsResult(系统弹出框里面的允许与拒绝回调的方法)函数中进行检测，如果返回PERMISSION_DENIED，则去调用shouldShowRequestPermissionRationale函数，如果返回false代表用户已经禁止该权限（上面的3和4两种情况），弹出dialog告诉用户你需要该权限的理由，让用户手动打开）
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param permissionResult
     */
    public static void onRequestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, PermissionResultDefaultListener permissionResult
            , PermissionResultReplaceDefaultListener deniedListener) {
        if (requestCode == CheckPermissionManager.CODE_MULTI_PERMISSION) {
            CheckPermissionManager.requestMultiPermissionsResult(activity, permissions, grantResults, permissionResult, deniedListener);
            return;
        }
        CheckPermissionManager.requestPermissionsResult(activity, requestCode, permissions, grantResults, permissionResult);
    }


    /***
     * 是否低于M 版本
     * @return
     */
    public static boolean isLowMVersion() {
        //低版本不去进行校验
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    /***
     * 显示一个dialog
     * @param activity
     * @param title
     * @param message
     * @param clickListener
     */
    public static void showMessageDialog(Activity activity, String title, String message, DialogInterface.OnClickListener clickListener) {
        RedCommonDialog.Builder builder = new RedCommonDialog.Builder(activity);
        if (StringUtils.isNotBlank(title)) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.perm_open, clickListener);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    /***
     * 打开设置界面
     * @param activity
     * @param title
     * @param message
     */
    public static void openSettingActivityDialog(final Activity activity, String title, String message) {
        //提示用户打开setting界面
        showMessageDialog(activity, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);


                dialog.dismiss();
            }
        });
    }

    /**
     * 通过context获取Activity实例
     *
     * @param context
     * @return
     */
    private static Activity getActivityByContext(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        return activity;
    }

    /***
     * 显示拨打电话的对话框
     * @param context
     * @param phoneRes
     */
    private static void callPhoneDialog(final Context context, final String phoneRes) {
        CommonDialog.Builder callUpBuilder = new CommonDialog.Builder(context);
        callUpBuilder.setMessage(phoneRes).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                StartActivityUtils.callPhoneActivity(context, phoneRes);
                dialog.dismiss();
            }
        }).create().show();
    }


}
