package com.mter.vicl.repositories;

import com.mter.vicl.entities.classroom.Classroom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends CrudRepository<Classroom, Long> {
    Optional<Classroom> findBySecretKey(String secretKey);
}
