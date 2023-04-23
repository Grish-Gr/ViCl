package com.mter.vicl.dto.response;

import com.mter.vicl.entities.tasks.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

public record TaskDto(
    Long taskID,
    String title,
    String description,
    Date createDate,
    Date expirationDate,
    Long classroomID,
    List<FileInfoDto> supplementFiles
) {
    public static TaskDto from(Task task){
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCreateDate(),
            task.getExpirationDate(),
            task.getClassroom().getId(),
            task.getSupplementFiles().stream().map(supplement -> FileInfoDto.from(
                supplement,
                getDownloadLinkToSupplement(task.getId(), supplement.getId()))
            ).toList()
        );
    }

    private static URI getDownloadLinkToSupplement(Long taskID, Long supplementID){
        return UriComponentsBuilder.newInstance()
            .path("/api/v1/storage/download/supplement-task")
            .queryParam("taskID", taskID)
            .queryParam("supplementID", supplementID)
            .build()
            .toUri();
    }
}
