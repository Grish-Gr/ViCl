package com.mter.vicl.repositories;

import com.mter.vicl.entities.tasks.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
