package com.nino.micro.business.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.nino.micro.business.application.MicroApplication;
import com.nino.micro.business.utils.logUtils.LogUtils;
import com.nino.micro.business.utils.security.AESHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonUtils {

    protected static String uuid;
    private static String channelCode = "";
    private static String deviceUUID = ""; // 之所以使用静态变量，是保证该变量保存的时间长点
    private static String appPackageName = "";
    private static int appVersionCode = 0;
    private static String appVersionName = "";
    private static String deviceMode = "";
    private static String deviceBrand = "";
    private static String deviceInfo = "";
    private static String deviceSystemVersion = "";
    private static String deviceMac = "";
    private static long lastClickTime;
    private static CommonUtils utils = null;

    private static final int REVERSE_LENGTH = 56;
    private final String UUID_PATH = ".vt"; //用来保存uuid
    private final String UUID_FILE = "id.txt";
    private int debugIpCount;
    public static List<String> CONNECT_TYPE = new ArrayList<String>();

    private CommonUtils() {

    }

    public static CommonUtils getInstance() {
        if (utils == null) {
            utils = new CommonUtils();
        }
        return utils;
    }

    /**
     * 当前是否连接WIFI
     */
    public static boolean isWifiConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) MicroApplication.getInstance().
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo wifiNetworkInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 当前是否有有效可用网络
     *
     * @param context
     * @return: true表示有可用网络；false表示目前无可用网络
     */
    public static boolean isNetworkConnected(Context context) {
        CONNECT_TYPE.clear();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // TODO 网络不可用
        } else {// 获取所有网络连接信息
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {// 逐一查找状态为已连接的网络
                for (int i = 0; i < info.length; i++) {
                    LogUtils.e("info[i].getTypeName()" + info[i].getTypeName() + "--" + info[i].getState());
                    LogUtils.e("info[i].getType " + info[i].getType() + "--" + info[i].getState());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        CONNECT_TYPE.add(info[i].getTypeName().toUpperCase());
                    }
                    if (!CONNECT_TYPE.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 获取Manifest文件中的 meta value
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = "unknown";
        if (context == null || metaKey == null) {
            return "unknown";
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null == ai) {
                return metaValue;
            }

            metaData = ai.metaData;
            if (metaData == null) {
                return metaValue;
            }
            Object meta = metaData.get(metaKey);
            metaValue = meta.toString();

        } catch (NameNotFoundException e) {

        } catch (Exception e) {

        }
        return metaValue;
    }

    // 获取Manifest文件中的 meta value
    public static boolean getMetaValues(Context context, String metaKey) {
        Bundle metaData = null;
        boolean metaValue = true;
        if (context == null || metaKey == null) {
            return true;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);

            if (null == ai) {
                return metaValue;
            }

            metaData = ai.metaData;
            if (metaData == null) {
                return metaValue;
            }
            metaValue = metaData.getBoolean(metaKey);

        } catch (NameNotFoundException e) {

        }
        return metaValue;
    }

    /**
     * 控件是否可以重复点击
     */
    public static boolean isRepeatClick() {
        if (System.currentTimeMillis() - lastClickTime <= 1000) {
            LogUtils.e("你点击太快了");
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    /**
     * 获取ChannelCode
     */
    public String getAppChannelCode() {
        if (StringUtils.isBlank(channelCode)) {
            channelCode = getMetaValue(MicroApplication.getInstance().appContext, "BaiduMobAd_CHANNEL");
        }
        if (StringUtils.isBlank(channelCode)) {
            channelCode = "unknown_code";
        }
        return channelCode;
    }

    public static boolean isDebugMode() {
        boolean isDebug = false;
        isDebug = getMetaValues(MicroApplication.getInstance().appContext, "DEBUG_MODE");
        return isDebug;
    }

    /**
     * 是否设置了IP地址
     */
    public static boolean isSetIpMode() {
        boolean isDebug = false;
        isDebug = SharedPreferencesUtils.getInstance
                (MicroApplication.getInstance().getApplicationContext(), Constants.SP_FILE_NAME_HOST).getBoolean("is_setting", false);
        // LogUtils.d("isDebug = " + isDebug);
        return isDebug;
    }

    /**
     * 获取包名
     */
    public String getAppPackageName() {
        if (StringUtils.isBlank(appPackageName)) {
            appPackageName = MicroApplication.getInstance().appContext.getPackageName();
        }
        //如果取不到包名，直接赋值,注意不同的应用，该包名要区别一下
        if (StringUtils.isBlank(appPackageName)) {
            appPackageName = "com.vota.smart.home";
        }
        return appPackageName;
    }

    /**
     * 获取VersionCode
     */
    public int getAppVersionCode() {
        if (appVersionCode == 0) {
            try {
                appVersionCode = MicroApplication.getInstance().appContext.getPackageManager().getPackageInfo(
                        MicroApplication.getInstance().appContext.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException e) {
                appVersionCode = 1;
                e.printStackTrace();
            }
        }
        return appVersionCode;
    }

    /**
     * 获取VersionName
     */
    public String getAppVersionName() {
        if (StringUtils.isBlank(appVersionName)) {
            try {
                appVersionName = MicroApplication.getInstance().appContext.getPackageManager().getPackageInfo(
                        MicroApplication.getInstance().appContext.getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                appVersionName = "";
                e.printStackTrace();
            }
        }
        if (StringUtils.isBlank(appVersionName)) {
            appVersionName = "unknown version name " + getDeviceInfo();
        }
        return appVersionName;
    }

    /**
     * 获取UUID
     */
    public String getDeviceUUID() {
        if (StringUtils.isBlank(deviceUUID)) {
            deviceUUID = getDeviceUuid().toString();
        }
        //如果仍然还是一个空值，赋值随机数
        if (StringUtils.isBlank(deviceUUID)) {
            deviceUUID = null;
        }
        LogUtils.d("Socket connect deviceUUID = " + deviceUUID);
        return deviceUUID;
    }

    /**
     * 获取UUID
     *
     * @param uuid 自动生成的UUID
     */
    public void setDeviceUUID(String uuid) {
        this.deviceUUID = uuid;
    }

    /**
     * Returns a unique UUID for the current android device. As with all UUIDs,
     * this unique ID is "very highly likely" to be unique across all Android
     * devices. Much more so than ANDROID_ID is.
     * <p/>
     * The UUID is generated by using ANDROID_ID as the base key if appropriate,
     * falling back on TelephonyManager.getDeviceID() if ANDROID_ID is known to
     * be incorrect, and finally falling back on a random UUID that's persisted
     * to SharedPreferences if getDeviceID() does not return a usable value.
     */
    public synchronized String getDeviceUuid() {

        if (uuid == null) {
            //本地保存的UUID
            String id = SharedPreferencesUtils.getInstance(MicroApplication.getInstance().getApplicationContext()
                    , SharedPreferencesUtils.UUID).getString(SharedPreferencesUtils.KEY_UUID, null);
            if (StringUtils.isNotBlank(id)) {
                // Use the ids previously computed and stored in the
                // prefs file
                uuid = UUID.fromString(id).toString();
            } else {
                //获取UUID
                uuid = getUniqueUUID();
                // Write the value out to the prefs file
                SharedPreferencesUtils.getInstance(MicroApplication.getInstance().getApplicationContext()
                        , SharedPreferencesUtils.UUID).putString(SharedPreferencesUtils.KEY_UUID, uuid.toString());
            }
        }

        return uuid;
    }

    /**
     * 如果出现getDeviceUuid出现异常，随机生成一个唯一的标示符,生成规则按照
     * mac地址－>SerialNo－>SubscriberId－>SIM卡的序列号－>手机号－>null
     */
    private String getUniqueUUID() {
        TelephonyManager tm = ((TelephonyManager) MicroApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE));
        String uuidString = null;
        UUID uuid = null;
        //android_id
        uuidString = Settings.Secure.getString(MicroApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (StringUtils.isNotBlank(uuidString) && (!"9774d56d682e549c".equals(uuidString))) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("android = " + uuid);
            if (uuid != null) {
                return uuid.toString();
            }
        } else {
            //deviceId
            if (tm == null) {
                uuidString = getOtherUUID();
                return uuidString;
            }
            uuidString = tm.getDeviceId();
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("device id = " + uuid);
            if (uuid != null) {
                return uuid.toString();
            } else {
                uuidString = getOtherUUID();
                return uuidString;
            }
        }

        return "";
    }

    private String getOtherUUID() {
        String uuidString = null;
        UUID uuid = null;
        //判断wifi的mac地址
        uuidString = getMacAddress();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("wifi = " + uuid);
            if (uuid != null) {
                uuidString = "VTappw-" + uuid.toString();
                return uuidString;
            }
        }

        //序列号
        uuidString = getSerialNo();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("SerialNo = " + uuid);
            if (uuid != null) {
                uuidString = "VTappser-" + uuid.toString();
                return uuidString;
            }
        }

        TelephonyManager tm = ((TelephonyManager) MicroApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE));
        if (tm == null) {
            return null;
        }

        //获取手机唯一的用户ID 例如：IMSI(国际移动用户识别码) for a GSM phone.
        uuidString = tm.getSubscriberId();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("Subscriber = " + uuid);
            if (uuid != null) {
                uuidString = "VTappsub-" + uuid.toString();
                return uuidString;
            }
        }
        //获取手机的SIM卡的序列号
        uuidString = tm.getSimSerialNumber();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("SimSerial   = " + uuid);
            if (uuid != null) {
                uuidString = "VTappsim-" + uuid.toString();
                return uuidString;
            }
        }
        //手机号：
        uuidString = tm.getLine1Number();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            LogUtils.d("line  = " + uuid);
            if (uuid != null) {
                uuidString = "VTappline-" + uuid.toString();
                return uuidString;
            }
        }
        return null;
    }


    /**
     * 根据本地是不是保存该随机的UUID来确定是不是需要重新生成UUID
     */

    private UUID getRandomUUID() {
        UUID randomUUID = UUID.randomUUID();
        LogUtils.d("read randomUUID ===  " + randomUUID);

        try {
            String uuid = readUUID();
            LogUtils.d("read uuid ===  " + uuid);
            if (StringUtils.isBlank(uuid)) {
                writeUUID(randomUUID.toString());
            } else {
                randomUUID = getUUIDFromString(uuid);
            }
            LogUtils.d("random uuid =======  " + uuid);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                writeUUID(randomUUID.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return randomUUID;
    }

    //获取手机序列号
    private String getSerialNo() {
        String serialno = Build.SERIAL;
        //防止得出来的值和imei一样
        if (StringUtils.isNotBlank(serialno) && (!"9774d56d682e549c".equals(serialno))) {
            return serialno;
        }
        //若没有build属性,则通过反射去读取字段
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialno = (String) (get.invoke(c, "ro.serialno", ""));
        } catch (Exception e) {
            e.printStackTrace();
            serialno = null;
        }
        if ("9774d56d682e549c".equals(serialno)) {
            return null;
        }
        return serialno;
    }

    //根据String来生成UUID
    private UUID getUUIDFromString(String uuid) {
        UUID result;
        //华为手机在屏蔽权限之后，返回的是15个0，要排除这种情况
        if (StringUtils.isBlank(uuid) || uuid.contains("000000000000")) {
            return null;
        }
        try {
            result = UUID.nameUUIDFromBytes(uuid.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //出现异常则直接赋值随机
            e.printStackTrace();
            result = null;
        }
        return result;
    }


    /**
     * 获取手机型号
     */
    private String getDeviceModel() {
        if (StringUtils.isBlank(deviceMode)) {
            deviceMode = Build.MODEL; //
        }
        return deviceMode;
    }

    /**
     * 获取手机品牌
     */
    private String getDeviceBrand() {

        if (StringUtils.isBlank(deviceBrand)) {
            deviceBrand = Build.BRAND;
        }
        return deviceBrand;
    }

    /**
     * 先获取手机型号,后获取手机品牌,如果获取不出来则显示unknown
     */
    public String getDeviceInfo() {
        deviceInfo = getDeviceModel();
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = getDeviceBrand();
        }
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = "unknown android phone";
        }
        return deviceInfo;
    }

    /**
     * 获取手机的Android版本号
     */
    public String getDeviceVersion() {
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = Build.VERSION.RELEASE;
        }
        //如果还为空的话,取SDK_INT
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = String.valueOf(Build.VERSION.SDK_INT);
        }
        //如果还为空的话,直接赋值android"
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = "unknown sdk " + getDeviceInfo();
        }
        return "android-" + deviceSystemVersion;
    }

    /**
     * .获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    public String getMacAddress() {
        if (StringUtils.isBlank(deviceMac)) {
            WifiManager wifiManager = (WifiManager) MicroApplication.getInstance().appContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            deviceMac = wifiInfo.getMacAddress();
        }

        return deviceMac;
    }

    /**
     * 将随机生成的UUID写入到SD卡
     */
    private void writeUUID(String content) throws IOException {
        String code = AESHelper.encrypt(content, "VoTa");
        String path = getVoTaDirPath(UUID_PATH, "");
        if (StringUtils.isBlank(path)) {
            return;
        }
        writeFile(code, path, UUID_FILE);

    }

    /**
     * 读出SD卡随机生成的UUID
     */
    private String readUUID() throws IOException {
        String result = "";
        String path = getVoTaDirPath(UUID_PATH, "");
        LogUtils.d("readUUID path =======  " + path);
        if (StringUtils.isBlank(path)) {
            return result;
        }
        result = AESHelper.decrypt(readFile(path + UUID_FILE), "VoTa");
        return result;
    }


    /**
     * write the content into the file.
     *
     * @param content  content
     * @param fileName 文件的全路径
     * @throws IOException catch io exception
     */
    public static void writeFile(String content, String path, String fileName) throws IOException {

        File file = new File(path, fileName);
        FileOutputStream outFileStream = new FileOutputStream(file);
        outFileStream.write(content.getBytes());
        outFileStream.flush();
        outFileStream.close();
    }

    /**
     * read file as string
     *
     * @param filePath 文件的全路径
     * @return file content
     * @throws IOException catch io exception
     */
    public static String readFile(String filePath) throws IOException {
        String content = "";
        FileInputStream fin = new FileInputStream(filePath);
        int length = fin.available();
        byte[] buffer = new byte[length];
        fin.read(buffer);
        content = new String(buffer, "UTF-8");
        fin.close();
        return content;
    }


    /**
     * get VoTa dir or subfolder path
     *
     * @param subFolderName subfolder name
     * @return null:VoTa dir path;not null:subfolder path
     */
    public static String getVoTaDirPath(String subFolderName) {
        String path = "";
        path = getVoTaDirPath("VoTa", subFolderName);
        return path;
    }

    /**
     * get VoTa dir or subfolder path
     *
     * @param rootFolderName 根目录
     * @param subFolderName  创建的子文件夹路径
     * @return null:VoTa dir path;not null:subfolder path
     */

    public static String getVoTaDirPath(String rootFolderName, String subFolderName) {
        String path = "";

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return path;
        }

        if (!StringUtils.isBlank(subFolderName)) {
            path = Environment.getExternalStorageDirectory()
                    + File.separator + rootFolderName + File.separator + subFolderName + File.separator;
        } else {
            path = Environment.getExternalStorageDirectory()
                    + File.separator + rootFolderName + File.separator;
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public static String generateClassAndMethodTag() {
        String customTagPrefix = "";
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }
    /**
     * 判断应用是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        boolean hasInstalled = false;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.PERMISSION_GRANTED);
        for (PackageInfo p : list) {
            if (packageName != null && packageName.equals(p.packageName)) {
                hasInstalled = true;
                break;
            }
        }
        return hasInstalled;
    }


}
