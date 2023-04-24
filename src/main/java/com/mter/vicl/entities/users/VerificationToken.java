package com.mter.vicl.entities.users;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @Column(name="confirmed_token")
    private String verificationToken;

    @Column(name = "email")
    private String emailUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_user")
    private Role role;
}
