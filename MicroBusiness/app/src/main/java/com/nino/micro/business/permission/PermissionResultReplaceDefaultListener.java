package com.nino.micro.business.permission;

/**
 * @Date 2017-10-20 17:28
 * <p>
 * 替换掉所有默认的权限处理结果
 */
public interface PermissionResultReplaceDefaultListener {

    /***
     *  权限通过
     * @param requestCode
     */
    void onPermissionGranted(int requestCode);

    /***
     * 权限被拒
     * @param requestCode
     * @param deniedPermissions 被拒的权限
     */
    void onPermissionDenied(int requestCode, String[] deniedPermissions);

    /***
     * 当拒绝的时候不需要进行校验
     * @param requestCode
     */
    void onPermissionShouldShowRequestFalse(int requestCode);


}
