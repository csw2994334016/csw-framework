package com.three.user.service;

import com.three.common.utils.FileUtil;
import com.three.resource_jpa.jpa.file.entity.FileInfo;
import com.three.resource_jpa.jpa.file.repository.FileInfoRepository;
import com.three.resource_jpa.jpa.file.service.AbstractFileService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class UploadFileService extends AbstractFileService {

//    @Value("${file.ftp.hostname}")
//    private String hostname;
//
//    @Value("${file.ftp.username}")
//    private String username;
//
//    @Value("${file.ftp.password}")
//    private String password;

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.local.path}")
    private String fileLocalPath;

    @Value("${file.urlPrefix}")
    private String urlPrefix;

    @Value("${file.prefix}")
    private String prefix;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public FileInfoRepository getFileInfoRepository() {
        return fileInfoRepository;
    }

    @Override
    public boolean uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
        int index = fileInfo.getName().lastIndexOf(".");
        // 文件扩展名
        String fileSuffix = fileInfo.getName().substring(index);
        // 文件名
        String fileName = fileInfo.getName().substring(0, index);

        String addPath = "/" + LocalDate.now().toString().replace("-", "/");

        String suffix = addPath + "/" + fileName + System.currentTimeMillis() + fileSuffix;

        String savePath = fileLocalPath + prefix + suffix;
        String url = urlPrefix + prefix + suffix;
        fileInfo.setPath(savePath);
        fileInfo.setUrl(url);

        fileInfo.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        return FileUtil.saveFile(file, savePath);
//        return FtpOperationUtil.upload(hostname, username, password, path, file.getInputStream());
    }

    public boolean deleteFile(FileInfo fileInfo) {
        return FileUtil.deleteFile(fileInfo.getPath());
    }
}
