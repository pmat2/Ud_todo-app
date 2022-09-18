package io.github.pmat.todoapp.repository;

import io.github.pmat.todoapp.model.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {
}
