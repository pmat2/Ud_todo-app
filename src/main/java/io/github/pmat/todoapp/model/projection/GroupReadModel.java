package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    private String description;
    private LocalDateTime deadline;
    private boolean done;
    private Set<TaskReadModel> tasks;


    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        done = source.isDone();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(TaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<TaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
