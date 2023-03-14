package com.mter.vicl.services.security;

import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegistrationService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student registerStudent(RegistrationFormDto registrationForm){
        Student student = new Student();
        student.setName(registrationForm.name());
        student.setLastname(registrationForm.lastname());
        student.setSurname(registrationForm.surname());
        student.setEmail(registrationForm.email());
        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(registrationForm.password()));
        return studentRepository.save(student);
    }

    public Teacher registerTeacher(RegistrationFormDto registrationForm){
        Teacher teacher = new Teacher();
        teacher.setName(registrationForm.name());
        teacher.setLastname(registrationForm.lastname());
        teacher.setSurname(registrationForm.surname());
        teacher.setEmail(registrationForm.email());
        teacher.setRole(Role.TEACHER);
        teacher.setPassword(passwordEncoder.encode(registrationForm.password()));
        return teacherRepository.save(teacher);
    }
}
