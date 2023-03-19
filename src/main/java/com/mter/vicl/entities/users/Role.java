package com.mter.vicl.entities.users;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum Role {
    STUDENT(List.of(
        Permission.PERSONAL_ACCOUNT_STUDENT,
        Permission.READ_CLASSROOM,
        Permission.PASS_ANSWER
    )),
    TEACHER(List.of(
        Permission.PERSONAL_ACCOUNT_TEACHER,
        Permission.READ_CLASSROOM,
        Permission.CHANGE_CLASSROOM,
        Permission.CREATE_CLASSROOM,
        Permission.CREATE_TASK,
        Permission.READ_ANSWER_TASK
    ));

    private final List<Permission> properties;

    Role(List<Permission> properties) {
        this.properties = properties;
    }

    public List<? extends GrantedAuthority> authorities(){
        return properties.stream().map(Permission::getAuthority).toList();
    }
}
