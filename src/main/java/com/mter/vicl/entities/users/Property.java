package com.mter.vicl.entities.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Property {
    CRATE_CLASSROOM,
    CHANGE_CLASSROOM,
    READ_CLASSROOM,
    CREATE_TASK,
    PASS_TASK,
    READ_ANSWER_TASK;

    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority(this.name());
    }
}
