package io.github.pmat.todoapp.adapter;

import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
    @Override
    @Query("select g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();
}
