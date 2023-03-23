package com.mter.vicl.services;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.request.RefreshTokenFormDto;
import com.mter.vicl.dto.response.JwtResponseDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.security.JwtProvider;
import com.mter.vicl.security.JwtUtils;
import com.mter.vicl.services.security.AuthenticationService;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTests {

    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void initTeacherInDB(){
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("Mike");
        teacher.setLastname("Jonson");
        teacher.setPassword(this.passwordEncoder.encode("test"));
        teacher.setEmail("test@mail.com");
        teacher.setRole(Role.TEACHER);
        when(this.teacherRepository.findByEmail("test@mail.com")).thenReturn(
            Optional.of(teacher)
        );
    }

    @BeforeEach
    public void initStudentInDB(){
        Student student = new Student();
        student.setId(1);
        student.setName("Fred");
        student.setLastname("Ivanov");
        student.setPassword(this.passwordEncoder.encode("test_stud"));
        student.setEmail("test_stud@mail.com");
        student.setRole(Role.STUDENT);
        when(this.studentRepository.findByEmail("test_stud@mail.com")).thenReturn(
            Optional.of(student)
        );
    }

    @Test
    public void testLoginTeacher(){
        LoginFormDto loginForm = new LoginFormDto("test@mail.com", "test", "TEACHER");

        JwtResponseDto jwtResponse = authenticationService.getJwtTokens(loginForm);
        JwtAuthentication authentication = jwtUtils.getJwtAuthentication(jwtResponse.getAccessToken());

        Assertions.assertTrue(jwtProvider.validateAccessToken(jwtResponse.getAccessToken()));
        Assertions.assertTrue(jwtProvider.validateRefreshToken(jwtResponse.getRefreshToken()));
        Assertions.assertAll("Check JwtAuthentication",
            () -> Assertions.assertEquals(1, authentication.getUserID()),
            () -> Assertions.assertEquals("Mike", authentication.getName()),
            () -> Assertions.assertEquals("Jonson", authentication.getLastname()),
            () -> Assertions.assertEquals(Role.TEACHER, authentication.getRole())
        );
    }

    @Test
    public void testLoginStudent(){
        LoginFormDto loginFormDto = new LoginFormDto("test_stud@mail.com", "test_stud", "STUDENT");

        JwtResponseDto jwtResponse = authenticationService.getJwtTokens(loginFormDto);
        JwtAuthentication authentication = jwtUtils.getJwtAuthentication(jwtResponse.getAccessToken());

        Assertions.assertTrue(jwtProvider.validateAccessToken(jwtResponse.getAccessToken()));
        Assertions.assertTrue(jwtProvider.validateRefreshToken(jwtResponse.getRefreshToken()));
        Assertions.assertAll("Check JwtAuthentication",
            () -> Assertions.assertEquals(1, authentication.getUserID()),
            () -> Assertions.assertEquals("Fred", authentication.getName()),
            () -> Assertions.assertEquals("Ivanov", authentication.getLastname()),
            () -> Assertions.assertEquals(Role.STUDENT, authentication.getRole())
        );
    }

    @Test
    public void testRefreshTokenStudent(){
        LoginFormDto loginFormDto = new LoginFormDto("test_stud@mail.com", "test_stud", "STUDENT");
        JwtResponseDto jwtResponse = authenticationService.getJwtTokens(loginFormDto);
        RefreshTokenFormDto refreshTokenForm = new RefreshTokenFormDto(jwtResponse.getRefreshToken());

        JwtResponseDto refreshJwtResponse = authenticationService.refreshJwtTokens(refreshTokenForm.refreshToken());
        JwtAuthentication authentication = jwtUtils.getJwtAuthentication(refreshJwtResponse.getAccessToken());

        Assertions.assertTrue(jwtProvider.validateAccessToken(refreshJwtResponse.getAccessToken()));
        Assertions.assertTrue(jwtProvider.validateRefreshToken(refreshJwtResponse.getRefreshToken()));
        Assertions.assertAll("Check JwtAuthentication",
            () ->Assertions.assertEquals(1, authentication.getUserID()),
            () ->Assertions.assertEquals("Fred", authentication.getName()),
            () ->Assertions.assertEquals("Ivanov", authentication.getLastname()),
            () ->Assertions.assertEquals(Role.STUDENT, authentication.getRole())
        );
    }

    @Test
    public void testRefreshTokenTeacher(){
        LoginFormDto loginFormDto = new LoginFormDto("test@mail.com", "test", "TEACHER");
        JwtResponseDto jwtResponse = authenticationService.getJwtTokens(loginFormDto);
        RefreshTokenFormDto refreshTokenForm = new RefreshTokenFormDto(jwtResponse.getRefreshToken());

        JwtResponseDto refreshJwtResponse = authenticationService.refreshJwtTokens(refreshTokenForm.refreshToken());
        JwtAuthentication authentication = jwtUtils.getJwtAuthentication(refreshJwtResponse.getAccessToken());

        Assertions.assertTrue(jwtProvider.validateAccessToken(refreshJwtResponse.getAccessToken()));
        Assertions.assertTrue(jwtProvider.validateRefreshToken(refreshJwtResponse.getRefreshToken()));
        Assertions.assertAll("Check JwtAuthentication",
            () -> Assertions.assertEquals(1, authentication.getUserID()),
            () -> Assertions.assertEquals("Mike", authentication.getName()),
            () -> Assertions.assertEquals("Jonson", authentication.getLastname()),
            () -> Assertions.assertEquals(Role.TEACHER, authentication.getRole())
        );
    }

    @Test
    public void testWrongEmail_LoginStudent(){
        LoginFormDto loginFormDto = new LoginFormDto("test@mail.com", "test_stud", "STUDENT");

        Assertions.assertThrows(AuthenticationException.class,
            () -> authenticationService.getJwtTokens(loginFormDto)
        );
    }

    @Test
    public void testWrongRole_LoginStudent(){
        LoginFormDto loginFormDto = new LoginFormDto("test_stud@mail.com", "test_stud", "TEACHER");

        Assertions.assertThrows(AuthenticationException.class,
            () -> authenticationService.getJwtTokens(loginFormDto)
        );
    }

    @Test
    public void testWrongPassword_LoginTeacher(){
        LoginFormDto loginFormDto = new LoginFormDto("test@mail.com", "tst", "TEACHER");

        Assertions.assertThrows(AuthenticationException.class,
            () -> authenticationService.getJwtTokens(loginFormDto)
        );
    }
}
