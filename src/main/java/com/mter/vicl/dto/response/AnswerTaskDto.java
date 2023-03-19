package com.mter.vicl.dto.response;

import com.mter.vicl.entities.tasks.AnswerTask;

public record AnswerTaskDto(Long answerID,
                            String answer,
                            Long studentID,
                            String nameStudent,
                            String lastNameStudent
) {

    public static AnswerTaskDto from(AnswerTask answerTask){
        return new AnswerTaskDto(
            answerTask.getId(),
            answerTask.getAnswer(),
            answerTask.getStudent().getId(),
            answerTask.getStudent().getName(),
            answerTask.getStudent().getLastname()
        );
    }
}
