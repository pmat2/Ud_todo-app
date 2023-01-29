package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.repository.TaskRepository;
import io.github.pmat.todoapp.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

//    @GetMapping(params = {"!sort", "!page", "!size"})
//    public CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
//        logger.info("[readAllTasks] invoked, returning all tasks");
//        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
//    }
    @GetMapping(params = {"!sort", "!page", "!size"})
    public ResponseEntity<List<Task>> readAllTasks(){
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("[readAllTasks] invoked, returning all tasks with pagination");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Integer id){
        logger.info("[getTask] invoked, returning task id: {}", id);
        return taskRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    public ResponseEntity<List<Task>> getDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        logger.info("[getDoneTasks] invoked for state = {}", state);
        return ResponseEntity.ok(taskRepository
                .findByDone(state));
    }

    @GetMapping("/today")
    public ResponseEntity<List<Task>> getTasksDueToday(){
        logger.info("[getTasksDueToday] - returning all tasks after deadline, deadline today, or no deadline");
        return ResponseEntity.ok(taskRepository.findByDoneIsFalseAndDeadlineLessThanEqualOrDeadlineIsNull(LocalDateTime.now()));
    }

    @PostMapping
    public ResponseEntity<Task> postTask(@RequestBody @Valid Task task){
        logger.info("[postTask] invoked, adding new task: {}", task.toString());
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable Integer id){
        if(!taskRepository.existsById(id)){
            logger.error("[toggleTask] invoked, for id: {}, task with that id not found in database", id);
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        logger.info("[updateTask] invoked for id: {}, toggled done to: {}", id, taskRepository.findById(id).orElse(null).isDone());
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @RequestBody @Valid Task target){
        logger.info("updateTask({}) invoked, updating task id: {}", id, id);
        if(!taskRepository.existsById(id)){
            logger.error("[updateTask] invoked, for id: {}, task with that id not found in database", id);
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                        .ifPresent(task -> task.updateFrom(target));
        logger.info("pupdateTask] invoked for id: {}, updated task to: {}", id, target);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer id){
        if(!taskRepository.existsById(id)){
            logger.info("pdeleteTask] invoked, failed to find task with id: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("[deleteTask] invoked, deleting task with id: {}", id);
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
