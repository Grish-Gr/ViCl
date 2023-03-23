package com.mter.vicl.services.storage_files;

import com.mter.vicl.dto.response.FileInfoDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ManipulateFile {
    FileInfoDto upload(MultipartFile file) throws IOException;
    Resource downloadFile(Long fileID) throws IOException;
    FileInfoDto getInfoByID(Long fileID);
}
