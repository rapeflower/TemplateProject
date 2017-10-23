package com.nino.micro.business.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


import com.nino.micro.business.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2017-10-20 16:50
 * <p>
 * 参考地址为:http://www.jianshu.com/p/a51593817825
 * http://www.jianshu.com/p/b4a8b3d4f587
 * <p>
 * public void onRequestPermissionsResult为系统弹出框里面的允许与拒绝回调的方法
 */
public class CheckPermissionManager {
    /***
     * 对应REQUEST_PERMISSION的权限名称
     */
    //public static final int CODE_GET_ACCOUNTS = 0;
    public static final int CODE_READ_ACCOUNTS = 0;
    public static final int CODE_CALL_PHONE = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CAMERA = 3;
    public static final int CODE_ACCESS_FINE_LOCATION = 4;
    // public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_RECORD_AUDIO = 5;
    //public static final int CODE_READ_EXTERNAL_STORAGE = 8;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 6;
    // public static final int CODE_RECEIVE_SMS = 10;
    public static final int CODE_READ_SMS = 7;

    public static final int CODE_MULTI_PERMISSION = 100;

    /***
     * 6.0权限的基本知识，以下是需要单独申请的权限，共分为9组，每组只要有一个权限申请成功了，就默认整组权限都可以使用了。
     */
    //group.CONTACTS
    private static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_ACCOUNTS = Manifest.permission.READ_CONTACTS;
    //group.PHONE
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    //group.CAMERA
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //group.LOCATION
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //group.MICROPHONE
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    //group.STORAGE
    private static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //group.SMS
    private static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;


    private static final String[] REQUEST_PERMISSION = {
            //PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_ACCOUNTS,
            PERMISSION_CALL_PHONE,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            // PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_RECORD_AUDIO,
            //PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            //  PERMISSION_RECEIVE_SMS,
            PERMISSION_READ_SMS
    };

    /***
     *
     * @param activity
     * @param requestCode
     * @param permissionResult if you need request CAMERA permission,parameters is PermissionManager.CODE_CAMERA
     */

    protected static void requestPermission(Activity activity, int requestCode, PermissionResultDefaultListener permissionResult) {

        if (isLowMVersionRequestPermission(activity)) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }

