package com.mter.vicl.services;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.services.storage.StorageService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class FileServiceTest {

    @Inject
    private StorageService manipulateFile;

    @Test
    public void testUploadFile() throws IOException {
        File file = new File("src/main/resources/storage/4d/cf/4dcf24e879a8dfa1f1aa0682a543a579_8c577ae0-5b26-4bb7-80ea-b83b64da044c.xlsx");
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Test_file.xlsx",
            "xlsx",
            new FileInputStream(file)
        );

        FileInfo fileInfo = manipulateFile.upload(multipartFile);

        Assertions.assertEquals(fileInfo.getFileName(), "Test file");
        Assertions.assertEquals(fileInfo.getSize(), multipartFile.getSize());
    }

    @Test
    public void testDownloadFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Something text in file".getBytes()
        );

        FileInfo fileInfo = manipulateFile.upload(multipartFile);
        ResourceDto resource = manipulateFile.downloadFile(fileInfo.getId());

        Assertions.assertEquals(resource.fileInfo().getFileName(), "Test file");
    }

    @Test
    public void testGetFileInfoByID() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
            "Test file",
            "Something text in file".getBytes()
        );

        FileInfo fileInfo = manipulateFile.upload(multipartFile);
        FileInfo fileInfo1 = manipulateFile.getInfoByID(fileInfo.getId());

        Assertions.assertEquals(fileInfo, fileInfo1);
    }
}
