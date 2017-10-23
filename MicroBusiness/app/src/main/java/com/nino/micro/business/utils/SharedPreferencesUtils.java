package com.nino.micro.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nino.micro.business.application.MicroApplication;

/***********
 * @Author rape flower
 * @Date 2017-03-16 14:45
 * @Describe
 * SharedPreferences帮助类
 * 用户的信息保存在 SP_FILE_NAME_USER_PROFILE，之所以不在SP_FILE_NAME_USER_PROFILE这个文件中进行保存，
 * 是因为退出用户登录的时候，会将该file清空
 */
public class SharedPreferencesUtils {
    public static final String MICRO = "micro";
    public static final String KEY_OPEN_APP = "key_open_app";//用来保存应用是不是完全退出
    public static final String UUID = "device_id.xml";
    public static final String KEY_UUID = "device_id";
    /**
     * 保存当前的年
     */
    public static final String KEY_CURRENT_YEAR = "key_current_year";
    /**
     * 保存当前的月
     */
    public static final String KEY_CURRENT_MONTH = "key_current_month";//
    /**
     * 保存当前的日
     */
    public static final String KEY_CURRENT_DAY = "key_current_day";//

    /**
     * 版本更新稍后提醒，如果用户执行该操作之后，一天之内不在提醒
     */
    public static final String KEY_NEW_VERSION_REMINDER_LATER = "key_new_version_reminder_later";

    /**
     * 版本更新稍后提醒，如果用户执行该操作之后，一天之内不在提醒
     */
    public static final String KEY_NEW_VERSION_FORCE_UPDATE = "key_new_version_force_update";
    /**
     * 标记健康去哪儿是否已读，如果用户点击已读之后，一天之内不在提醒为未读
     */
    public static final String KEY_HEALTH_NEWS_UNREAD = "key_health_news_unread";
    /**
     * 悬浮窗参数shared preference
     */
    public static final String KEY_SUSPEND_PARAMETER_X = "x";
    public static final String KEY_SUSPEND_PARAMETER_Y = "y";

    /**
     * 应用第一次启动
     */
    public static final String KEY_LOGIN = "some_times";//
    public static final String VALUE_LOGIN = "second";//
    /**
     * 搜索页面的关键字
     */
    public static final String KEY_HOME_KEYWORD = "key_home_keyword";

    /**
     * 是否允许读取系统通讯录选择联系人
     */
    public static final String READ_SYSTEM_CONTACT = "read_system_contact";

    /**
     * 填写订单页面保存的选择物流logisticCompanyIndex
     */
    public static final String LOGISTIC_COMPANY_INDEX = "logistic_company_index";

    /**
     * 填写订单页面保存的地址memberAddressId
     */
    public static final String MEMBER_ADDRESS_ID = "member_address_id";

    /***
     * 用来保存广告的imageUrl地址
     */
    public static final String KEY_ADVERT_FULL = "key_advert_full";
    public static final String KEY_ADVERT_CLOSE = "key_advert_close";
    public static final String KEY_ADVERT_SPECIAL_HOME = "key_advert_SpecialFloatingHome";
    public static final String KEY_ADVERT_SPECIAL_ME = "key_advert_SpecialFloatingMe";

    /***
     * 用来保存埋点文件名称
     */
    public static final String COUNT_TXT_FILE_NAME = "count_txt_file_name";
    /**
     * 用来保存是否提醒用户签到
     */
    public static final String REMIND_SIGN_In = "remind_sign_in";

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;

    private SharedPreferencesUtils() {
    }

    /**
     * 获取一个app默认的SharedPreferencesUtils
     */
    public synchronized static SharedPreferencesUtils getInstance() {
        sharedPreferencesUtils = getInstance(MicroApplication.getInstance().getApplicationContext());
        return sharedPreferencesUtils;

    }

    /**
     * 获取一个SharedPreferencesUtils实例,并创建SharedPreferences
     */
    public synchronized static SharedPreferencesUtils getInstance(Context context, String name) {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtils();
        }
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferencesUtils;

    }

    /**
     * 获取一个SharedPreferencesUtils实例,并创建SharedPreferences
     * <p/>
     * 默认的都写入MICRO，创建该MICRO的sharedPreferences是放在WelcomeActivity中，其余的均可以用这个方法获取
     */
    public synchronized static SharedPreferencesUtils getInstance(Context context) {
        sharedPreferencesUtils = getInstance(context, MICRO);
        return sharedPreferencesUtils;
    }

    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putString(String key, String value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public String getString(String key, String defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        String value = sharedPreferences.getString(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        boolean value = sharedPreferences.getBoolean(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putInt(String key, int value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public int getInt(String key, int defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        int value = sharedPreferences.getInt(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putLong(String key, long value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public long getLong(String key, long defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        long value = sharedPreferences.getLong(key, defValue);
        return value;
    }
}
