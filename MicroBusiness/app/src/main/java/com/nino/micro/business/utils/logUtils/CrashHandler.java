package com.nino.micro.business.utils.logUtils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.nino.micro.business.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/***********
 * @Author rape flower
 * @Date 2017-03-16 14:55
 * @Describe
 * get global uncaught exception and save in the file,and we
 * can find exception easy.
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private static CrashHandler instance;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private SimpleDateFormat timeFormat2 = new SimpleDateFormat("yyMMdd HHmmss");
    private boolean flag = true;
    private Context context;

    //data divide
    private static final String BOUNDARY = "---------------------------7db1c523809b2";
    private static final String SLASH_N = "\n";
    private static final String SLASH_R_SLASH_N = "\r\n";
    private static final String DOUBLE = "--";
    //server ip for saving crash log
    private static final String HOST_PATH = "172.31.90.56";
    //server path
    private static final String URL_PATH = "http://" + HOST_PATH + "/crash/";
    private static final String VoTa_CRASH = "VoTa/Crash/";

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * catch uncaught exception
     *
     * @param arg0
     * @param arg1
     */
    public void uncaughtException(Thread arg0, Throwable arg1) {

        LogUtils.d("This application find exceptions, will be terminated");

        if (flag) {
            // current time
            String currentTime = timeFormat.format(System.currentTimeMillis()) + SLASH_N;

            // version info of current app
            String versionInfo = SLASH_N + "Version Info: " + SLASH_N + getVersionInfo();

            // hardware info of mobile
            String mobileInfo = SLASH_N + "Phone Info: " + SLASH_N + getMobileInfo();

            // crash info
            String errorInfo = SLASH_N + "Crash Info: " + SLASH_N + getErrorInfo(arg1);

            //save file name
            final String fileName = "crash" + "_" + CommonUtils.getInstance().getDeviceInfo() + "_" + timeFormat2.format(System.currentTimeMillis()) + ".txt";

            LogUtils.d(String.format("This application find exceptions,errorInfo: %s", errorInfo));

            try {
                File file = new File(saveCrashInfoDir(), fileName);
                FileOutputStream outFileStream = new FileOutputStream(file);
                outFileStream.write((currentTime + versionInfo + mobileInfo + errorInfo).getBytes());
                outFileStream.flush();
                outFileStream.close();
                new Thread() {
                    public void run() {
                        try {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        } catch (Exception e) {
                            e.printStackTrace();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    /**
     * get dir of save crash info
     *
     * @return
     */
    private String saveCrashInfoDir() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + VoTa_CRASH;
        } else {
            path = context.getFilesDir().getAbsolutePath() + File.separator + VoTa_CRASH;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * // get error info
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        arg1.printStackTrace();
        pw.close();
        String error = writer.toString();
        return error;
    }

    /**
     * get hardware info of mobile
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append(SLASH_N);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * // get version info
     *
     * @return
     */
    private String getVersionInfo() {
        return CommonUtils.getInstance().getDeviceVersion() + SLASH_N;
    }

    /**
     * upload the crash info to server
     *
     * @param username
     * @param password
     * @param path
     * @return
     * @throws Exception
     */
    private boolean uploadHttpURLConnection(String username, String password, String path) throws Exception {
        File file = new File(path);
        StringBuilder sb = new StringBuilder();
        sb.append(DOUBLE + BOUNDARY + SLASH_R_SLASH_N);
        sb.append("Content-Disposition: form-data; name=\"username\"" + SLASH_R_SLASH_N);
        sb.append(SLASH_R_SLASH_N);
        sb.append(username + SLASH_R_SLASH_N);

        sb.append(DOUBLE + BOUNDARY + SLASH_R_SLASH_N);
        sb.append("Content-Disposition: form-data; name=\"password\"" + SLASH_R_SLASH_N);
        sb.append(SLASH_R_SLASH_N);
        sb.append(password + SLASH_R_SLASH_N);

        sb.append(DOUBLE + BOUNDARY + SLASH_R_SLASH_N);
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + path + "\"" + SLASH_R_SLASH_N);
        sb.append("Content-Type: image/pjpeg" + SLASH_R_SLASH_N);
        sb.append(SLASH_R_SLASH_N);

        byte[] before = sb.toString().getBytes("UTF-8");
        byte[] after = (SLASH_R_SLASH_N + DOUBLE + BOUNDARY + DOUBLE + SLASH_R_SLASH_N).getBytes("UTF-8");

        URL url = new URL(URL_PATH);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("Content-Length", String.valueOf(before.length + file.length() + after.length));
        conn.setRequestProperty("HOST", HOST_PATH);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        InputStream in = new FileInputStream(file);

        out.write(before);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1)
            out.write(buf, 0, len);

        out.write(after);

        in.close();
        out.close();
        return conn.getResponseCode() == 200;
    }

}