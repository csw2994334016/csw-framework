package com.three.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 字符串工具类
 *
 * @author cssw
 * @date 2017-11-10 上午11:03:50
 */
public class StringUtil {

    private static final char SEPARATOR = '_';

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty() || str.replaceAll(" ", "").isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(String... strs) {
        for (int i = 0; i < strs.length; i++) {
            if (isBlank(strs[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean contains(String str, String key) {
        if (str != null && str.contains(key)) {
            return true;
        }
        return false;
    }

    public static boolean contains(String... strs) {
        for (int i = 0; i < strs.length - 1; i++) {
            if (contains(strs[i], strs[strs.length - 1])) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] strs, String key) {
        for (int i = 0; i < strs.length; i++) {
            if (contains(strs[i], key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母大写
     *
     * @param in
     * @return
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        return head.toUpperCase() + in.substring(1, in.length());
    }

    public static String getStr(String str) {
        return str == null ? "" : str;
    }

    public static Set<Long> getStrToIdSet(String ids) {
        String[] idArray = StringUtils.split(ids, ",");
        return getStringArrayToIdSet(idArray);
    }

    public static Set<Long> getStringArrayToIdSet(String[] ids) {
        Set<Long> idSet = new HashSet<>();
        if (ids != null) {
            for (String id : ids) {
                if (isNotBlank(id)) {
                    idSet.add(Long.valueOf(id));
                }
            }
        }
        return idSet;
    }

    public static Set<String> getStrToIdSet1(String ids) {
        String[] idArray = StringUtils.split(ids, ",");
        return getStringArrayToIdSet1(idArray);
    }

    public static Set<String> getStringArrayToIdSet1(String[] ids) {
        Set<String> idSet = new HashSet<>();
        if (ids != null) {
            for (String id : ids) {
                if (isNotBlank(id)) {
                    idSet.add(id);
                }
            }
        }
        return idSet;
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getStrToDate(String time) {
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCapitalizeCamelCase(" hello_world ") == "HelloWorld"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toUnderScoreCase(" helloWorld ") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 首字母转小写
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (!isEmpty(str) && !isEmpty(prefix)) {
            String str2 = str.toString();
            return str2.startsWith(prefix.toString()) ? subSuf(str2, prefix.length()) : str2;
        } else {
            return str(str);
        }
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static String subSuf(CharSequence string, int fromIndex) {
        return isEmpty(string) ? null : sub(string, fromIndex, string.length());
    }

    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return str(str);
        } else {
            int len = str.length();
            if (fromIndex < 0) {
                fromIndex += len;
                if (fromIndex < 0) {
                    fromIndex = 0;
                }
            } else if (fromIndex > len) {
                fromIndex = len;
            }

            if (toIndex < 0) {
                toIndex += len;
                if (toIndex < 0) {
                    toIndex = len;
                }
            } else if (toIndex > len) {
                toIndex = len;
            }

            if (toIndex < fromIndex) {
                int tmp = fromIndex;
                fromIndex = toIndex;
                toIndex = tmp;
            }

            return fromIndex == toIndex ? "" : str.toString().substring(fromIndex, toIndex);
        }
    }
}
