package com.mter.vicl.dto.request;

import com.mter.vicl.entities.users.Role;

public record RegistrationFormDto(
    String name,
    String lastname,
    String surname,
    String email,
    String password
) {

}
