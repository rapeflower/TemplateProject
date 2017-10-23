package com.nino.micro.business.utils;

import com.nino.micro.business.utils.logUtils.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-03-16 14:45
 * @Describe 字符工具类
 */
public class CharsetUtils {
    public static final String DEFAULT_ENCODING_CHARSET = "ISO-8859-1";
    public static final List<String> SUPPORT_CHARSET = new ArrayList();

    static {
        SUPPORT_CHARSET.add("ISO-8859-1");
        SUPPORT_CHARSET.add("GB2312");
        SUPPORT_CHARSET.add("GBK");
        SUPPORT_CHARSET.add("GB18030");
        SUPPORT_CHARSET.add("US-ASCII");
        SUPPORT_CHARSET.add("ASCII");
        SUPPORT_CHARSET.add("ISO-2022-KR");
        SUPPORT_CHARSET.add("ISO-8859-2");
        SUPPORT_CHARSET.add("ISO-2022-JP");
        SUPPORT_CHARSET.add("ISO-2022-JP-2");
        SUPPORT_CHARSET.add("UTF-8");
    }

    private CharsetUtils() {
    }

    public static String toCharset(String str, String charset, int judgeCharsetLength) {
        try {
            String ex = getEncoding(str, judgeCharsetLength);
            return new String(str.getBytes(ex), charset);
        } catch (Throwable var4) {
            LogUtils.w(var4);
            return str;
        }
    }

    public static String getEncoding(String str, int judgeCharsetLength) {
        String encode = "ISO-8859-1";
        Iterator var4 = SUPPORT_CHARSET.iterator();

        while(var4.hasNext()) {
            String charset = (String)var4.next();
            if(isCharset(str, charset, judgeCharsetLength)) {
                encode = charset;
                break;
            }
        }

        return encode;
    }

    public static boolean isCharset(String str, String charset, int judgeCharsetLength) {
        try {
            String e = str.length() > judgeCharsetLength?str.substring(0, judgeCharsetLength):str;
            return e.equals(new String(e.getBytes(charset), charset));
        } catch (Throwable var4) {
            return false;
        }
    }
}
