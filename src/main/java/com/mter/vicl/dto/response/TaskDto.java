package com.mter.vicl.dto.response;

import com.mter.vicl.entities.tasks.Task;

import java.util.Date;

public record TaskDto(Long taskID, String title, String description, Date createDate, Date expirationDate, Long classroomID) {

    public static TaskDto from(Task task){
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCreateDate(),
            task.getExpirationDate(),
            task.getClassroom().getId()
        );
    }
}
