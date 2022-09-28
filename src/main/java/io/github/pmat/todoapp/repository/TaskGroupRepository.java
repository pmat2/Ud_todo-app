package io.github.pmat.todoapp.repository;

import io.github.pmat.todoapp.model.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();
    Optional<TaskGroup> findById(Integer id);
    TaskGroup save(TaskGroup entity);
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