        if ((requestCode < 0 || requestCode >= REQUEST_PERMISSION.length) || requestCode == CODE_MULTI_PERMISSION) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }

        String permission = REQUEST_PERMISSION[requestCode];

        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, permission);
        } catch (Exception e) {
            return;
        }

        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }
        // 如果第二次弹出权限申请的对话框，会出现“以后不再弹出”的提示框，如果用户勾选了，
        // 你再申请权限，则shouldShowRequestPermissionRationale返回true，意思是说要给用户一个 解释，告诉用户为什么要这个权限。
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            shouldShowPermissionRationale(activity, requestCode, permission);
            return;
        }
        requestPermissions(activity, new String[]{permission}, requestCode);
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
    protected static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                   @NonNull int[] grantResults, PermissionResultDefaultListener permissionResult) {

        if (isLowMVersionRequestPermission(activity)) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }
        if ((requestCode <= 0 || requestCode >= REQUEST_PERMISSION.length) && requestCode != CODE_MULTI_PERMISSION) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }
        //权限组内只要有一个被授权，其他的权限也就有了权限，所以使用grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(permissionResult, requestCode);
            return;
        }

        String permission = REQUEST_PERMISSION[requestCode];

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            onConfirmDeniedAgain(activity, requestCode);
        }

    }


    /***
     * 一次申请多个权限
     * @param activity
     * @param permissionCodes
     * @param permissionResult
     */

    protected static void requestMultiPermissions(Activity activity, int[] permissionCodes, PermissionResultDefaultListener permissionResult) {
        requestMultiPermissions(activity, permissionCodes, permissionResult, null);
    }

    /***
     * 一次申请多个权限
     * @param activity
     * @param permissionCodes
     * @param permissionResult
     */

    protected static void requestMultiPermissions(Activity activity, int[] permissionCodes, PermissionResultDefaultListener permissionResult
            , PermissionResultReplaceDefaultListener replaceDefaultListener) {

        if (isLowMVersionRequestPermission(activity)) {
            onPermissionGranted(permissionResult, replaceDefaultListener, CODE_MULTI_PERMISSION);
            return;
        }

        if (permissionCodes == null || permissionCodes.length == 0) {
            onPermissionGranted(permissionResult, replaceDefaultListener, CODE_MULTI_PERMISSION);
            return;
        }

        List<String> shouldPermissions = new ArrayList<>();
        List<String> checkPermissions = new ArrayList<>();
        List<Integer> shouldCodes = new ArrayList<>();
        for (int i = 0; i < permissionCodes.length; i++) {
            int code = permissionCodes[i];
            if (code <= 0 || code >= REQUEST_PERMISSION.length) {
                continue;

            }
            String permission = REQUEST_PERMISSION[permissionCodes[i]];
            int checkSelfPermission = ActivityCompat.checkSelfPermission(activity, permission);
            if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                shouldPermissions.add(permission);
                shouldCodes.add(code);
                continue;
            }
            checkPermissions.add(permission);
        }

        if (shouldPermissions.size() == 0 && checkPermissions.size() == 0) {
            onPermissionGranted(permissionResult, replaceDefaultListener, CODE_MULTI_PERMISSION);
            return;
        }
        //如果所有的权限都被校验过一次，则直接提示
        if (checkPermissions.size() == 0 && shouldPermissions.size() != 0) {
            onPermissionShouldShowRequestFalse(activity, shouldCodes.toArray(new Integer[shouldCodes.size()]), CODE_MULTI_PERMISSION, replaceDefaultListener);
            return;
        }
        onPermissionDenied(activity, shouldPermissions, checkPermissions, replaceDefaultListener);

    }


    private static void onPermissionShouldShowRequestFalse(Activity activity, Integer[] permissions
            , int requestCode, PermissionResultReplaceDefaultListener replaceListener) {
        if (replaceListener == null) {
            onConfirmDeniedAgain(activity, permissions);
            return;
        }
        replaceListener.onPermissionShouldShowRequestFalse(requestCode);

    }

    private static void onPermissionShouldShowRequestFalse(Activity activity, List<String> permissions
            , int requestCode, PermissionResultReplaceDefaultListener replaceListener) {
        if (replaceListener == null) {
            onConfirmDeniedAgain(activity, permissions);
            return;
        }
        replaceListener.onPermissionShouldShowRequestFalse(requestCode);

    }


    /**
     * 主动检查权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    protected static void requestPermissionsDirectly(@NonNull Activity activity,
                                                     @NonNull String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }


    /***
     * 处理多个申请权限的
     * @param activity
     * @param permissions
     * @param grantResults
     * @param permissionResult
     */
    protected static void requestMultiPermissionsResult(final Activity activity, @NonNull String[] permissions,
                                                        @NonNull int[] grantResults, PermissionResultDefaultListener permissionResult) {
        requestMultiPermissionsResult(activity, permissions, grantResults, permissionResult, null);
    }

    /***
     * 处理多个申请权限的
     * @param activity
     * @param permissions
     * @param grantResults
     * @param permissionResult
     */
    protected static void requestMultiPermissionsResult(final Activity activity, @NonNull String[] permissions,
                                                        @NonNull int[] grantResults, PermissionResultDefaultListener permissionResult
            , PermissionResultReplaceDefaultListener replaceListener) {
        if (isLowMVersionRequestPermission(activity)) {
            onPermissionGranted(permissionResult, replaceListener, CODE_MULTI_PERMISSION);
            return;
        }
        List<String> checkPermissions = new ArrayList<>();
        List<String> shouldPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                shouldPermissions.add(permission);
                continue;
            }
            checkPermissions.add(permission);
        }

        if (shouldPermissions.size() == 0 && checkPermissions.size() == 0) {
            onPermissionGranted(permissionResult, replaceListener, CODE_MULTI_PERMISSION);
            return;
        }

        //如果所有的权限都被校验过一次，则直接提示
        if (checkPermissions.size() == 0 && shouldPermissions.size() != 0) {
            onPermissionShouldShowRequestFalse(activity, shouldPermissions, CODE_MULTI_PERMISSION, replaceListener);
        }
    }


    /***
     * 1、请求权限后，系统会弹出请求权限的Dialog,用户选择允许或拒绝后，会回调onRequestPermissionsResult方法, 该方法类似于onActivityResult
     * 2、在Fragment中申请权限，不要使用ActivityCompat.requestPermissions,
     * 直接使用Fragment的requestPermissions方法，否则会回调到Activity的onRequestPermissionsResult
     *3、如果在Fragment中嵌套Fragment，在子Fragment中使用requestPermissions方法，onRequestPermissionsResult不会回调回来，
     *建议使用getParentFragment().requestPermissions方法，
     *这个方法会回调到父Fragment中的onRequestPermissionsResult，加入以下代码可以把回调透传到子Fragment
     * @param activity
     * @param permissions
     * @param requestCode
     */
    private static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /***
     * 权限被允许
     * @param permissionResult
     * @param requestCode
     */
    private static void onPermissionGranted(PermissionResultDefaultListener permissionResult, int requestCode) {
        if (permissionResult == null) {
            return;
        }
        permissionResult.onPermissionGranted(requestCode);
    }


    /***
     * 权限被允许
     * @param permissionResult
     * @param replaceListener
     * @param requestCode
     */
    private static void onPermissionGranted(PermissionResultDefaultListener permissionResult, PermissionResultReplaceDefaultListener replaceListener, int requestCode) {
        if (permissionResult != null)
            permissionResult.onPermissionGranted(requestCode);

        if (replaceListener != null)
            replaceListener.onPermissionGranted(requestCode);
    }

    /***
     * 权限需要被询问
     * @param permissionResult
     * @param requestCode
     */
    private static void onPermissionShouldShowRequestFalse(Activity activity, Integer[] permissions, PermissionResultReplaceDefaultListener permissionResult, int requestCode) {
        if (permissionResult == null) {
            onConfirmDeniedAgain(activity, permissions);
            return;
        }
        permissionResult.onPermissionShouldShowRequestFalse(requestCode);
    }

    /***
     * 权限被拒绝
     * @param permissionResult
     * @param requestCode
     */
    private static void onPermissionDenied(PermissionResultReplaceDefaultListener permissionResult, int requestCode, String[] deniedPermissions) {
        if (permissionResult == null) {
            return;
        }
        permissionResult.onPermissionDenied(requestCode, deniedPermissions);
    }

    /**
     * 权限被拒绝
     *
     * @param activity
     * @param shouldPermissions
     * @param checkPermissions
     * @param replaceListener
     */
    private static void onPermissionDenied(Activity activity, List<String> shouldPermissions, List<String> checkPermissions
            , PermissionResultReplaceDefaultListener replaceListener) {
        //否则直接把所有的权限作为没有被校验过的权限进行处理
        List<String> last = getLastRequestPermissions(shouldPermissions, checkPermissions);
        if (replaceListener == null) {
            ActivityCompat.requestPermissions(activity, last.toArray(new String[last.size()]), CODE_MULTI_PERMISSION);
            return;
        }
        replaceListener.onPermissionDenied(CODE_MULTI_PERMISSION, last.toArray(new String[last.size()]));
    }

    /***
     * 根据requestCode获取对应的权限提示
     * @param activity
     * @param requestCode
     * @return
     */
    private static String getPermissionCommonInfo(Activity activity, int requestCode) {
        String[] infos = activity.getResources().getStringArray(R.array.permission);
        if (requestCode < 0 || requestCode >= infos.length) {
            return "";
        }
        return infos[requestCode];
    }

    /***
     * 根据requestCode获取对应的权限提示
     * @param activity
     * @param permissions
     * @return
     */
    private static String getPermissionCommonInfo(Activity activity, List<String> permissions) {
        String[] infos = activity.getResources().getStringArray(R.array.permission);
        StringBuffer buffer = new StringBuffer();
        if (permissions == null || permissions.size() == 0)
            return "";

        for (String permission : permissions) {
            for (int i = 0; i < REQUEST_PERMISSION.length; i++) {
                if (permission.equals(REQUEST_PERMISSION[i])) {
                    buffer.append(infos[i]);
                    buffer.append(",");
                    break;
                }

            }

        }
        String result = buffer.toString();
        if (result.length() > 0 && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /***
     * 根据requestCode获取对应的权限提示
     * @param activity
     * @param permissionCodes
     * @return
     */
    private static String getPermissionCommonInfo(Activity activity, Integer[] permissionCodes) {
        String[] infos = activity.getResources().getStringArray(R.array.permission);
        StringBuffer buffer = new StringBuffer();
        if (permissionCodes == null || permissionCodes.length == 0)
            return "";

        for (int i = 0; i < permissionCodes.length; i++) {
            buffer.append(infos[permissionCodes[i]]);
            if (i < permissionCodes.length - 1) buffer.append(",");
        }
        return buffer.toString();
    }

    /***
     * 再一次提示用户开启权限
     * @param activity
     * @param requestCode
     */
    private static void onConfirmDeniedAgain(Activity activity, int requestCode) {
        String common = getPermissionCommonInfo(activity, requestCode);
        String message = String.format(activity.getString(R.string.perm_message), common);
        String title = String.format(activity.getString(R.string.perm_title), common);
        PermissionManager.openSettingActivityDialog(activity, title, message);
    }

    /**
     * 再一次提示用户开启权限
     *
     * @param activity
     * @param permissions
     */
    private static void onConfirmDeniedAgain(Activity activity, List<String> permissions) {
        String common = getPermissionCommonInfo(activity, permissions);
        String message = String.format(activity.getString(R.string.perm_message), common);
        String title = String.format(activity.getString(R.string.perm_title), common);
        PermissionManager.openSettingActivityDialog(activity, title, message);
    }

    /***
     * 再一次提示用户开启权限
     * @param activity
     * @param shouldCodes
     */
    private static void onConfirmDeniedAgain(Activity activity, Integer[] shouldCodes) {
        String common = getPermissionCommonInfo(activity, shouldCodes);
        String message = String.format(activity.getString(R.string.perm_message), common);
        String title = String.format(activity.getString(R.string.perm_title), common);
        PermissionManager.openSettingActivityDialog(activity, title, message);
    }

    /***
     * 不执行requestPermission方法
     * @param activity
     * @return
     */
    private static boolean isLowMVersionRequestPermission(Activity activity) {
        if (activity == null) {
            return true;
        }

        //低版本不去进行校验
        if (PermissionManager.isLowMVersion()) {
            return true;
        }
        return false;
    }

    /***
     * TODO http://blog.csdn.net/hudashi/article/details/50775180
     * Android原生系统中，如果第二次弹出权限申请的对话框，会出现“以后不再弹出”的提示框，如果用户勾选了，你再申请权限，则shouldShowRequestPermissionRationale返回true，意思是说要给用户一个 解释，告诉用户为什么要这个权限。
     * 然而，在实际开发中，需要注意的是，很多手机对原生系统做了修改，比如小米，小米4的6.0的shouldShowRequestPermissionRationale 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了。。。。
     * 所以说这个地方有坑，我的解决方法是，在回调里面处理，如果用户拒绝了这个权限，则打开本应用信息界面，由用户自己手动开启这个权限
     * （第二种方案：在onRequestPermissionsResult函数中进行检测，如果返回PERMISSION_DENIED，则去调用shouldShowRequestPermissionRationale函数，如果返回false代表用户已经禁止该权限（上面的3和4两种情况），弹出dialog告诉用户你需要该权限的理由，让用户手动打开）
     * @param activity
     * @param requestCode
     * @param permission
     */
    private static void shouldShowPermissionRationale(final Activity activity, final int requestCode, final String permission) {
        onConfirmDeniedAgain(activity, requestCode);
    }

    /***
     * 将校验过的权限和没有校验过的权限一起进行重新校验
     * @param shouldPermissions
     * @param checkPermissions
     * @return
     */
    private static List<String> getLastRequestPermissions(List<String> shouldPermissions, List<String> checkPermissions) {
        List<String> last = new ArrayList<>();
        if (shouldPermissions != null) last.addAll(shouldPermissions);
        if (checkPermissions != null) last.addAll(checkPermissions);
        return last;
    }
}
