package io.github.pmat.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Task description must not be empty")
    private String description;

    private Boolean done = Boolean.FALSE;

    private LocalDateTime deadline;

    @Embedded
    private Audit audit = new Audit();

    public Task() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean completed) {
        this.done = completed;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateTask(Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", completed=" + done +
                '}';
    }
}
