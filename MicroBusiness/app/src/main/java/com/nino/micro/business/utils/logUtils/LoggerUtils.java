package com.nino.micro.business.utils.logUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nino.micro.business.utils.CommonUtils;
import com.nino.micro.business.utils.Constants;
import com.nino.micro.business.utils.DateFormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 保存log到文件 tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 *
 * @author lz
 * @version 2014年12月15日09:34:15
 */
public class LoggerUtils implements LogUtils.CustomLogger {

    private static String PATH_LOGCAT;
    private static String name;

    public LoggerUtils(Context context) {
        //创建log文件
        createLogFile(context);
        //设置是否显示、输出log日志
        LogUtils.allowD = CommonUtils.getInstance().isDebugMode();
        LogUtils.allowE = CommonUtils.getInstance().isDebugMode();
        LogUtils.allowI = CommonUtils.getInstance().isDebugMode();
        LogUtils.allowV = CommonUtils.getInstance().isDebugMode();
        LogUtils.allowW = CommonUtils.getInstance().isDebugMode();
        LogUtils.allowWtf = CommonUtils.getInstance().isDebugMode();
        //设置是否捕获崩溃log
        if (CommonUtils.getInstance().isDebugMode()) {
            CrashHandler handler = CrashHandler.getInstance();
            handler.init(context.getApplicationContext());
        }
    }

    @Override
    public void d(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.d(tag, content);
    }

    @Override
    public void d(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.d(tag, content, tr);
    }

    @Override
    public void e(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.e(tag, content);
    }

    @Override
    public void e(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.e(tag, content, tr);
    }

    @Override
    public void i(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.i(tag, content);
    }

    @Override
    public void i(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.i(tag, content, tr);
    }

    @Override
    public void v(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.v(tag, content);
    }

    @Override
    public void v(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.v(tag, content, tr);
    }

    @Override
    public void w(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.w(tag, content);
    }

    @Override
    public void w(String tag, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + tr + "\r\n");
        Log.w(tag, tr);
    }

    @Override
    public void w(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.w(tag, content, tr);
    }

    @Override
    public void wtf(String tag, String content) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + "\r\n");
        Log.wtf(tag, content);
    }

    @Override
    public void wtf(String tag, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + tr + "\r\n");
        Log.wtf(tag, tr);
    }

    @Override
    public void wtf(String tag, String content, Throwable tr) {
        saveToFile(PATH_LOGCAT, name, DateFormatUtils.getCurrentDateTime() + " " + tag + " " + content + " " + tr
                + "\r\n");
        Log.wtf(tag, content, tr);
    }

    /**
     * 生成log文件
     */
    private void createLogFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory().getPath() + File.separator
                    + Constants.PATIENT_LOG_FILE_PATH;
        } else {
            // 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getPath() + File.separator + Constants.PATIENT_LOG_FILE_PATH;
        }

        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 时间格式，log文件命名时用到
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        name = dateFormat.format(new Date());
    }

    /**
     * 保存到文件
     */
    private synchronized void saveToFile(String path, String name, String content) {
        if (!CommonUtils.getInstance().isDebugMode()) {
            return;
        }
        FileOutputStream fos = null;
        synchronized (this) {
            try {
                fos = new FileOutputStream(new File(path, name + "log.txt"), true);
                fos.write(content.getBytes());
            } catch (IOException e) {
            } catch (Exception e) {
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
            }
        }
    }
}
