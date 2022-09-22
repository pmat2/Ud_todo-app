package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String decription;
    private Set<TaskWriteModel> tasks;

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Set<TaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(){
        TaskGroup result = new TaskGroup();
        result.setDescription(decription);
        result.setTasks(
                tasks.stream()
                .map(TaskWriteModel::toTask)
                .collect(Collectors.toSet())
        );
        return result;
    }
}
