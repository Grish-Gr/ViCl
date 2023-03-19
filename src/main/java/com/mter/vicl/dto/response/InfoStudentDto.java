package com.mter.vicl.dto.response;

import com.mter.vicl.entities.users.Student;

public record InfoStudentDto(String name, String lastname) {

    public static InfoStudentDto from(Student student){
        return new InfoStudentDto(student.getName(), student.getLastname());
    }
}
