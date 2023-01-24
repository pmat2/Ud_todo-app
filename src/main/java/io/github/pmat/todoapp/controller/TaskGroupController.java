package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.model.projection.GroupWriteModel;
import io.github.pmat.todoapp.repository.TaskRepository;
import io.github.pmat.todoapp.service.TaskGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/group")
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private TaskGroupService service;
    private TaskRepository repository;

    public TaskGroupController(TaskGroupService taskGroupService, TaskRepository repository) {
        this.service = taskGroupService;
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<GroupReadModel>> getTaskGroups(){
        logger.info("[getTaskGroups] - returning all groups");
        return ResponseEntity.ok(service.readAll());
    }

    @PostMapping
    public ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel source){
        logger.info("[createGroup] - create group for: {}", source);
        GroupReadModel result = service.createGroup(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/toggle/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable Integer id){
        logger.info("[toggleGroup] - toggle group with id: {}", id);
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksFromGroup(@PathVariable Integer id){
        logger.info("[getTasksFromGroup] - for group id: {}", id);
        return ResponseEntity.ok(repository.findAllByGroupId(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIAE(IllegalArgumentException exception){
        logger.info("[handleIAE] - caught IllegalArgumentException");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleISE(IllegalStateException exception){
        logger.info("[handleISE] - caught IllegalStateException");
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
