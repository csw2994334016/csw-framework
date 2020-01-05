package com.three.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	/**
	 * 文件的md5
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String fileMd5(InputStream inputStream) {
		try {
			return DigestUtils.md5Hex(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将文件保存到本地
	 *  @param file
	 * @param path
	 */
	public static boolean saveFile(MultipartFile file, String path) {
		try {
			File targetFile = new File(path);
			if (targetFile.exists()) {
				return true;
			}

			// 判断文件路径是否存在，否则创建目录
			boolean mkdirsFlag = true;
			if (!targetFile.getParentFile().exists()) {
				mkdirsFlag = targetFile.getParentFile().mkdirs();
			}
			if (mkdirsFlag) {
				file.transferTo(targetFile);
			}
			return targetFile.exists();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除本地文件
	 *
	 * @param pathname
	 * @return
	 */
	public static boolean deleteFile(String pathname) {
		File file = new File(pathname);
		if (file.exists()) {
			boolean flag = file.delete();

			if (flag) {
				File[] files = file.getParentFile().listFiles();
				if (files == null || files.length == 0) {
					file.getParentFile().delete();
				}
			}

			return flag;
		}

		return false;
	}
}
