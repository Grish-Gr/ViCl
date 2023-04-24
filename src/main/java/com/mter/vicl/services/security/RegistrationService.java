package com.mter.vicl.services.security;

import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.entities.users.*;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.repositories.VerificationTokenRepository;
import com.mter.vicl.services.email.EmailService;
import com.mter.vicl.services.exceptions.AuthenticationInSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public User register(RegistrationFormDto registrationForm){
        User user = registrationForm.getRole().equals(Role.STUDENT)
            ? registerStudent(registrationForm)
            : registerTeacher(registrationForm);
        sendConfirmRegistrationMessage(user);
        return user;
    }

    @Transactional
    public User confirmUserInSystem(String verificationToken) throws AuthenticationInSystemException{
        VerificationToken token = verificationTokenRepository.findById(verificationToken).orElseThrow(() ->
            new AuthenticationInSystemException("Verification token is not valid")
        );
        if (token.getRole().equals(Role.STUDENT)){
            Student student = studentRepository.findByEmailIgnoreCase(token.getEmailUser()).orElseThrow();
            student.setVerificationInSystem(true);
            studentRepository.save(student);
            return student;
        } else {
            Teacher teacher = teacherRepository.findByEmailIgnoreCase(token.getEmailUser()).orElseThrow();
            teacher.setVerificationInSystem(true);
            teacherRepository.save(teacher);
            return teacher;
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
        student.setVerificationInSystem(false);
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
        teacher.setVerificationInSystem(false);
        return teacherRepository.save(teacher);
    }

    private void sendConfirmRegistrationMessage(User user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setVerificationToken(UUID.randomUUID().toString());
        verificationToken.setRole(user.getRole());
        verificationToken.setEmailUser(user.getEmail());
        verificationTokenRepository.save(verificationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
            + "http://localhost:8080/api/v1/auth/confirm-account?token=" + verificationToken.getVerificationToken());
        emailService.sendEmail(mailMessage);
    }
}
