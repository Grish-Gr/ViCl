package com.mter.vicl.dto.response;

import com.mter.vicl.entities.users.User;

public record UserDto(long userID, String name, String lastname, String surname, String email, String role, Boolean verificationInSystem) {

    public static UserDto from(User user){
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getLastname(),
            user.getSurname(),
            user.getEmail(),
            user.getRole().name(),
            user.isVerificationInSystem()
        );
    }
}
