package com.mter.vicl.services.security;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    public String getJwtTokenTeacher(LoginFormDto loginForm) throws AuthenticationException{
        Teacher teacher = getTeacherByEmailAndPassword(loginForm.email(), loginForm.password());
        return jwtProvider.generateToken(teacher.getId(), teacher.getRole());
    }

    public String getJwtTokenStudent(LoginFormDto loginFormDto) throws AuthenticationException{
        Student student = getStudentByEmailAndPassword(loginFormDto.email(), loginFormDto.password());
        return jwtProvider.generateToken(student.getId(), student.getRole());
    }

    private Teacher getTeacherByEmailAndPassword(String email, String password) throws AuthenticationException{
        Teacher teacher = teacherRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationServiceException("Invalid email or password")
        );
        if (passwordEncoder.matches(password, teacher.getPassword())){
            return teacher;
        } else {
            throw new AuthenticationServiceException("Invalid email or password");
        }
    }

    private Student getStudentByEmailAndPassword(String email, String password){
        Student student = studentRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationServiceException("Invalid email or password"));
        if (passwordEncoder.matches(password, student.getPassword())){
            return student;
        } else {
            throw new AuthenticationServiceException("Invalid email or password");
        }
    }
}
