package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.Task;

public class TaskReadModel {
    private String description;
    private boolean done;

    public TaskReadModel(Task source) {
        description = source.getDescription();
        done = source.getDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
