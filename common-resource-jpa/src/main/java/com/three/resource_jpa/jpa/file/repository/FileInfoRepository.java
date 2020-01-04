package com.three.resource_jpa.jpa.file.repository;

import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.resource_jpa.jpa.file.entity.FileInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by csw on 2019/09/07.
 * Description:
 */
@Repository()
public interface FileInfoRepository extends BaseRepository<FileInfo, String> {

    FileInfo findByMd5(String fileMd5);
}
