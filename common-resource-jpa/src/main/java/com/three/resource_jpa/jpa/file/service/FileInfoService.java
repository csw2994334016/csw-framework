package com.three.resource_jpa.jpa.file.service;

import com.three.common.utils.FileUtil;
import com.three.common.utils.StringUtil;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.jpa.file.entity.FileInfo;
import com.three.resource_jpa.jpa.file.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FileInfoService extends BaseService<FileInfo, String> {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    public static FileInfo getFileInfo(MultipartFile file) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMd5(FileUtil.fileMd5(file.getInputStream()));// 将文件的md5
        fileInfo.setName(file.getOriginalFilename());
        fileInfo.setContentType(file.getContentType());
        fileInfo.setSize(file.getSize());
        return fileInfo;
    }

    @Transactional
    public FileInfo create(FileInfo fileInfo) {
        return fileInfoRepository.save(fileInfo);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (String id : idSet) {
            FileInfo fileInfo = getEntityById(fileInfoRepository, id);
            fileInfo.setStatus(code);
            fileInfoList.add(fileInfo);
        }
        fileInfoRepository.saveAll(fileInfoList);
    }
}
