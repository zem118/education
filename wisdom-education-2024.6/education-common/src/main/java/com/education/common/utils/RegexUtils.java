package com.education.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   
 *   
 */
public class RegexUtils {

    // 手机号正则校验
    public static final String MOBILE_REGEX = "1\\d{10}";

    public static boolean compile(String regex, Object value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(value));
        return matcher.matches();
    }
}
