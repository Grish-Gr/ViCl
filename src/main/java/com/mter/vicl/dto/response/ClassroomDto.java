package com.mter.vicl.dto.response;

import com.mter.vicl.entities.classroom.Classroom;

public record ClassroomDto(
    Long id,
    String title,
    String description) {

    public static ClassroomDto from(Classroom classRoom){
        return new ClassroomDto(
            classRoom.getId(),
            classRoom.getTitle(),
            classRoom.getDescription());
    }
}
