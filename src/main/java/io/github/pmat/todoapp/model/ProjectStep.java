package io.github.pmat.todoapp.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "project_steps")
public class ProjectStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Project steps description must not be empty")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private int daysToDeadline;

    public ProjectStep() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Project getProjects() {
        return project;
    }
    public void setProjects(Project project) {
        this.project = project;
    }
    public int getDaysToDeadline() {
        return daysToDeadline;
    }
    public void setDaysToDeadline(int daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
}
