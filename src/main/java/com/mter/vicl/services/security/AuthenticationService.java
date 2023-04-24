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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;

@Slf4j
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
    private final HashMap<String, String> storageJwtTokens = new HashMap<>();

    public JwtResponseDto loginInSystem(LoginFormDto loginForm) throws AuthenticationException{
        User user = loginForm.getRole().equals(Role.TEACHER)
            ? getTeacherByEmailAndPassword(loginForm.email(), loginForm.password())
            : getStudentByEmailAndPassword(loginForm.email(), loginForm.password());
        if (!user.isVerificationInSystem()){
            log.error("User is not confirmed account by email");
            throw new AuthenticationServiceException("User is not confirmed account by email");
        }
        JwtResponseDto jwtTokens = generateTokensByUser(user);
        String userIDInSystem = jwtUtils.getUserIDAuthService(jwtTokens.getRefreshToken());
        storageJwtTokens.put(userIDInSystem, jwtTokens.getRefreshToken());
        return generateTokensByUser(user);
    }

    public JwtResponseDto refreshJwtTokens(String refreshToken
    ) throws AuthenticationServiceException, NoSuchElementException {
        if (!jwtProvider.validateRefreshToken(refreshToken)){
            throw new AuthenticationServiceException("Invalid refresh token");
        }
        String userIDInSystem = jwtUtils.getUserIDAuthService(refreshToken);
        if (!storageJwtTokens.containsKey(userIDInSystem) ||
            !storageJwtTokens.get(userIDInSystem).equals(refreshToken)
        ){
            storageJwtTokens.remove(userIDInSystem);
            throw new AuthenticationServiceException("Token has already been refreshed");
        }
        String newRefreshToken = jwtUtils.generateNewRefreshToken(refreshToken);
        String accessToken = jwtUtils.generateAccessTokenByRefreshToken(refreshToken);
        Long expireTime = jwtUtils.getExpireTimeAccessToken(accessToken);
        String role = jwtUtils.getUserRole(refreshToken);
        storageJwtTokens.replace(userIDInSystem, newRefreshToken);
        return new JwtResponseDto(accessToken, newRefreshToken, expireTime, role);
    }

    private Teacher getTeacherByEmailAndPassword(String email, String password) throws AuthenticationException{
        Teacher teacher = teacherRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new AuthenticationServiceException("Invalid email or password")
        );
        if (passwordEncoder.matches(password, teacher.getPassword())){
            return teacher;
        } else {
            throw new AuthenticationServiceException("Invalid email or password");
        }
    }

    private Student getStudentByEmailAndPassword(String email, String password){
        Student student = studentRepository.findByEmailIgnoreCase(email)
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
        return new JwtResponseDto(accessToken, refreshToken, expireTime, user.getRole().name());
    }
}
