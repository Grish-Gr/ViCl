package com.mter.vicl.security;

import com.mter.vicl.entities.users.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements Principal {

    private String name;
    private Long id;
    private Role role;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
