package com.mter.vicl.dto.response;

import com.mter.vicl.entities.FileInfo;

import java.net.URI;
import java.util.Date;

public record FileInfoDto(Long fileID, String fileName, Long size, Date uploadDate, URI downloadLink) {

    public static FileInfoDto from(FileInfo fileInfo, URI downloadLink){
        return new FileInfoDto(
            fileInfo.getId(),
            fileInfo.getFileName(),
            fileInfo.getSize(),
            fileInfo.getUploadDate(),
            downloadLink
        );
    }
}
