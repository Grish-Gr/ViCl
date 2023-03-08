package com.mter.vicl.entities.users;

import com.mter.vicl.entities.users.Property;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    STUDENT(List.of(
        Property.READ_CLASSROOM,
        Property.PASS_TASK
    )),
    TEACHER(List.of(
        Property.READ_CLASSROOM,
        Property.CHANGE_CLASSROOM,
        Property.CRATE_CLASSROOM,
        Property.CREATE_TASK,
        Property.READ_ANSWER_TASK
    ));

    private final List<Property> properties;

    Role(List<Property> properties) {
        this.properties = properties;
    }

    public List<? extends GrantedAuthority> authorities(){
        return properties.stream().map(prop ->
            new SimpleGrantedAuthority(prop.name())
        ).toList();
    }
}
