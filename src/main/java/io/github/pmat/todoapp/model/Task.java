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

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup taskGroup) {
        this.description = description;
        this.deadline = deadline;
        if (taskGroup != null) {
            this.group = taskGroup;
        }
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

    public void updateTask(Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(TaskGroup group) {
        this.group = group;
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
