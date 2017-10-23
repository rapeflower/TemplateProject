package com.nino.micro.business.utils;


import java.util.ArrayList;
import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-10-20 17:07
 * @Describe 全局的静态不可改变变量
 */
public class Constants {

    public static final String URL_SCHEME_FILE_PREFIX = "file:";
    public static final String URL_SCHEME_HTTP = "http://";
    public static final String URL_SCHEME_HTTPS = "https://";

    // ### params ### App的基本配置 ### params ###
    /**
     * 图片上传服务器
     */
    public static final String UPLOAD_RESOURCE_SERVER_HOST = "";
    public static final String UPLOAD_RESOURCE_SERVER_URL = UPLOAD_RESOURCE_SERVER_HOST + "/upload";

    /**
     * app的最大大小
     */
    public static final int APP_UPDATE_SIZE = 100000;
    /**
     * 用来保存拍照图片的目录
     */
    public static final String APP_PICTURE_FOLDER = "Xc/Picture/";
    /**
     * 日志目录
     */
    public static String PATIENT_LOG_FILE_PATH = "Xc/Log";
    /**
     * 用来保存压缩图片缓存图片的目录
     */
    public static final String APP_PHOTO_CACHE_FOLDER = "Xc/Photo_Cache/";
    /**
     * 用来保存下载图片缓存图片的目录
     */
    public static final String APP_DOWNLOAD_PHOTO_CACHE_FOLDER = "Xc/Download_Photo_Cache/";
    /**
     * 用来保存截屏图片的目录
     */
    public static final String APP_CURRENT_IMAGE = "Xc/Current_Image/";


    // ### params ###  广播 ### params ###
    /**
     * 用户登录成功广播
     */
    public static final String BROADCAST_ACTION_USER_LOGIN = "action.user.login";
    /**
     * 用户退出登录广播
     */
    public static final String BROADCAST_ACTION_USER_LOGOUT = "action.user.logout";
    /**
     * 用户信息更新成功广播
     */
    public static final String BROADCAST_ACTION_USER_UPDATE = "action.user.update";
    /**
     * IP地址shared preference 文件名（测试用）
     */
    public static final String SP_FILE_NAME_HOST = "host_profile";


    /******************* 常量Key **********************/

}
