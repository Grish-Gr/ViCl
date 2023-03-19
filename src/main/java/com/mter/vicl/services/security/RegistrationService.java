package com.mter.vicl.services.security;

import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.entities.users.User;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegistrationFormDto registrationForm){
        if (registrationForm.getRole().equals(Role.STUDENT)){
            return registerStudent(registrationForm);
        } else {
            return registerTeacher(registrationForm);
        }
    }

    private Student registerStudent(RegistrationFormDto registrationForm){
        Student student = new Student();
        student.setName(registrationForm.name());
        student.setLastname(registrationForm.lastname());
        student.setSurname(registrationForm.surname());
        student.setEmail(registrationForm.email());
        student.setRole(registrationForm.getRole());
        student.setPassword(passwordEncoder.encode(registrationForm.password()));
        return studentRepository.save(student);
    }

    private Teacher registerTeacher(RegistrationFormDto registrationForm){
        Teacher teacher = new Teacher();
        teacher.setName(registrationForm.name());
        teacher.setLastname(registrationForm.lastname());
        teacher.setSurname(registrationForm.surname());
        teacher.setEmail(registrationForm.email());
        teacher.setRole(registrationForm.getRole());
        teacher.setPassword(passwordEncoder.encode(registrationForm.password()));
        return teacherRepository.save(teacher);
    }
}
