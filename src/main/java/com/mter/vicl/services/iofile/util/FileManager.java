package com.mter.vicl.services.iofile.util;

import com.mter.vicl.services.exceptions.FileStorageException;
import com.mter.vicl.services.exceptions.NotFoundFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileManager {

    private final Path pathToStorage;

    public FileManager(@Value("${storage-file.upload-dir}") String uploadDir){
        this.pathToStorage = Paths.get(uploadDir);
    }

    public String saveFileIntoStorage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }
        try (InputStream inputStream = file.getInputStream()) {
            String md5sum = getMD5ChecksumFile(file.getInputStream());
            String pathToFile = getPathToFile(md5sum, file.getOriginalFilename());
            Path destinationFile = this.pathToStorage.resolve(Paths.get(pathToFile));
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return pathToFile;
        }
    }

    public Resource downloadFileIntoStorage(String pathToFile) throws NotFoundFileException {
        try {
            Path file = this.pathToStorage.resolve(pathToFile);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new NotFoundFileException("");
            }
        } catch (MalformedURLException e) {
            throw new NotFoundFileException("");
        }
    }

    private String getPathToFile(String md5, String fileName) throws FileStorageException {
        String pathDirs = String.format("%s\\%s\\%s",
            this.pathToStorage, md5.substring(0, 2), md5.substring(2, 4)
        );
        if (!createDirs(pathDirs)){
            throw new FileStorageException("Fail create dirs to file");
        }
        return String.format("%s\\%s\\%s_%s",
            md5.substring(0, 2), md5.substring(2, 4), md5, getNamingFile(fileName)
        );
    }

    private boolean createDirs(String dirsToFile){
        File dirs = new File(dirsToFile);
        if (dirs.exists())
            return true;
        return dirs.mkdirs();
    }

    private String getMD5ChecksumFile(InputStream inputStream) throws IOException {
        return DigestUtils.md5DigestAsHex(inputStream);
    }

    private String getNamingFile(String originalNameFile){
        String fileExtension = originalNameFile.substring(originalNameFile.lastIndexOf('.'));
        return UUID.randomUUID() + fileExtension;
    }
}
