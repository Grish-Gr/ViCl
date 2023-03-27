package com.mter.vicl.services.iofile;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.repositories.FileInfoRepository;
import com.mter.vicl.services.exceptions.FileStorageException;
import com.mter.vicl.services.iofile.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService implements StorageService {

    @Autowired
    private FileManager fileManager;
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public FileInfo upload(MultipartFile file) throws IOException {
        String pathToFile = fileManager.saveFileIntoStorage(file);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setPathToFile(pathToFile);
        fileInfo.setUploadDate(new Date());
        fileInfo.setSize(file.getSize());
        return fileInfoRepository.save(fileInfo);
    }

    @Override
    public ResourceDto downloadFile(Long fileID) throws IOException {
        FileInfo fileInfo = fileInfoRepository.findById(fileID).orElseThrow();
        Resource resource = fileManager.downloadFileIntoStorage(fileInfo.getPathToFile());
        return new ResourceDto(resource, fileInfo);
    }

    @Override
    public FileInfo getInfoByID(Long fileID) {
        return null;
    }
}
