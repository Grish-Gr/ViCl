package com.mter.vicl.dto.response;

import com.mter.vicl.entities.classroom.ClassroomMessage;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

public record MessageDto(
    Long id,
    String title,
    String message,
    Date created,
    List<FileInfoDto> supplementFiles
) {
    public static MessageDto from(ClassroomMessage message){
        return new MessageDto(
            message.getId(),
            message.getTitle(),
            message.getMessage(),
            message.getCreateDate(),
            message.getSupplementFiles().stream().map(supplement -> FileInfoDto.from(
                supplement,
                getDownloadLinkToSupplement(message.getId(), supplement.getId()))
            ).toList()
        );
    }

    private static URI getDownloadLinkToSupplement(Long classroomMessageID, Long supplementID){
        return UriComponentsBuilder.newInstance()
            .path("/api/v1/storage/download/supplement-message")
            .queryParam("classroomMessageID", classroomMessageID)
            .queryParam("supplementID", supplementID)
            .build()
            .toUri();
    }
}
