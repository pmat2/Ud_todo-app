package io.github.pmat.todoapp.repository;

import io.github.pmat.todoapp.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();
    Page<Task> findAll(Pageable page);
    Optional<Task> findById(Integer id);
    List<Task> findByDone(@Param("state") boolean completed);
    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    Task save(Task entity);
    void deleteById(Integer id);

    List<Task> findAllByGroupId(Integer groupId);
}
