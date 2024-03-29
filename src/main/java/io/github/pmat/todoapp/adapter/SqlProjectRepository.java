package io.github.pmat.todoapp.adapter;

import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.repository.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
//    @Query("from Project p join fetch p.taskGroups")
    List<Project> findAll();
}
