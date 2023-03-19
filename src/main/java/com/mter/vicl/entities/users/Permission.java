package com.mter.vicl.entities.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Permission {
    PERSONAL_ACCOUNT_STUDENT,
    PERSONAL_ACCOUNT_TEACHER,
    CREATE_CLASSROOM,
    CHANGE_CLASSROOM,
    READ_CLASSROOM,
    CREATE_TASK,
    PASS_ANSWER,
    READ_ANSWER_TASK;

    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority(this.name());
    }
}
