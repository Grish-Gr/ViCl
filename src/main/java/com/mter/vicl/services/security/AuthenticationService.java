package com.mter.vicl.services.security;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.response.JwtResponseDto;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.entities.users.User;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.security.JwtProvider;
import com.mter.vicl.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponseDto getJwtTokens(LoginFormDto loginForm) throws AuthenticationException{
        User user = loginForm.getRole().equals(Role.TEACHER)
            ? getTeacherByEmailAndPassword(loginForm.email(), loginForm.password())
            : getStudentByEmailAndPassword(loginForm.email(), loginForm.password());
        return generateTokensByUser(user);
    }

    public JwtResponseDto refreshJwtTokens(String refreshToken
    ) throws AuthenticationServiceException, NoSuchElementException {
        if (!jwtProvider.validateRefreshToken(refreshToken)){
            throw new AuthenticationServiceException("Invalid jwt token");
        }
        return generateTokensByRefreshToken(refreshToken);
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

    private JwtResponseDto generateTokensByUser(User user){
        String refreshToken = jwtProvider.generateRefreshToken(
            user.getId(),
            user.getRole(),
            user.getName(),
            user.getLastname()
        );
        String accessToken = jwtProvider.generateAccessToken(
            user.getId(),
            user.getRole(),
            user.getName(),
            user.getLastname()
        );
        Long expireTime = jwtUtils.getExpireTimeAccessToken(accessToken);
        return new JwtResponseDto(accessToken, refreshToken, expireTime);
    }

    private JwtResponseDto generateTokensByRefreshToken(String refreshToken){
        String newRefreshToken = jwtUtils.generateNewRefreshToken(refreshToken);
        String accessToken = jwtUtils.generateAccessTokenByRefreshToken(refreshToken);
        Long expireTime = jwtUtils.getExpireTimeAccessToken(accessToken);
        return new JwtResponseDto(accessToken, newRefreshToken, expireTime);
    }
}
