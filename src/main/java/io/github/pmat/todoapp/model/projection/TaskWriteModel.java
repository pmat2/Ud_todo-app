package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.Task;

import java.time.LocalDateTime;

public class TaskWriteModel {
    private String description;
    private LocalDateTime deadline;

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

    public Task toTask(){
        Task result = new Task();
        result.setDescription(description);
        result.setDeadline(deadline);
        return result;
    }
}
