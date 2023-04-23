package com.mter.vicl.dto.request;

import com.mter.vicl.dto.response.ClassroomDto;
import com.mter.vicl.dto.response.MessageDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.ClassroomMessage;

import java.util.List;

public record ClassroomMessageDto(List<MessageDto> messages, ClassroomDto classroom) {
    public static ClassroomMessageDto from(List<ClassroomMessage> messages, Classroom classroom){
        return new ClassroomMessageDto(
            messages.stream().map(MessageDto::from).toList(),
            ClassroomDto.from(classroom)
        );
    }
}
