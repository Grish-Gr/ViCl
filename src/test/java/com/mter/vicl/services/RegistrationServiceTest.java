package com.mter.vicl.services;

import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.services.security.RegistrationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RegistrationServiceTest {

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegistrationService registrationService;

    @Test
    public void testRegistrationStudent(){
        RegistrationFormDto registrationForm = new RegistrationFormDto(
            "Ivan", "Minin", "Borisov", "iv_bor@mail.com", "test", "STUDENT"
        );

        registrationService.register(registrationForm);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Assertions.assertAll(
            () -> Assertions.assertEquals("Ivan", captor.getValue().getName()),
            () -> Assertions.assertEquals("iv_bor@mail.com", captor.getValue().getEmail()),
            () -> Assertions.assertTrue(passwordEncoder.matches("test", captor.getValue().getPassword())),
            () -> Assertions.assertEquals(Role.STUDENT, captor.getValue().getRole())
        );
    }

    @Test
    public void testRegistrationTeacher(){
        RegistrationFormDto registrationForm = new RegistrationFormDto(
            "John", "Verfin", "Borisov", "ver_bor@mail.com", "tst", "TEACHER"
        );

        registrationService.register(registrationForm);

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
        Assertions.assertAll(
            () -> Assertions.assertEquals("John", captor.getValue().getName()),
            () -> Assertions.assertEquals("ver_bor@mail.com", captor.getValue().getEmail()),
            () -> Assertions.assertTrue(passwordEncoder.matches("tst", captor.getValue().getPassword())),
            () -> Assertions.assertEquals(Role.TEACHER, captor.getValue().getRole())
        );
    }
}
