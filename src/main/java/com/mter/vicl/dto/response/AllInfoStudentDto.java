package com.mter.vicl.dto.response;

import com.mter.vicl.entities.users.Student;

public record AllInfoStudentDto(Long studentID, String name, String lastname, String surname, String email) {

    public static AllInfoStudentDto from(Student student){
        return new AllInfoStudentDto(
            student.getId(),
            student.getName(),
            student.getLastname(),
            student.getSurname(),
            student.getEmail()
        );
    }
}
