package com.mter.vicl.repositories;

import com.mter.vicl.entities.tasks.AnswerTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerTaskRepository extends CrudRepository<AnswerTask, Long> {
}
