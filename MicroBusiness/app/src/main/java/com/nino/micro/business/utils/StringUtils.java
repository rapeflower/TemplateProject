package com.nino.micro.business.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /***
     * 是不是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        // int strLen;
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String s) {
        return !StringUtils.isBlank(s);
    }

    public static boolean isNullObject(Object obj) {
        return obj == null;
    }

    public static boolean isNotNullObject(Object obj) {
        return !isNullObject(obj);
    }

    /***
     * 是不是数字格式
     *
     * @param str
     * @return
     */
    public static boolean isNumericString(String str) {
        if (isBlank(str))
            return false;
        return str.matches("[0-9]+");
    }

    /***
     * 是不是手机号码
     *
     * @param mobileNo
     * @return
     */
    public static boolean isPhoneNumber(String mobileNo) {
        if (isBlank(mobileNo))
            return false;
        String regex = "^[1][0-9]{10}$";
        return Pattern.matches(regex, mobileNo);
    }

    /***
     * 是不是电话号码
     *
     * @param telephoneNo
     * @return
     */
    public static boolean isTelephoneNumber(String telephoneNo) {
        String regex = "^(([0\\+]\\d{2,3}-?)?(0\\d{2,3})-?)?(\\d{7,8})";

        return Pattern.matches(regex, telephoneNo);
    }

    /***
     * 是不是邮箱地址
     *
     * @param eMail
     * @return
     */
    public static boolean isValidEMail(String eMail) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, eMail);
    }

    /***
     * 是不是有效的6-20为密码
     *
     * @param pwd
     * @return
     */
    public static boolean isValidPwd(String pwd) {
        //String regex = "^(?=.*?[a-zA-Z`~!@#$%^&*()_\\-+={}\\[\\]\\\\|:;\\\"'<>,.?/])[a-zA-Z\\d`~!@#$%^&*()_\\-+={}\\[\\]\\\\|:;\\\"'<>,.?/]{6,20}$";
        String regex = "^[a-zA-Z0-9`~!@#$%^&*()_]{6,20}$";
        return Pattern.matches(regex, pwd);
    }

    /***
     * 是不是有效的6位为数字密码
     *
     * @param pwd
     * @return
     */
    public static boolean isValidPayPwd(String pwd) {
        String regex = "^[0-9]{6}$";
        return Pattern.matches(regex, pwd);
    }

    public static boolean isStartWithAtoZChar(String str) {
        if (isBlank(str)) {
            return false;
        }
        try {
            String cha = str.substring(0, 1);
            return isEnglishCharacter(cha);
        } catch (IndexOutOfBoundsException e) {

        }
        return false;
    }

    /***
     * 是不是英文字母
     *
     * @param str
     * @return
     */
    public static boolean isEnglishCharacter(String str) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }


    /****
     * yyyy年MM月dd日 格式化日期
     *
     * @return
     */
    public static String DateFormatCurrentDay() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        return formatter.format(c.getTime());
    }

    public String splitIt(String str, int length) {
        int loopCount;
        StringBuffer splitedStrBuf = new StringBuffer();
        loopCount = (str.length() % length == 0) ? (str.length() / length) : (str.length() / length + 1);
        System.out.println("Will Split into " + loopCount);
        for (int i = 1; i <= loopCount; i++) {
            if (i == loopCount) {
                splitedStrBuf.append(str.substring((i - 1) * length, str.length()));
            } else {
                splitedStrBuf.append(str.substring((i - 1) * length, (i * length)));
            }
        }

        return splitedStrBuf.toString();
    }

    /**
     * 手机号中间四位隐藏
     *
     * @param mobile 手机号
     * @return
     */
    public static String mobileFormat(String mobile) {
        if (isBlank(mobile)) {
            return mobile;
        }
        if (!isPhoneNumber(mobile)) {
            return mobile;
        }
        String startString = mobile.substring(0, 3);
        String endString = mobile.substring(7);
        return startString + "****" + endString;
    }

    /**
     * 校验输入的是否是字母、数字、汉字
     *
     * @param s
     * @return
     */
    public static boolean isValidEditText(String s) {
        String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        return Pattern.matches(regex, s);
    }

    /**
     * 判断是否是Emoji表情
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return 表情返回true  否则返回false
     */
    public static boolean isEmojiCharacter(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 限制字符串长度
     *
     * @param source 字符串
     * @param limit  限制的长度
     * @param suffix 截取字符串后添加后缀，如"..."，如果不需要，设为null
     * @return
     */
    public static String limitStringLength(String source, int limit, String suffix) {
        int length = source.length();
        StringBuffer sb = new StringBuffer();
        if (length > limit) {
            sb.append(source.substring(0, limit))
                    .append(suffix);
        }
        return sb.toString();
    }

    /**
     * 生成价格字符串
     */
    public static String formatTwoDecimalPriceString(String price) {
        if (isBlank(price)) {
            return price;
        }
        try {
            if (price.startsWith("￥")) {
                price = price.substring(1);
            }
            float priceFloat = Float.valueOf(price);
            DecimalFormat dFormat = new DecimalFormat("0.00");
            price = dFormat.format(priceFloat);
        } catch (NumberFormatException e) {

        }
        return "￥" + price;
    }

    /**
     * @param price
     * @return
     */
    public static String formatNoDecimalPriceString(String price) {
        if (isBlank(price)) {
            return price;
        }
        try {
            if (price.startsWith("￥")) {
                price = price.substring(1);
            }
            float priceFloat = Float.valueOf(price);
            DecimalFormat dFormat = new DecimalFormat("0");
            price = dFormat.format(priceFloat);
        } catch (NumberFormatException e) {

        }
        return "￥" + price;
    }

    /**
     * @param price
     * @return
     */
    public static String formatOneDecimalPriceString(String price) {
        if (isBlank(price)) {
            return price;
        }
        try {
            if (price.startsWith("￥")) {
                price = price.substring(1);
            }
            float priceFloat = Float.valueOf(price);
            DecimalFormat dFormat = new DecimalFormat("0.0");
            price = dFormat.format(priceFloat);
        } catch (NumberFormatException e) {

        }
        return "￥" + price;
    }

    /**
     * 清除电话号码中的空格
     *
     * @return
     */
    private static String clearBlank(String number) {
        if (StringUtils.isNotBlank(number)) {
            return number.replaceAll(" ", "");
        }
        return number;
    }

    /**
     * 电话号码的长度是否符合
     *
     * @param number
     * @return
     */
    private static boolean numberLength(String number) {
        if (StringUtils.isNotBlank(number)) {
            int len = clearBlank(number).length();
            return (len == 11 || len == 14);
        }
        return false;
    }

    /**
     * 过滤掉电话号码中个的"+86"
     */
    public static String filterPlus86AndBlank(String number) {
        if (StringUtils.isNotBlank(number)) {
            if (number.contains("+86")) {
                return clearBlank(number.replace("+86", ""));
            } else {
                return clearBlank(number);
            }
        }

        return number;
    }

    /***
     * 是不是身份证号码
     *
     * @param number
     * @return
     */
    public static boolean isIdentifyNumber(String number) {
        return IdCardUtils.isIdcard(number);
    }


    public static int calculateIndex(CharSequence c, int num) {
        double len = 0;
        int index = -1;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
            if (Math.round(len) == num) {
                return i;
            }
        }

        return index;
    }

    public static boolean isValidVerifyCode(String code) {
        String regex = "[0-9]{6}";
        return Pattern.matches(regex, code);
    }

    public static boolean isValidStringDate(String strDate) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date inputBirthday = fmt.parse(strDate);
            if (new Date().after(inputBirthday)) {
                return true;
            } else {
                return false;

            }
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getBirthday(String cardID) {
        String returnDate = null;
        StringBuffer tempStr = null;
        if (cardID != null && cardID.trim().length() > 0) {
            tempStr = new StringBuffer(cardID.substring(6, 14));
            tempStr.insert(6, '-');
            tempStr.insert(4, '-');
        }
        if (tempStr != null && tempStr.toString().trim().length() > 0) {
            returnDate = tempStr.toString();
        }
        return returnDate;
    }

    public static String getGender(String cardID) {
        String returnGender = null;
        if (cardID != null && cardID.trim().length() > 0) {
            returnGender = cardID.substring(16, 17);
            if (Integer.parseInt(returnGender) % 2 == 0) {
                returnGender = "2";
            } else {
                returnGender = "1";
            }
        }
        return returnGender;
    }

    public static boolean isValidZipCode(String zipCode) {
        String regex = "[0-9]\\d{5}(?!\\d)";
        return Pattern.matches(regex, zipCode);
    }

    public static String getCurrentDate() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(c.getTime());

    }

    public static boolean yes(String str) {
        if (isNotBlank(str) && str.equals("Y")) {
            return true;
        }
        if (isNotBlank(str) && str.equals("y")) {
            return true;
        }
        return false;
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    public static String formatTime(long time) {
        long day = time / (1000 * 60 * 60 * 24);
        long hour = (time / (1000 * 60 * 60) - day * 24);
        long min = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return (day < 10 ? (String.format("0%s", day)) : day) + "天" +
                (hour < 10 ? (String.format("0%s", hour)) : hour) + "时"
                + (min < 10 ? (String.format("0%s", min)) : min) + "分"
                + (s < 10 ? (String.format("0%s", s)) : s) + "秒";
    }

}
