package com.nino.micro.business.permission;

/**
 * @Date 2017-10-20 17:28
 * <p>
 * 在请求权限的时，返回对应的处理结果,Denied和ShouldShowRequestFalse采用默认结果
 */
public interface PermissionResultDefaultListener {
    /***
     /***
     *  权限通过
     * @param requestCode
     */
    void onPermissionGranted(int requestCode);

//    /***
//     * 权限被拒
//     * @param requestCode
//     * @param deniedPermissions 被拒的权限
//     */
//    void onPermissionDenied(int requestCode, String[] deniedPermissions);
//

//    /***
//     * 当拒绝的时候不需要进行校验
//     * @param requestCode
//     */
//    void onPermissionShouldShowRequestFalse(int requestCode);


}
