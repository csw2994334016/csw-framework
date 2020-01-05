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

    @Value("${file.local.urlPrefix}")
    private String urlPrefix;
    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.local.path}")
    private String localFilePath;

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

        String filePath = "/" + LocalDate.now().toString().replace("-", "/");

        String suffix = filePath + "/" + fileInfo.getName().substring(0, index) + System.currentTimeMillis() + fileSuffix;

        String path = localFilePath + suffix;
        String url = urlPrefix + suffix;
        fileInfo.setPath(path);
        fileInfo.setUrl(url);

        fileInfo.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        return FileUtil.saveFile(file, path);

    }

    public boolean deleteFile(FileInfo fileInfo) {
        return FileUtil.deleteFile(fileInfo.getPath());
    }
}
