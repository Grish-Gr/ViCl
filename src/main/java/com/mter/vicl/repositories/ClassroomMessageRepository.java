package com.mter.vicl.repositories;

import com.mter.vicl.entities.classroom.ClassroomMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomMessageRepository extends CrudRepository<ClassroomMessage, Long> {
}
