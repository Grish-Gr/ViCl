package com.mter.vicl.dto.response;

import com.mter.vicl.entities.tasks.AnswerTask;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public record AnswerTaskDto(
    Long answerID,
    String answer,
    Long studentID,
    String nameStudent,
    String lastNameStudent,
    List<FileInfoDto> supplementFiles
) {

    public static AnswerTaskDto from(AnswerTask answerTask){
        return new AnswerTaskDto(
            answerTask.getId(),
            answerTask.getAnswer(),
            answerTask.getStudent().getId(),
            answerTask.getStudent().getName(),
            answerTask.getStudent().getLastname(),
            answerTask.getSupplementFiles().stream().map(supplement -> FileInfoDto.from(
                supplement,
                getDownloadLinkToSupplement(answerTask.getId(), supplement.getId()))
            ).toList());
    }

    private static URI getDownloadLinkToSupplement(Long answerID, Long supplementID){
        return UriComponentsBuilder.newInstance()
            .path("/api/v1/storage/download/supplement-answer")
            .queryParam("answerID", answerID)
            .queryParam("supplementID", supplementID)
            .build()
            .toUri();
    }
}
