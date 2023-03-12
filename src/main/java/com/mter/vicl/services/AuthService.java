package com.mter.vicl.services;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    public Student registerStudent(RegistrationFormDto registrationForm){
        Student student = new Student();
        student.setName(registrationForm.name());
        student.setLastname(registrationForm.lastname());
        student.setSurname(registrationForm.surname());
        student.setEmail(registrationForm.email());
        student.setRole(Role.STUDENT);
        return studentRepository.save(student);
    }

    public Teacher registerTeacher(RegistrationFormDto registrationForm){
        Teacher teacher = new Teacher();
        teacher.setName(registrationForm.name());
        teacher.setLastname(registrationForm.lastname());
        teacher.setSurname(registrationForm.surname());
        teacher.setEmail(registrationForm.email());
        teacher.setRole(Role.TEACHER);
        return teacherRepository.save(teacher);
    }

    public String getJwtToken(LoginFormDto loginForm){

        return "";
    }
}
