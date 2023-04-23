package com.mter.vicl.services.storage;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.entities.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageService {
    List<FileInfo> uploadAll(MultipartFile... files);
    FileInfo upload(MultipartFile file) throws IOException;
    ResourceDto downloadFile(Long fileID) throws IOException;
    FileInfo getInfoByID(Long fileID);
}
