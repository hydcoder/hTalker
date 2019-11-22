package com.hyd.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hydCoder on 2019/11/22.
 * 以梦为马，明日天涯。
 */
public class DateTimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd",
            Locale.ENGLISH);

    /**
     * 返回一个简单的时间字符串
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String getSimpleDate(Date date) {
        return FORMAT.format(date);
    }
}
