package com.three.resource_jpa.jpa.file.service;

import com.three.resource_jpa.jpa.file.entity.FileInfo;
import com.three.resource_jpa.jpa.file.repository.FileInfoRepository;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractFileService {

    public abstract FileInfoRepository getFileInfoRepository();

    public FileInfo upload(MultipartFile file) throws Exception {
        FileInfo fileInfo = FileInfoService.getFileInfo(file);

        // 先根据文件md5查询记录
        FileInfo oldFileInfo = getFileInfoRepository().findByMd5(fileInfo.getMd5());

        if (oldFileInfo != null) {// 如果已存在文件，避免重复上传同一个文件
            return oldFileInfo;
        }

        if (file.getOriginalFilename() == null) {
            throw new NullPointerException("上传文件名称不可以为空");
        }

        if (!file.getOriginalFilename().contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }

        boolean saveOk = uploadFile(file, fileInfo);

        if (saveOk) {
            getFileInfoRepository().save(fileInfo);// 将文件信息保存到数据库
            log.info("上传文件：{}", fileInfo);
            return fileInfo;
        } else {
            throw new NullPointerException("保存文件到服务器硬盘[" + fileInfo.getPath() + "]失败");
        }

    }

    public abstract boolean uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception;

}
