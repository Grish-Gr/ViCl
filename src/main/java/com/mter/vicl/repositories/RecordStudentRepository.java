package com.mter.vicl.repositories;

import com.mter.vicl.entities.classroom.RecordStudent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RecordStudentRepository extends CrudRepository<RecordStudent, Long> {
    @Query("SELECT rs FROM RecordStudent rs WHERE classroom = :classroomID")
    Optional<RecordStudent> findByClassroom(Long classroomID);

    @Query("SELECT rs FROM RecordStudent rs WHERE student = :studentID")
    Collection<RecordStudent> findByStudent(Long studentID);

    @Query("SELECT rs FROM RecordStudent rs WHERE classroom = :classroomID and student = :studentID")
    Optional<RecordStudent> findByClassroomAndStudent(Long classroomID, Long studentID);
}
