package com.mter.vicl.repositories;

import com.mter.vicl.entities.users.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Optional<Teacher> findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);
}
