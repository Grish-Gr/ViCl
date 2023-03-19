package com.mter.vicl.dto.request;

import com.mter.vicl.entities.classroom.StatusRecord;

public record StatusRecordStudentFormDto(Long studentID, String statusRecord) {

    public StatusRecord getStatusRecord(){
        return StatusRecord.valueOf(statusRecord);
    }
}
