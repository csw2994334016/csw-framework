package com.three.common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class FtpOperationUtil {

    private static final Logger log = LoggerFactory.getLogger(FtpOperationUtil.class);

    public static boolean upload(String hostname, String username, String password, String targetPath, InputStream inputStream) throws SocketException, IOException {
        //实例化ftpClient
        FTPClient ftpClient = new FTPClient();
        //设置登陆超时时间,默认是20s
        ftpClient.setDataTimeout(12000);
        //1.连接服务器
        ftpClient.connect(hostname, 21);
        //2.登录（指定用户名和密码）
        boolean b = ftpClient.login(username, password);
        if (!b) {
            log.error("登录超时");
            if (ftpClient.isConnected()) {
                // 断开连接
                ftpClient.disconnect();
            }
        }
        // 设置字符编码
        ftpClient.setControlEncoding("UTF-8");
        //基本路径，一定存在
        String basePath = "/";
        String[] pathArray = targetPath.split("/");
        for (String path : pathArray) {
            basePath += path + "/";
            //3.指定目录 返回布尔类型 true表示该目录存在
            boolean dirExists = ftpClient.changeWorkingDirectory(basePath);
            //4.如果指定的目录不存在，则创建目录
            if (!dirExists) {
                //此方式，每次，只能创建一级目录
                boolean flag = ftpClient.makeDirectory(basePath);
                if (flag) {
                    log.info("创建成功！");
                }
            }
        }
        //重新指定上传文件的路径
        ftpClient.changeWorkingDirectory(targetPath);
        //5.设置上传文件的方式
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        /**
          * 6.执行上传
          * remote 上传服务后，文件的名称
          * local 文件输入流
          * 上传文件时，如果已经存在同名文件，会被覆盖
          */
        boolean uploadFlag = ftpClient.storeFile(targetPath, inputStream);
        if (uploadFlag) {
            log.info("上传成功！");
        } else {
            log.error("上传失败！");
        }
        return uploadFlag;
    }
}
