package com.mter.vicl.repositories;

import com.mter.vicl.entities.users.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {
}
