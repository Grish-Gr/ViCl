package com.mter.vicl.dto.request;

import com.mter.vicl.entities.users.Role;

public record RegistrationFormDto(
    String name,
    String lastname,
    String surname,
    String email,
    String password,
    String role
) {
    public Role getRole(){
        return Role.valueOf(role);
    }
}
