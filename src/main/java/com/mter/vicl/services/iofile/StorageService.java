package com.mter.vicl.services.iofile;

import com.mter.vicl.dto.response.FileInfoDto;
import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.entities.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    FileInfo upload(MultipartFile file) throws IOException;
    ResourceDto downloadFile(Long fileID) throws IOException;
    FileInfo getInfoByID(Long fileID);
}
