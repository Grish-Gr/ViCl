package com.mter.vicl.services;

import com.mter.vicl.dto.response.FileInfoDto;
import com.mter.vicl.services.storage_files.ManipulateFile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootTest
public class FileServiceTest {

    @Inject
    private ManipulateFile manipulateFile;

    @Test
    public void testUploadFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Something text in file".getBytes()
        );

        FileInfoDto fileInfoDto = manipulateFile.upload(multipartFile);

        Assertions.assertEquals(fileInfoDto.name(), "Test file");
        Assertions.assertEquals(fileInfoDto.size(), multipartFile.getSize());
    }

    @Test
    public void testDownloadFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Something text in file".getBytes()
        );

        FileInfoDto fileInfoDto = manipulateFile.upload(multipartFile);
        Resource resource = manipulateFile.downloadFile(fileInfoDto.fileID());

        Assertions.assertEquals(resource.getFilename(), "Test file");
    }

    @Test
    public void testGetFileInfoByID() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Something text in file".getBytes()
        );

        FileInfoDto fileInfoDto = manipulateFile.upload(multipartFile);
        FileInfoDto fileInfoDto1 = manipulateFile.getInfoByID(fileInfoDto.fileID());

        Assertions.assertEquals(fileInfoDto, fileInfoDto1);
    }
}
