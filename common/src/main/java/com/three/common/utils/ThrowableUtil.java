package com.three.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by csw on 2019/03/30.
 * Description:异常工具
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
