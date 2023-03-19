package com.mter.vicl.dto.response;

import com.mter.vicl.entities.classroom.RecordStudent;

import java.util.Date;

public record RecordStudentDto(Long recordID,
                               Date dateRecord,
                               Long studentID,
                               String nameStudent,
                               String lastnameStudent
) {
    public static RecordStudentDto from(RecordStudent recordStudent){
        return new RecordStudentDto(
            recordStudent.getId(),
            recordStudent.getDateRecord(),
            recordStudent.getStudent().getId(),
            recordStudent.getStudent().getName(),
            recordStudent.getStudent().getLastname()
        );
    }
}
