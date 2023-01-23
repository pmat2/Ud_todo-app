package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    public static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        logger.info("[findAllAsync] - supplying async");
        return CompletableFuture.supplyAsync(repository::findAll);
    }
}
