package io.github.pmat.todoapp.repository;

import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    List<Project> findAll();
    Optional<Project> findById(Integer id);
    Project save(Project entity);
}
