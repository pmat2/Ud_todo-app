package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWriteModel {
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private List<TaskWriteModel> tasks = new ArrayList<>();

    public GroupWriteModel() {
        tasks.add(new TaskWriteModel());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(Project project) {
        TaskGroup result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        result.setProject(project);
        return result;
    }
}
