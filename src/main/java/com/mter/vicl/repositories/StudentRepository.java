package com.mter.vicl.repositories;

import com.mter.vicl.entities.users.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}
